package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import ru.jetlabs.ts.tourdataservice.models.HotelLevel

object Hotels : LongIdTable("hotels") {
    val place = reference("place_id", Places)
    val name = varchar("name", 255)
    val level = enumeration<HotelLevel>("level")
}