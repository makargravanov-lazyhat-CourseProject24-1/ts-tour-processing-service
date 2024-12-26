package ru.jetlabs.ts.tourprocessingservice

data class RegisterTicketForm(
    val tour: Tour,
    val userId: Long
)