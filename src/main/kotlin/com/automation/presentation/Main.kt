package org.example.com.automation.presentation


import org.example.com.automation.data.NetworkClient
import org.example.com.automation.data.repository.PipelineRepositoryImpl
import org.example.com.automation.domain.repository.PipelineRepository
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    // 1. Capture the target domain argument from user input, or fallback to a default
    val targetDomain = if (args.isNotEmpty()) {
        args[0]
    } else {
        println("No target domain provided via command line. Defaulting to 'apple.com' for evaluation.")
        "apple.com"
    }

    println("====================================================")
    println("Starting Automated Outreach Pipeline for: $targetDomain")
    println("====================================================")

    // 2. Instantiating our concrete clean architecture repository implementation
    val pipelineRepository: PipelineRepository = PipelineRepositoryImpl()

    // 3. Wrapping our async suspending repository actions inside a blocking coroutine scope
    runBlocking {
        try {
            //STAGE 1: Discovering Lookalike Competitor Domains
            val lookalikes = pipelineRepository.fetchLookalikeDomains(targetDomain)
            if (lookalikes.isEmpty()) {
                println("Pipeline halted: No lookalike domains discovered.")
                return@runBlocking
            }
            println("Stage 1 Complete: Found ${lookalikes.size} lookalike domains.")

            // STAGE 2: Discovering Executive Profiles & Consolidated Emails
            val executiveLeads = pipelineRepository.discoverExecutiveLeads(lookalikes)
            if (executiveLeads.isEmpty()) {
                println("Pipeline halted: No executive contact entries located.")
                return@runBlocking
            }
            println("Stage 2 Complete: Successfully enriched data for ${executiveLeads.size} contacts.")

            //STAGE 3: Mandatory Submission Safety Verification Checkpoint
            println("\n====================================================")
            println("SAFETY CHECKPOINT: VERIFICATION REQUIRED")
            println("====================================================")
            print("Ready to broadcast personalized email sequences via Brevo? Type 'Y' to confirm execution, or any other key to abort: ")

            // Reading keyboard input from the terminal window
            val userConfirmation = readLine()?.trim() ?: ""

            if (userConfirmation.equals("Y", ignoreCase = true)) {
                // User authorized execution -> Proceed to broadcast outreach sequences
                pipelineRepository.sendOutreachEmails(executiveLeads)
                println("\nPipeline Executed Successfully! All outreach communications dispatched.")
            } else {
                println("\nBroadcast Aborted by User. No outreach emails were transmitted.")
            }

        } catch (e: Exception) {
            println("\nCritical Failure inside Execution Pipeline: ${e.message}")
        } finally {
            //Cleanly shut down our shared Ktor asynchronous HTTP engine connection
            NetworkClient.close()
            println("Network client engines powered down cleanly. Goodbye!")
        }
    }
}