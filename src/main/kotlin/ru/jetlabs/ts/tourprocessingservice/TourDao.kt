package ru.jetlabs.ts.tourprocessingservice

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TourDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TourDao>(Tours)

    var agencyId by Tours.agencyId
    var name by Tours.name
    var description by Tours.description
    var hotelId by Tours.hotelId
    var roomId by Tours.roomId
    var nutritionId by Tours.nutritionId
    var startDate by Tours.startDate
    var endDate by Tours.endDate
    var personCount by Tours.personCount
}