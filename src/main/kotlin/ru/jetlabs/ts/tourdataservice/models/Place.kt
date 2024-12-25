package ru.jetlabs.ts.tourdataservice.models

import org.jetbrains.exposed.sql.ResultRow

data class Place(
    val id: Long,
    val name: String,
    val address: String,
)

fun ResultRow.mapToPlace(): Place = this.let {
    with(Places) {
        Place(
            id = it[id].value,
            name = it[name],
            address = it[address],
        )
    }
}