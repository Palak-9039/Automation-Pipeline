package org.example.com.automation.data

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkClient {
    // A single, shared HTTP client instance for the entire pipeline
    val httpClient = HttpClient(CIO) {
        // Automatically handles converting JSON API responses into Kotlin Data Classes
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Prevents crashes if an API adds new fields later
            })
        }
    }

    // Ensures the HTTP client closes properly when the application shuts down
    fun close() {
        httpClient.close()
    }
}