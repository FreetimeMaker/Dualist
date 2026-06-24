# Dualist 🌓

**Dualist** is a modern, high-performance, and privacy-focused To-Do List application for Android. It is designed with an **adaptive interface** that provides a seamless experience across phones, tablets, and foldables.

Built with **Jetpack Compose** and **Material 3**, Dualist follows a strict **Offline-First** philosophy and is completely free of Google Play Services (GMS).

## ✨ Features

- **Adaptive 2-Pane Layout**: Automatically switches between a single-column list on phones and a List-Detail view on tablets and foldables.
- **Search & Filter**: Quickly find tasks with real-time search.
- **Swipe-to-Delete**: Intuitive gestures to manage your task list.
- **Categories & Tags**: Organize your tasks into categories for better focus.
- **Offline-First**: All data is stored locally in a Room database. No internet connection required.
- **Import/Export**: Easily backup and restore your tasks locally.
- **GMS-Free**: No dependencies on Google Play Services or Firebase. Ideal for de-googled Android distributions.
- **Material 3 Design**: Vibrant, energetic color scheme with full support for Light/Dark mode and Dynamic Color.
- **Edge-to-Edge**: Modern immersive UI that utilizes the full screen.
- **Performance**: Reactive UI updates powered by Kotlin Coroutines and Flow.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/compose)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Navigation**: [Jetpack Navigation 3](https://developer.android.com/jetpack/androidx/releases/navigation)
- **Adaptive Layouts**: [Compose Material 3 Adaptive](https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive)
- **Asynchrony**: Kotlin Coroutines & Flow

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug (or newer)
- Android SDK 37 (Target)
- Minimum Android version: 7.0 (API 24)

### Building the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/FreetimeMaker/Dualist.git
   ```
2. Open the project in Android Studio.
3. Build the project using the Gradle wrapper:
   ```bash
   ./gradlew assembleDebug
   ```

## 📦 Production Readiness

This project is configured for production:
- **Accessibility**: Support for screen readers (TalkBack).
- **Security**: R8/ProGuard minification enabled for release builds.
- **Testing**: Includes a foundation for unit and ViewModel tests.

## 🤝 Donations

If you like Dualist, I'd appreciate a small donation — thank you! Below are some common cryptocurrency options.

<div align="center">

[![Bitcoin](https://img.shields.io/badge/Bitcoin-BTC-FF9900?style=for-the-badge&logo=bitcoin&logoColor=white)](https://blockchair.com/bitcoin/address/1DsCAVrzvGokrzXpe6YR33QuTo5EppiKRE)
[![Litecoin](https://img.shields.io/badge/Litecoin-LTC-345D9D?style=for-the-badge&logo=litecoin&logoColor=white)](https://blockchair.com/litecoin/address/LU2ERRXKTeKnzpuieQcpsBteViEY7ff5Wg)

</div>

Alternatively, you can also display the addresses directly:

- Bitcoin (BTC): `1DsCAVrzvGokrzXpe6YR33QuTo5EppiKRE` — or open in block explorer by clicking the badge above
- Litecoin (LTC): `LU2ERRXKTeKnzpuieQcpsBteViEY7ff5Wg` — or open in block explorer by clicking the badge above

## 🛡 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

*Dualist - Simple, Adaptive, Private.*
