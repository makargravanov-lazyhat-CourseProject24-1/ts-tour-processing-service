package ru.jetlabs.ts.tourdataservice.models

data class Transport(
    val type: TransportType,
    val name: String,
    val contractor: Contractor,
)