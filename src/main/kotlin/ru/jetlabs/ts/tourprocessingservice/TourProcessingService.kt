package ru.jetlabs.ts.tourprocessingservice

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLException

@Component
@Transactional
class TourProcessingService(
    private val ticketsServiceClient: TicketsServiceClient
) {
    fun getAllTours(): List<Tour> = TourDao.all().map { it.mapToTour() }

    fun getTourById(id: Long): GetTourByIdResult =
        TourDao.findById(id)?.mapToTour()?.let { GetTourByIdResult.Success(it) } ?: GetTourByIdResult.NotFound

    fun reserveTicket(id: Long, form: ReserveTicketForm): RegisterTicketResult {
        try {
            val tour = TourDao.findById(id)?.mapToTour() ?: return RegisterTicketResult.Error.TourNotFound(id)

            val response = ticketsServiceClient.registerTicket(
                RegisterTicketForm(
                    tour = tour,
                    userId = form.userId,
                )
            )

            return if (response.statusCode.is2xxSuccessful)
                RegisterTicketResult.Success
            else
                RegisterTicketResult.Error.BadRequest(response.body.toString())
        } catch (e: SQLException) {
            return RegisterTicketResult.Error.UnknownError(e.stackTraceToString())
        }
    }
}

sealed interface RegisterTicketResult {
    data object Success : RegisterTicketResult
    sealed interface Error : RegisterTicketResult {
        val message: String

        data class TourNotFound(val id: Long) : Error {
            override val message: String = "tour with id = $id not found"
        }

        data class BadRequest(override val message: String) : Error

        data class UnknownError(override val message: String) : Error
    }
}