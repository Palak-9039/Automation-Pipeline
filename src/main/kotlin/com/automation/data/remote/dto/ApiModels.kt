package org.example.com.automation.data.remote.dto

import kotlinx.serialization.Serializable

// Network Specific Data Transfer Objects (DTOs) marked for Ktor Serialization
@Serializable
data class OceanLookalikeRequest(
    val size: Int = 10,
    val companiesFilters: OceanFilters
)
@Serializable
data class OceanFilters(
    val lookalikeDomains: List<String>
)

@Serializable
data class OceanCompany(
    val domain: String? = null,
    val name: String? = null
)

@Serializable
data class OceanLookalikeResponse(
    val data: List<OceanCompany> = emptyList()
)

@Serializable
data class SearchPersonRequest(
    val page: Int = 1,
    val filters: Filters
)

@Serializable
data class Filters(
    val company: CompanyFilter,
)

@Serializable
data class CompanyFilter(
    val websites: WebsiteFilter
)

@Serializable
data class WebsiteFilter(
    val include: List<String>
)

@Serializable
data class SearchPersonResponse(
    val error: Boolean = false,
    val results: List<PersonResult> = emptyList()
)

@Serializable
data class PersonResult(
    val person: Person
)

@Serializable
data class Person(
    val first_name: String? = null,
    val last_name: String? = null,
    val current_job_title: String? = null,
    val linkedin_url: String? = null,
    val email: EmailInfo? = null
)


@Serializable
data class EmailInfo(
    val email: String? = null
)


@Serializable
data class BrevoEmailRequest(
    val sender: BrevoSender,
    val to: List<BrevoRecipient>,
    val subject: String,
    val htmlContent: String
)

@Serializable
data class BrevoSender(
    val email: String
)

@Serializable
data class BrevoRecipient(
    val email: String,
    val name: String? = null
)