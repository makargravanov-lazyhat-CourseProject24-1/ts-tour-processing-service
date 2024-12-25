package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import ru.jetlabs.ts.tourdataservice.models.NutritionType

object HotelNutritions : LongIdTable("hotel_nutritions") {
    val hotel = reference("hotel_id", Hotels)
    val type = enumeration<NutritionType>("type")
    val costPerDay = double("cost_per_day")
}