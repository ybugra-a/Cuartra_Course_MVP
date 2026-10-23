# Personal Calendar & Smart Reminder (MVP)

A lightweight, visually appealing Android application designed to track daily schedules and task completion. Developed as an MVP (Minimum Viable Product) for a Mobile Software course, this project aims to break the monotonous design of standard calendar apps by introducing a modular, dark-themed, and user-friendly experience.

## Current Features (V1.0)
* **Modern Dark UI:** A sleek, eye-friendly `#121212` dark theme with rounded `#1E1E2E` task cards using `CardView`.
* **Scrollable Dashboard:** Tasks are dynamically organized and smoothly scrollable within a `ScrollView`.
* **Custom Task Dialog:** A specialized 3:4 ratio popup with a blurred (API 31+) or dimmed background for adding new daily tasks.
* **Persistent State:** Utilizes `SharedPreferences` to ensure the task list, maximum capacity, and completed counts survive app restarts.
* **Time-Based Auto-Reset:** Includes a localized logic check that automatically resets the task counter 5 minutes after a designated task period ends.

## Tech Stack
* **Language:** Java
* **UI Design:** XML Views (LinearLayout, ScrollView, CardView)
* **Storage:** SharedPreferences (Local device storage)
* **Environment:** Android Studio (Minimum SDK: API 24)

## Roadmap (Upcoming Features)
Based on our comprehensive SDLC phase planning, future updates will include:
* **Room Database:** Transitioning from `SharedPreferences` to a robust SQLite architecture for handling repeating routines and daily exceptions.
* **Smart AI Engine:** A localized, Duolingo-style notification system that reacts to user progress, maintains streaks, and sends motivational reminders.
* **Bottom Navigation:** Splitting the architecture into 3 core screens: Dashboard (Overview), Timeline (Detailed Calendar), and Profile (Customization).

## Quick Start
1. Clone the repository to your local machine.
2. Open the directory via **Android Studio**.
3. Execute `Build > Clean Project` followed by `Rebuild Project` to ensure all XML resources are properly generated.
4. Run on a virtual emulator or connected physical Android device.
