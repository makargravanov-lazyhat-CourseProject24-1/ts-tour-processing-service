package ru.jetlabs.ts.tourdataservice.models

data class HotelNutrition(
    val id: Long,
    val type: NutritionType,
    val costPerDay: Double
)