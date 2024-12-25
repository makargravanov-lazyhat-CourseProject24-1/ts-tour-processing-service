package ru.jetlabs.ts.tourdataservice

import ru.jetlabs.ts.tourdataservice.models.TicketBookingResponse

sealed interface GetBookingDataByTicketId {
    data class Success(val data: List<TicketBookingResponse>) : GetBookingDataByTicketId
}