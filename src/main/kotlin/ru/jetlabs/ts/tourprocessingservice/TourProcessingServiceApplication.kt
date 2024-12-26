package ru.jetlabs.ts.tourprocessingservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class TourProcessingServiceApplication

fun main(args: Array<String>) {
    runApplication<TourProcessingServiceApplication>(*args)
}