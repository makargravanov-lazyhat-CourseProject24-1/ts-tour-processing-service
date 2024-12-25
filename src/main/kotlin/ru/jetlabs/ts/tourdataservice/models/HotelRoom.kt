package ru.jetlabs.ts.tourdataservice.models

data class HotelRoom(
    val id: Long,
    val capacity: RoomCapacity,
    val type: RoomType,
    val wifi: Boolean,
    val costPerDay: Double
)