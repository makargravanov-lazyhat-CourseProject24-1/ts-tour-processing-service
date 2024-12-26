package ru.jetlabs.ts.tourprocessingservice

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object Tours : LongIdTable("tours") {
    val agencyId = long("agencyId")
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val hotelId = long("hotel_id")
    val roomId = long("room_id")
    val nutritionId = long("nutrition_id")
    val startDate = date("start_date")
    val endDate = date("end_date")
    val personCount = integer("person_count")
}