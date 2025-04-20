# Code Challenge

## ✅ Challenge Requirements

| Feature | Status |
|--------|--------|
| App supports both Java and Kotlin | ✅ |
| Restaurant List screen loads on launch | ✅ |
| Tap on restaurant navigates to detail screen | ✅ |
| Back navigation returns to list | ✅ |
| Restaurant list fetched from API | ✅ |
| Search and filter by name/cuisine | ✅ |
| Sort by best deals | ✅ |
| Restaurant Details screen shows sorted deal cards | ✅ |
| Sort deals by highest discount | ✅ |
| Ignore buttons in detail screen | ✅ |

This project is a restaurant listing application built using **Kotlin**/**Java** and **Android Jetpack** components.
It follows a clean architecture approach with modularized code to ensure scalability, maintainability, and testability.

---

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Setup](#setup)

---

## Features
- Fetch and display restaurant list from remote API
- Real-time search by name and cuisine
- Deal sorting by best value (list) and highest discount (details)
- Offline and error handling
- Clean architecture with separation of concerns
- Modularized code (data, domain, presentation layers)
- Dependency injection with Dagger Hilt
- Unit tests for ViewModels and UseCases

---

## Architecture
The project follows **Clean Architecture** principles, ensuring separation of concerns and modularity. The architecture is divided into three main layers:

1. **Presentation Layer**:
   - Contains UI components such as `Fragment` and `ViewModel`.
   - Observes `LiveData` for state updates and handles user interactions.

2. **Domain Layer**:
   - Contains business logic and use cases.
   - Independent of frameworks and external libraries.

3. **Data Layer**:
   - Handles data sources (network).
   - Implements repository pattern to abstract data access.

---

## Tech Stack
- **Languages**: Kotlin & Java (multi-language modules)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Dagger Hilt
- **Networking**: Retrofit
- **Async**: Kotlin Coroutines
- **UI**: Jetpack Navigation, RecyclerView, Material Components
- **Testing**: JUnit, MockK
- **Image Loading**: Glide
- **Build Tools**: Android Studio
- **Version Control**: Git
- **Code Quality**: Ktlint

---

## Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/Sami00777/Code-Challenge-Eat-Club.git
