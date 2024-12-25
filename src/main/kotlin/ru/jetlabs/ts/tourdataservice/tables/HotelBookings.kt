package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object HotelBookings : LongIdTable("hotel_bookings") {
    val tourId = long("tour_id")
    val hotel = reference("hotel_id", Hotels)
    val room = reference("room_id", HotelRooms)
    val nutrition = reference("hotel_nutrition", HotelNutritions)
    val startBookingDate = datetime("start_booking_date")
    val endBookingDate = datetime("end_booking_date")
}