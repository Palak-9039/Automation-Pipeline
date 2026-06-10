# Automation Outreach Pipeline

## Overview

An automated outreach pipeline built with Kotlin and Ktor that:

1. Discovers lookalike companies using Ocean.io
2. Finds executive contacts using Prospeo
3. Generates personalized outreach emails
4. Sends emails through Brevo
5. Includes a safety confirmation step before dispatch

## Tech Stack

- Kotlin
- Ktor Client
- Kotlin Serialization
- Ocean.io API
- Prospeo API
- Brevo API

## Features

- Lookalike company discovery
- Executive lead enrichment
- LinkedIn profile extraction
- Personalized email generation
- Automated email delivery
- End-to-end pipeline execution

## Environment Variables

```env
PROSPEO_API_KEY=YOUR_PROSPEO_API_KEY
OCEAN_API_TOKEN=YOUR_OCEAN_API_TOKEN
BREVO_API_KEY=YOUR_BREVO_API_KEY
SENDER_EMAIL=YOUR_SENDER_EMAIL
```

## Build

```bash
./gradlew shadowJar
```

## Run

```bash
java -jar build/libs/Automation-Pipeline-1.0-SNAPSHOT-all.jar stripe.com
```

## Pipeline Flow

```text
Target Domain
    ↓
Ocean.io Lookalike Discovery
    ↓
Prospeo Executive Search
    ↓
Lead Enrichment
    ↓
Safety Confirmation
    ↓
Brevo Outreach Delivery
```

## Note

API credentials are intentionally omitted from this repository and should be supplied through environment configuration.
