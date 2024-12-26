package ru.jetlabs.ts.tourprocessingservice

import java.time.LocalDate

data class Tour(
    val id: Long,
    val agencyId: Long,
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
    agencyId = agencyId,
    name = name,
    description = description,
    hotelId = hotelId,
    roomId = roomId,
    nutritionId = nutritionId,
    startDate = startDate,
    endDate = endDate,
    personCount = personCount,
)