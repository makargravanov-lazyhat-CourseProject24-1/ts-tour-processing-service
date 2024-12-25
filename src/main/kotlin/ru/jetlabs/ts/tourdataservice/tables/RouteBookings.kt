package ru.jetlabs.ts.tourdataservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object RouteBookings : LongIdTable("route_bookings") {
    val ticketId = long("ticket_id")
    val route = reference("route_id", TransportRoutes)
    val personCount = integer("person_count")
}