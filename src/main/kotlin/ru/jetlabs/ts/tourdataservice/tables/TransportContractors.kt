package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object TransportContractors : LongIdTable("contractors") {
    val title = varchar("title", 255)
    val description = varchar("description", 255)
}