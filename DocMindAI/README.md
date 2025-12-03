# DocMind AI - Intelligent Document Assistant

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose)
![AI](https://img.shields.io/badge/AI-Google_Gemini-8E75B2?style=for-the-badge&logo=googlebard)
![Build](https://img.shields.io/github/actions/workflow/status/Nitant06/Android-Projects/android_build.yml?style=for-the-badge)

**DocMind AI** is a modern Android application that transforms your smartphone into an intelligent document scanner. It combines **Computer Vision (OCR)** with **Generative AI (Gemini)** to not only read documents but understand, categorize, and summarize them automatically.

Built with **Strict Clean Architecture**, **Kotlin Multiplatform (KMP)** structure, and **Industry Standard** best practices.

---

## Screenshots

| Home Screen | App Demo |
|:---:|:---:|
<img src="Screenshots%20and%20video/home_screen.jpg" width="200"/> | ![**DocMind App Demo**](Screenshots%20and%20video/DocMind_App_Demo_GIF.gif)

---

## Key Features

*   **Smart Scanning:** Custom Camera implementation using **CameraX**.
*   **On-Device OCR:** Instantly extracts text from images using **Google ML Kit** (Offline capable).
*   **AI Intelligence:** Uses **Google Gemini 2.0 Flash** to analyze text, auto-detect document type (Invoice, Receipt, Note), and generate concise summaries.
*   **Organized Storage:** Persists data locally using **Room Database**.
*   **Modern UI:** Fully built with **Jetpack Compose** and Material 3 Design.
*   **Resilience:** robust parsing logic that handles unstructured AI responses gracefully.
*   **CI/CD Pipeline:** Automated testing and APK generation using **GitHub Actions**.

---

## Tech Stack & Architecture

The app follows **Strict Clean Architecture** with **MVVM** pattern, ensuring separation of concerns and testability.

*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose (Material 3)
*   **Architecture:** Clean Architecture (Data, Domain, Presentation layers)
*   **Dependency Injection:** Koin
*   **Networking:** Ktor Client
*   **Local Database:** Room (SQLite)
*   **Asynchronous:** Coroutines & Flow
*   **AI/ML:**
    *   Google ML Kit (Text Recognition)
    *   Google Gemini API (Generative Language)
*   **Navigation:** Type-Safe Navigation Compose
*   **Build System:** Gradle (Kotlin DSL) + Version Catalog (`libs.versions.toml`)

### Architecture Overview

```mermaid
graph TD
    UI["Presentation Layer<br>(Screens & ViewModels)"] -->|Observes| Domain["Domain Layer<br>(Use Cases & Models)"]
    Domain -->|Defines Contract| Repo["Repository Interface"]
    Data["Data Layer<br>(Repository Impl)"] -->|Implements| Repo
    Data -->|Uses| DB[("Room Database")]
    Data -->|Uses| API["Gemini AI Service"]
    Data -->|Uses| OCR["ML Kit OCR"]


