package ru.jetlabs.ts.tourprocessingservice

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.date
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sun.security.krb5.internal.Ticket
import java.time.LocalDate

@SpringBootApplication
class TourProcessingServiceApplication

fun main(args: Array<String>) {
    runApplication<TourProcessingServiceApplication>(*args)
}

@Component
@Transactional
class SchemaInitialize : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        SchemaUtils.create(
            Tours
        )
    }
}

@RestController
@RequestMapping("/ts-tour-processing-service/api/v1/tour")
class TourProcessingServiceController(
    val tourProcessingService: TourProcessingService
) {
    @GetMapping
    fun getAllTours(): ResponseEntity<*> =
        tourProcessingService.getAllTours().let { ResponseEntity.status(HttpStatus.OK).body(it) }

    @GetMapping("/{id}")
    fun getTourById(@PathVariable id: Long): ResponseEntity<*> =
        tourProcessingService.getTourById(id).let {
            when (it) {
                GetTourByIdResult.NotFound -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it)
                is GetTourByIdResult.Success -> ResponseEntity.status(HttpStatus.OK).body(it.tour)
            }
        }

    @GetMapping("/{id}/ticket")
    fun registerTicket(@PathVariable id: Long): ResponseEntity<*> {

    }
}

@Component
@Transactional
class TourProcessingService {
    fun getAllTours(): List<Tour> = TourDao.all().map { it.mapToTour() }

    fun getTourById(tourId: Long): GetTourByIdResult =
        TourDao.findById(tourId)?.mapToTour()?.let { GetTourByIdResult.Success(it) } ?: GetTourByIdResult.NotFound

    fun registerTicket(tour: Tour): ResponseEntity<*> {

    }
}

sealed interface GetTourByIdResult {
    data class Success(val tour: Tour) : GetTourByIdResult
    data object NotFound : GetTourByIdResult
}

object Tours : LongIdTable("tours") {
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val hotelId = long("hotel_id")
    val roomId = long("room_id")
    val nutritionId = long("nutrition_id")
    val startDate = date("start_date")
    val endDate = date("end_date")
    val personCount = integer("person_count")
}

class TourDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TourDao>(Tours)

    var name by Tours.name
    var description by Tours.description
    var hotelId by Tours.hotelId
    var roomId by Tours.roomId
    var nutritionId by Tours.nutritionId
    var startDate by Tours.startDate
    var endDate by Tours.endDate
    var personCount by Tours.personCount
}

data class Tour(
    val id: Long,
    val name: String,
    val description: String,
    val hotelId: Long,
    val roomId: Long,
    val nutritionId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val personCount: Int,
)

fun TourDao.mapToTour(): Tour = Tour(
    id = id.value,
    name = name,
    description = description,
    hotelId = hotelId,
    roomId = roomId,
    nutritionId = nutritionId,
    startDate = startDate,
    endDate = endDate,
    personCount = personCount,
)