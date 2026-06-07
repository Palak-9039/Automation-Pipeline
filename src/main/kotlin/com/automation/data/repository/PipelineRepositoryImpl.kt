package org.example.com.automation.data.repository

import org.example.com.automation.AppConfig
import org.example.com.automation.data.NetworkClient
import org.example.com.automation.data.remote.dto.*
import org.example.com.automation.domain.model.ExecutiveLead
import org.example.com.automation.domain.model.LookalikeDomain
import org.example.com.automation.domain.repository.PipelineRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PipelineRepositoryImpl : PipelineRepository {

    // STAGE 1: Fetching Lookalike Domains via Ocean.io
    override suspend fun fetchLookalikeDomains(targetDomain: String): List<LookalikeDomain> {
        println("\n Stage 1: Searching for lookalike domains for $targetDomain...")

        // Fallback checkpoint if the API token isn't provided
        if (AppConfig.oceanApiToken.isEmpty()) {
            println("Ocean.io API Token missing. Utilizing mock discovery fallback.")
            return listOf(
                LookalikeDomain("${targetDomain.substringBefore(".")}-competitor.com", "Competitor Corp"),
                LookalikeDomain("alt-${targetDomain}", "Alternative Solutions")
            )
        }

        return try {
            val response = NetworkClient.httpClient.post("https://api.ocean.io/v1/lookalikes") {
                header("Authorization", "Bearer ${AppConfig.oceanApiToken}")
                contentType(ContentType.Application.Json)
                setBody(OceanLookalikeRequest(domain = targetDomain))
            }

            if (response.status.isSuccess()) {
                val data = response.body<OceanLookalikeResponse>()
                data.lookalikes.map { domain -> LookalikeDomain(domain, domain.substringBefore(".")) }
            } else {
                println("Ocean API returned error status: ${response.status}. Falling back to default list.")
                emptyList()
            }
        } catch (e: Exception) {
            println("Ocean API Connection Exception: ${e.message}. Falling back.")
            emptyList()
        }
    }

    // STAGE 2: Locate Executive Leads and Emails via Prospeo (Consolidated Step)
    override suspend fun discoverExecutiveLeads(domains: List<LookalikeDomain>): List<ExecutiveLead> {
        println("\n Stage 2: Finding executive profiles and contact emails via Prospeo...")
        val discoveredLeads = mutableListOf<ExecutiveLead>()

        for (target in domains) {
            println("Querying data for lookalike domain: ${target.domainName}")
            try {
                val response = NetworkClient.httpClient.post("https://api.prospeo.io/v1/enrichment/domain") {
                    header("X-KEY", AppConfig.prospeoApiKey)
                    contentType(ContentType.Application.Json)
                    setBody(ProspeoSearchRequest(domain = target.domainName))
                }

                if (response.status.isSuccess()) {
                    val searchResult = response.body<ProspeoSearchResponse>()
                    val emailList = searchResult.response?.email_list ?: emptyList()

                    if (emailList.isEmpty()) {
                        println("No leads discovered for ${target.domainName}")
                        continue
                    }

                    // Map network DTO responses directly to our clean domain models
                    for (prospeoEmail in emailList) {
                        val lead = ExecutiveLead(
                            firstName = prospeoEmail.first_name ?: "Unknown",
                            lastName = prospeoEmail.last_name ?: "User",
                            title = prospeoEmail.title ?: "Executive",
                            linkedinUrl = prospeoEmail.linkedin ?: "https://www.linkedin.com",
                            companyDomain = target.domainName,
                            email = prospeoEmail.email
                        )
                        discoveredLeads.add(lead)
                        println("Found Executive: ${lead.firstName} ${lead.lastName} (${lead.title}) -> ${lead.email}")
                    }
                } else {
                    println("Prospeo API error status: ${response.status}")
                }
            } catch (e: Exception) {
                println("Prospeo network exception: ${e.message}")
            }
        }
        return discoveredLeads
    }

    // STAGE 3: Build Custom Copy and Broadcast via Brevo
    override suspend fun sendOutreachEmails(contacts: List<ExecutiveLead>): Boolean {
        println("\nStage 3: Broadcasting automated outreach sequences via Brevo...")

        for (contact in contacts) {
            val personalBody = """
                Hello ${contact.firstName},
                
                I noticed your incredible work as ${contact.title} at ${contact.companyDomain.substringBefore(".")}. 
                We are building advanced AI-driven workflow orchestrations that seamlessly boost pipeline efficiencies.
                
                Would love to connect and share insights briefly.
                
                Best regards,
                Palak Richhariya
            """.trimIndent()

            try {
                val response = NetworkClient.httpClient.post("https://api.brevo.com/v3/smtp/email") {
                    header("api-key", AppConfig.brevoApiKey)
                    contentType(ContentType.Application.Json)
                    setBody(mapOf(
                        "sender" to mapOf("email" to AppConfig.senderEmail),
                        "to" to listOf(mapOf("email" to contact.email, "name" to "${contact.firstName} ${contact.lastName}")),
                        "subject" to "Scale Collaboration Opportunities for ${contact.companyDomain.substringBefore(".")}",
                        "htmlContent" to "<p>${personalBody.replace("\n", "<br>")}</p>"
                    ))
                }

                if (response.status.isSuccess()) {
                    println("Outreach email successfully transmitted to: ${contact.email}")
                } else {
                    println("Failed to broadcast to ${contact.email}. Status: ${response.status}")
                }
            } catch (e: Exception) {
                println("Brevo SMTP network engine exception: ${e.message}")
            }
        }
        return true
    }
}