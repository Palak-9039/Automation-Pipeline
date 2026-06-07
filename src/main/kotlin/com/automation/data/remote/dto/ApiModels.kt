package org.example.com.automation.data.remote.dto

import kotlinx.serialization.Serializable

// Network Specific Data Transfer Objects (DTOs) marked for Ktor Serialization
@Serializable
data class OceanLookalikeRequest(val domain: String)

@Serializable
data class OceanLookalikeResponse(val lookalikes: List<String> = emptyList())

@Serializable
data class ProspeoSearchRequest(val domain: String)

@Serializable
data class ProspeoSearchResponse(val response: ProspeoData? = null)

@Serializable
data class ProspeoData(val email_list: List<ProspeoEmail> = emptyList())

@Serializable
data class ProspeoEmail(
    val email: String,
    val first_name: String? = null,
    val last_name: String? = null,
    val title: String? = null,
    val linkedin: String? = null
)