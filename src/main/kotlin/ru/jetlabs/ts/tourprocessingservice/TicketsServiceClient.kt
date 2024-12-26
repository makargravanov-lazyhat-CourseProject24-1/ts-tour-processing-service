package ru.jetlabs.ts.tourprocessingservice

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "ts-tickets-service", url = "http://ts-tickets-service:8080/ts-tickets-service/api/v1/tickets")
interface TicketsServiceClient {
    @PostMapping
    fun registerTicket(@RequestBody form: RegisterTicketForm): ResponseEntity<*>
}