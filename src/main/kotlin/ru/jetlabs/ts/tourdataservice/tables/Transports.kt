package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import ru.jetlabs.ts.tourdataservice.models.TransportType

object Transports : LongIdTable("transports") {
    val type = enumeration<TransportType>("type")
    val name = varchar("name", 255)
    val contractor = reference("contractor_id", TransportContractors)
    val capacity = integer("capacity")
}