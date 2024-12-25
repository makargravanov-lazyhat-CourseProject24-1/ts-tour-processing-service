package ru.jetlabs.ts.tourdataservice

import org.jetbrains.exposed.sql.selectAll
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.jetlabs.ts.tourdataservice.models.Hotel
import ru.jetlabs.ts.tourdataservice.models.mapToHotel
import ru.jetlabs.ts.tourdataservice.tables.Hotels

@SpringBootApplication
class TourDataServiceApplication

@RestController
@RequestMapping("/ts-tour-data-service/api/v1/hotels")
class HotelsController {
    @GetMapping
    fun getAllHotels(): ResponseEntity<*> = getAllHotels().let { ResponseEntity.status(HttpStatus.OK).body(it) }
}

@Component
@Transactional
class HotelsService {
    fun getAllHotels(): List<Hotel> = Hotels.selectAll().map { it.mapToHotel() }
}