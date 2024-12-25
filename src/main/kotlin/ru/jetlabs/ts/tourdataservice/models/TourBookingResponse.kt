package ru.jetlabs.ts.tourdataservice.models

import java.time.LocalDateTime

data class TourBookingResponse(
    val id: Long,
    val hotel: Hotel,
    val room: HotelRoom,
    val nutrition: HotelNutrition,
    val startBookingDate: LocalDateTime,
    val endBookingDate: LocalDateTime,
    val costPerDay: Double
)