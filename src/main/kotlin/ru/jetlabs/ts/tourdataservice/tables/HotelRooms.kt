package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import ru.jetlabs.ts.tourdataservice.models.RoomCapacity
import ru.jetlabs.ts.tourdataservice.models.RoomType

object HotelRooms : LongIdTable("hotel_rooms") {
    val hotel = reference("hotel_id", Hotels)
    val capacity = enumeration<RoomCapacity>("capacity")
    val type = enumeration<RoomType>("type")
    val wifi = bool("wifi")
    val costPerDay = double("cost_per_day")
}