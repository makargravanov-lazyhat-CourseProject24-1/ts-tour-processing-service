package ru.jetlabs.ts.tourdataservice.models

data class TicketBookingResponse(
    val id: Long,
    val ticketId: Long,
    val route: TransportRoute,
    val personCount: Int
)