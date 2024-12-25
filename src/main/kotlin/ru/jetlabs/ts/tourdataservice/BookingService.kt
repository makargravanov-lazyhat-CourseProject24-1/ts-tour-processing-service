package ru.jetlabs.ts.tourdataservice

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.jetlabs.ts.tourdataservice.models.*
import ru.jetlabs.ts.tourdataservice.tables.*

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