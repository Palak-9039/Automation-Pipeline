package org.example.com.automation.domain.model


import kotlinx.serialization.Serializable

// Core models
data class CompanyTarget(val domain: String)

data class LookalikeDomain(val domainName: String, val companyName: String)

data class ExecutiveLead(
    val firstName: String,
    val lastName: String,
    val title: String,
    val linkedinUrl: String,
    val companyDomain: String,
    val email: String
)

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