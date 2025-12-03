# ChitChat App - A Modern Android Messaging Application

ChitChat is a full-stack, real-time messaging application for Android, built entirely with modern, native technologies. It showcases a robust, multi-module Clean Architecture and integrates a suite of Firebase services to provide a complete, end-to-end user experience, including an on-device AI feature for smart replies.

---

## 📸 Screenshots

| Login | Sign Up | Conversations (Active) |
| :---: | :---: | :---: |
| ![Login Screen](login.png) | ![Sign Up Screen](SignUp.png) | ![Conversations Screen](conversation_screen.png) |

| New Chat | Chat Screen | Profile (Edit) | Other User Profile (View) |
| :---: | :---: | :---: | :---: |
| ![New Chat Screen](new_chat_screen.png) | ![Chat Screen](chat_screen.png) | ![Profile Screen](profile_screen.png) | ![Other User Profile Screen](other_user_profile_screen.png) |

---

## ✨ Features

- **Full Authentication Flow:** Secure user sign-up, login, and session persistence.
- **Real-time One-on-One Messaging:** Send and receive messages instantly with a real-time listener.
- **Rich Conversation List:** Displays active chats sorted by the most recent message, including the last message text and timestamp.
- **User Discovery:** A dedicated screen to find and start conversations with other registered users.
- **User Profiles & Image Uploads:** Users can set their display name and upload a profile picture, which is stored in Firebase Cloud Storage.
- **On-Device AI Smart Replies:** The app suggests contextual, one-tap replies to incoming messages using Google's ML Kit.
- **Push Notifications:** Receive notifications for new messages even when the app is in the background or closed, powered by Firebase Cloud Messaging and Cloud Functions.
- **Swipe-to-Delete:** Easily delete conversations with an intuitive swipe gesture.
- **Modern, Branded UI:** A clean, consistent, and attractive user interface built with Material Design 3.

---

## 🛠️ Tech Stack & Architecture

This project was built with a focus on modern, scalable, and maintainable practices.

### **Architecture**
- **Clean Architecture:** Separates the app into three distinct layers (Presentation, Domain, Data) for clear separation of concerns.
- **MVVM (Model-View-ViewModel):** The core pattern within the Presentation layer.
- **Modularization:** The project is split into `:app`, `:core`, and feature modules (`:feature_auth`, `:feature_chat`, `:feature_conversations`, `:feature_profile`) to simulate a professional team environment and ensure feature independence.
- **Repository Pattern:** Acts as a single source of truth for all data.

### **UI**
- **Jetpack Compose:** 100% of the UI is built declaratively.
- **Material Design 3:** For theming, components, and a modern aesthetic.
- **Coil:** For efficient, asynchronous image loading.

### **Backend & Data**
- **Firebase Authentication:** For user management.
- **Cloud Firestore:** As the real-time, NoSQL database.
- **Firebase Cloud Storage:** For hosting user-uploaded profile pictures.
- **Firebase Cloud Messaging (FCM):** For push notifications.
- **Cloud Functions for Firebase:** Server-side TypeScript logic to trigger notifications.

### **Asynchronous Programming**
- **Kotlin Coroutines & Flows:** Used for all background tasks and for observing real-time data streams from Firestore.

### **Dependency Injection**
- **Hilt:** For managing dependencies throughout the entire application.

### **Testing**
- **Unit Tests (JUnit & MockK):** Written for ViewModels to test business logic in isolation.
- **UI / Instrumented Tests (Compose Test Rule & Hilt):** Written to verify user flows and UI state changes.

---

## 🚀 Setup and Installation

To run this project, you will need to set up your own Firebase project.

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/YourUsername/YourRepoName.git
    ```

2.  **Firebase Setup:**
    - Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
    - **Register the App:** Add a new Android app to the project with the package name `com.example.chitchat` (or your chosen package name).
    - **Download `google-services.json`:** Download the configuration file and place it in the `app/` directory of the project.
    - **Enable Services:** In the Firebase Console, enable the following services:
        - **Authentication:** Enable the "Email/Password" sign-in method.
        - **Firestore Database:** Create a new database and start in "test mode".
        - **Storage:** Create a new storage bucket and start in "test mode".

3.  **Create Firestore Index:**
    - In the Firestore Database section, go to the **Indexes** tab.
    - Create a new **Composite Index** with the following configuration:
        - **Collection ID:** `conversations`
        - **Fields to index:**
            1. `participants` -> `Array-contains`
            2. `lastMessageTimestamp` -> `Descending`
    - Wait for the index to be fully built and enabled.

4.  **Deploy Cloud Function:**
    - Navigate to the `ChitChatFunctions` directory (or wherever you saved the function code).
    - Run the following commands in your terminal:
      ```bash
      npm install
      firebase deploy --only functions
      ```

5.  **Build and Run:** Open the project in Android Studio, let Gradle sync, and run the app on an emulator or a physical device.

## License

This project is licensed under the MIT License.  
See the LICENSE file for more details.
