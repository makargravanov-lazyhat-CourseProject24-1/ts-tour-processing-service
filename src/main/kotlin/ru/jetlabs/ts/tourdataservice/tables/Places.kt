package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object Places : LongIdTable("places") {
    val name = varchar("name", 255)
    val address = varchar("address", 255)
}