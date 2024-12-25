package ru.jetlabs.ts.tourdataservice

import ru.jetlabs.ts.tourdataservice.models.TourBookingResponse

sealed interface GetBookingDataByTourIdResult {
    data class Success(val data: TourBookingResponse) : GetBookingDataByTourIdResult
    data object NotFound : GetBookingDataByTourIdResult
}