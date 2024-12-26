package ru.jetlabs.ts.tourprocessingservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    fun reserveTicket(@PathVariable id: Long, @RequestBody body: ReserveTicketForm): ResponseEntity<*> =
        tourProcessingService.reserveTicket(id, body).let {
            when (it) {
                RegisterTicketResult.Success -> ResponseEntity.status(HttpStatus.OK).build<Unit>()
                is RegisterTicketResult.Error -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it)
            }
        }
}