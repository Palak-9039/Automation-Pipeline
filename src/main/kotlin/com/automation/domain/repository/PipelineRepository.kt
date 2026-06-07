package org.example.com.automation.domain.repository

import org.example.com.automation.domain.model.ExecutiveLead
import org.example.com.automation.domain.model.LookalikeDomain

interface PipelineRepository {

    // Stage 1: Fetching lookalike domain list using Ocean.io
    suspend fun fetchLookalikeDomains(targetDomain: String): List<LookalikeDomain>

    // Stage 2: Locating corporate executive profiles and contact emails using Prospeo
    suspend fun discoverExecutiveLeads(domains: List<LookalikeDomain>): List<ExecutiveLead>

    // Stage 3: Package personalized copy and broadcast emails safely via Brevo
    suspend fun sendOutreachEmails(contacts: List<ExecutiveLead>): Boolean
}