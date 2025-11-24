# ChitChatApp

ChitChat is a modern, real-time messaging application for Android, built using the latest technologies. It provides a seamless and interactive chatting experience, complete with user authentication, profile management, and AI-powered smart replies.

## Features

- **User Authentication**: Secure sign-up and login functionality using Firebase Authentication.
- **Real-Time Messaging**: Instantaneous one-on-one messaging powered by Firebase Firestore's real-time database.
- **Conversation Management**: A clean and intuitive list of all your conversations, sorted by the most recent activity.
- **Profile Customization**: Users can view and edit their profiles, including their display name and profile picture.
- **Smart Replies**: AI-powered suggestions for quick replies, integrated using ML Kit, to make conversations faster and more efficient.
- **Start New Chats**: Easily find and start conversations with other registered users.
- **Push Notifications**: Receive timely notifications for new messages via Firebase Cloud Messaging.
- **Swipe to Delete**: Effortlessly manage your conversation list by swiping to delete chats.
- **View User Profiles**: Tap on a user's profile in the chat screen to view their details.

## Tech Stack & Architecture

This project follows the principles of **Clean Architecture** and employs the **MVVM (Model-View-ViewModel)** pattern. The codebase is organized into a multi-module structure to promote separation of concerns, scalability, and maintainability.

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative and modern UI.
- **Architecture**:
  - Clean Architecture (UI, Domain, Data layers)
  - MVVM
  - Multi-module design (`app`, `core`, `feature_auth`, `feature_chat`, etc.)
- **Backend & Services**:
  - [Firebase Authentication](https://firebase.google.com/docs/auth) for user management.
  - [Firebase Firestore](https://firebase.google.com/docs/firestore) for real-time data storage (messages, conversations, users).
  - [Firebase Storage](https://firebase.google.com/docs/storage) for hosting profile pictures.
  - [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) for push notifications.
- **Machine Learning**: [ML Kit Smart Reply](https://developers.google.com/ml-kit/natural-language/smart-reply) for contextual message suggestions.
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for managing dependencies.
- **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) to handle screen transitions.
- **Asynchronous Operations**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html) for managing background threads and data streams.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for efficient image loading from URLs.
- **Testing**:
  - JUnit, Mockito, and MockK for unit testing.
  - Hilt Android Testing and Compose UI Tests for instrumentation tests.

## Project Structure

The project is divided into several modules to ensure a clean separation of concerns:

- `:app`: The main application module that integrates all other modules and handles the primary navigation graph.
- `:core`: A foundational module containing shared code, including domain models, repository interfaces, UI themes, and base Hilt modules for Firebase.
- `:feature_auth`: Manages all user authentication logic, including login and sign-up screens.
- `:feature_conversations`: Responsible for displaying the list of user conversations and initiating new chats.
- `:feature_chat`: Contains the core chat screen, message display, input handling, and smart reply integration.
- `:feature_profile`: Handles viewing and editing user profiles.

## Setup and Installation

To build and run the project locally, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/nitant06/Android-Projects.git
    cd Android-Projects/ChitChatApp
    ```

2.  **Firebase Setup:**
    - Create a new project on the [Firebase Console](https://console.firebase.google.com/).
    - Add an Android app to your Firebase project with the package name `com.example.chitchat`.
    - Download the `google-services.json` file.
    - Place the downloaded `google-services.json` file in the `app/` and `feature_auth/` directories.
    - In the Firebase Console, enable the following services:
        - **Authentication**: Enable the "Email/Password" sign-in method.
        - **Firestore Database**: Create a new database.
        - **Storage**: Create a new storage bucket.

3.  **Open in Android Studio:**
    - Open Android Studio and select "Open" or "Open an Existing Project".
    - Navigate to the cloned `ChitChatApp` directory and open it.

4.  **Build and Run:**
    - Let Android Studio sync the Gradle files.
    - Build and run the application on an Android emulator or a physical device.
