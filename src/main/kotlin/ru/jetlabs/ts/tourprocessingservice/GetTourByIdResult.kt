package ru.jetlabs.ts.tourprocessingservice

sealed interface GetTourByIdResult {
    data class Success(val tour: Tour) : GetTourByIdResult
    data object NotFound : GetTourByIdResult
}