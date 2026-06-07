package org.example.com.automation


import java.io.File
import java.util.Properties

object AppConfig {
    private val properties = Properties()

    init {
        val propertiesFile = File("local.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { properties.load(it) }
        } else {
            println("Warning: local.properties file not found at project root!")
        }
    }

    val oceanApiToken: String get() = properties.getProperty("OCEAN_API_TOKEN", "")
    val prospeoApiKey: String get() = properties.getProperty("PROSPEO_API_KEY", "")
    val eazyreachClientId: String get() = properties.getProperty("EAZYREACH_CLIENT_ID", "")
    val eazyreachClientSecret: String get() = properties.getProperty("EAZYREACH_CLIENT_SECRET", "")
    val brevoApiKey: String get() = properties.getProperty("BREVO_API_KEY", "")
    val senderEmail: String get() = properties.getProperty("SENDER_EMAIL", "contact@palakdevwebsite.website")
}