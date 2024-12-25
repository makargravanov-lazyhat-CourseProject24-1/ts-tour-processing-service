package ru.jetlabs.ts.tourdataservice.models

data class TransportRoute(
    val id: Long,
    val name: String,
    val transport: Transport,
    val departurePlace: Place,
    val arrivePlace: Place,
    val cost: Double,
    val capacity: Int
)