package ru.jetlabs.ts.tourdataservice

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@SpringBootApplication
class TourDataServiceApplication

@RestController
@RequestMapping("/ts-tour-data-service/api/v1/bookings")
class BookingController(
    val bookingService: BookingService
) {
    @GetMapping("/{id}")
    fun getBookingDataByTourId(@PathVariable("id") id: Long): ResponseEntity<*> =
        bookingService.getBookingDataByTourId(tourId = id).let {
            when (it) {
                is GetBookingDataByTourIdResult.Success -> ResponseEntity.status(HttpStatus.OK).body(it.data)
                is GetBookingDataByTourIdResult.NotFound -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it)
            }
        }
}

@Component
@Transactional
class BookingService {
    fun getBookingDataByTourId(tourId: Long): GetBookingDataByTourIdResult =
        HotelBookings.join(
            otherTable = Hotels,
            joinType = JoinType.FULL,
            onColumn = HotelBookings.hotel,
            otherColumn = Hotels.id
        ).join(
            otherTable = HotelRooms,
            joinType = JoinType.FULL,
            onColumn = HotelBookings.room,
            otherColumn = HotelRooms.id,
        ).join(
            otherTable = HotelNutritions,
            joinType = JoinType.FULL,
            onColumn = HotelBookings.nutrition,
            otherColumn = HotelNutritions.id,
        ).join(
            otherTable = Places,
            joinType = JoinType.FULL,
            onColumn = Hotels.place,
            otherColumn = Places.id
        ).selectAll().where { HotelBookings.tourId eq tourId }.singleOrNull()?.let {
            with(HotelBookings) {
                TourBookingResponse(
                    id = it[id].value,
                    hotel = with(Hotels) {
                        Hotel(
                            id = it[id].value,
                            name = it[name],
                            level = it[level],
                            place = it.mapToPlace()
                        )
                    },
                    room = with(HotelRooms) {
                        HotelRoom(
                            id = it[id].value,
                            capacity = it[capacity],
                            type = it[type],
                            wifi = it[wifi],
                            costPerDay = it[costPerDay],
                        )
                    },
                    nutrition = with(HotelNutritions) {
                        HotelNutrition(
                            id = it[id].value,
                            type = it[type],
                            costPerDay = it[costPerDay],
                        )
                    },
                    startBookingDate = it[startBookingDate],
                    endBookingDate = it[endBookingDate],
                    costPerDay = 12.2
                )
            }
        }?.let {
            GetBookingDataByTourIdResult.Success(it)
        } ?: GetBookingDataByTourIdResult.NotFound

    fun getBookingDataByTicketId(ticket: Long): GetBookingDataByTicketId =
        RouteBookings.join(
            otherTable = TransportRoutes,
            joinType = JoinType.FULL,
            onColumn = RouteBookings.route,
            otherColumn = TransportRoutes.id,
        ).join(
            otherTable = TransportRoutes,
            joinType = JoinType.FULL,
            onColumn = RouteBookings.route,
            otherColumn = TransportRoutes.id,
        ).join(
            otherTable = Transports,
            joinType = JoinType.FULL,
            onColumn = TransportRoutes.transport,
            otherColumn = Transports.id,
        ).selectAll().where { RouteBookings.ticketId eq ticket }.map {
            with(RouteBookings) {
                TicketBookingResponse(
                    id = it[id].value,
                    ticketId = it[ticketId],
                    route = with(TransportRoutes) {
                        TransportRoute(
                            id = it[id].value,
                            name = it[name],
                            transport = with(Transports) {
                                Transport(
                                    type = it[type],
                                    name = it[name],
                                    contractor = with(TransportContractors) {
                                        Contractor(
                                            id = it[id].value,
                                            title = it[title],
                                            description = it[description]
                                        )
                                    }
                                )
                            },
                            departurePlace = Places.selectAll().where { id eq it[departurePlace] }.single()
                                .mapToPlace(),
                            arrivePlace = Places.selectAll().where { id eq it[arrivePlace] }.single()
                                .mapToPlace(),
                            cost = it[cost],
                            capacity = it[capacity]
                        )
                    },
                    personCount = it[personCount]
                )
            }
        }.let { GetBookingDataByTicketId.Success(it) }
}

fun ResultRow.mapToPlace(): Place = Place(
    id = this[Places.id].value,
    name = this[Places.name],
    address = this[Places.address],
)


