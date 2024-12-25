package ru.jetlabs.ts.tourdataservice.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

data class Hotel(
    val id: Long,
    val name: String,
    val level: HotelLevel,
    val place: Place
)

fun ResultRow.mapToHotel(): Hotel = this.let {
    with(Hotels) {
        Hotel(
            id = it[id].value,
            name = it[name],
            level = it[level],
            place = Places.selectAll().where { id eq it[id].value }.single().mapToPlace()
        )
    }
}