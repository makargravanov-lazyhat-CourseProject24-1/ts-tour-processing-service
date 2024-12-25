package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object TransportRoutes : LongIdTable("routes") {
    val name = varchar("name", 255)
    val transport = reference("transport_id", Transports)
    val departurePlace = reference("departure_place_id", Places)
    val arrivePlace = reference("arrive_place_id", Places)
    val departureTime = datetime("departure_time")
    val arriveTime = datetime("arrive_time")
    val cost = double("cost")
    val capacity = integer("capacity")
}