sealed interface GetBookingDataByTicketId {
    data class Success(val data: List<TicketBookingResponse>) : GetBookingDataByTicketId
}

sealed interface GetBookingDataByTourIdResult {
    data class Success(val data: TourBookingResponse) : GetBookingDataByTourIdResult
    data object NotFound : GetBookingDataByTourIdResult
}

data class TourBookingResponse(
    val id: Long,
    val hotel: Hotel,
    val room: HotelRoom,
    val nutrition: HotelNutrition,
    val startBookingDate: LocalDateTime,
    val endBookingDate: LocalDateTime,
    val costPerDay: Double
)

data class Hotel(
    val id: Long,
    val name: String,
    val level: HotelLevel,
    val place: Place
)

data class HotelRoom(
    val id: Long,
    val capacity: RoomCapacity,
    val type: RoomType,
    val wifi: Boolean,
    val costPerDay: Double
)

data class HotelNutrition(
    val id: Long,
    val type: NutritionType,
    val costPerDay: Double
)

data class Place(
    val id: Long,
    val name: String,
    val address: String,
)

data class TicketBookingResponse(
    val id: Long,
    val ticketId: Long,
    val route: TransportRoute,
    val personCount: Int
)

data class TransportRoute(
    val id: Long,
    val name: String,
    val transport: Transport,
    val departurePlace: Place,
    val arrivePlace: Place,
    val cost: Double,
    val capacity: Int
)

data class Transport(
    val type: TransportType,
    val name: String,
    val contractor: Contractor,
)

data class Contractor(
    val id: Long,
    val title: String,
    val description: String,
)

enum class TransportType {
    AIRPLANE,
    BUS,
    TRAIN
}

object Transports : LongIdTable("transports") {
    val type = enumeration<TransportType>("type")
    val name = varchar("name", 255)
    val contractor = reference("contractor_id", TransportContractors)
    val capacity = integer("capacity")
}

object TransportRoutes : LongIdTable("routes") {
    val name = varchar("name", 255)
    val transport = reference("transport_id", Transports)
    val departurePlace = reference("departure_place_id", Places)
    val arrivePlace = reference("arrive_place_id", Places)
    val departureTime = datetime("departure_time")
    val arriveTime = datetime("arrive_time")
    val cost = double("cost")
    val capacity = integer("capacity")
}

object TransportContractors : LongIdTable("contractors") {
    val title = varchar("title", 255)
    val description = varchar("description", 255)
}

object Places : LongIdTable("places") {
    val name = varchar("name", 255)
    val address = varchar("address", 255)
}

enum class HotelLevel {
    ONE, TWO, THREE, FOUR, FIVE
}

object Hotels : LongIdTable("hotels") {
    val place = reference("place_id", Places)
    val name = varchar("name", 255)
    val level = enumeration<HotelLevel>("level")
}

enum class RoomCapacity {
    SINGLE,
    DOUBLE,
    TWIN,
    TRIPLE,
    QUADRIPLE,
    EXTRA_BED
}

enum class RoomType {
    STANDARD,
    SUPERIOR,
    STUDIO,
    FAMILY,
    DELUXE,
    LUXURY
}

object HotelRooms : LongIdTable("hotel_rooms") {
    val hotel = reference("hotel_id", Hotels)
    val capacity = enumeration<RoomCapacity>("capacity")
    val type = enumeration<RoomType>("type")
    val wifi = bool("wifi")
    val costPerDay = double("cost_per_day")
}

enum class NutritionType {
    RO, BB, HB, FB, AI, UAI, HB_PLUS, FB_PLUS
}

object HotelNutritions : LongIdTable("hotel_nutritions") {
    val hotel = reference("hotel_id", Hotels)
    val type = enumeration<NutritionType>("type")
    val costPerDay = double("cost_per_day")
}

object HotelBookings : LongIdTable("hotel_bookings") {
    val tourId = long("tour_id")
    val hotel = reference("hotel_id", Hotels)
    val room = reference("room_id", HotelRooms)
    val nutrition = reference("hotel_nutrition", HotelNutritions)
    val startBookingDate = datetime("start_booking_date")
    val endBookingDate = datetime("end_booking_date")
}

object RouteBookings : LongIdTable("route_bookings") {
    val ticketId = long("ticket_id")
    val route = reference("route_id", TransportRoutes)
    val personCount = integer("person_count")
}