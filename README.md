# Personal Calendar - V1 (Course MVP)

A lightweight Android application designed to track daily schedules and task completion. Developed as an introductory project for Mobile Software architecture.

## Core Features
*   **Daily Schedule View:** Displays scheduled activities and timeframes on the main screen.
*   **Task Counter:** Tracks completed tasks against the daily maximum capacity.
*   **Custom Dialog Integration:** Features a specialized 3:4 ratio popup with a blurred background (for Android 12+) or dimmed background (older versions) to add new tasks.
*   **Persistent State:** Utilizes `SharedPreferences` to ensure the task list, maximum capacity, and completed counts survive app restarts.
*   **Time-Based Auto-Reset:** Includes a localized logic check that automatically resets the task counter 5 minutes after a designated task period ends.

## Tech Stack
*   **Language:** Java
*   **UI Design:** XML Views (LinearLayouts, TextViews, Buttons)
*   **Storage:** SharedPreferences (Local device storage)
*   **Development Environment:** Android Studio (Minimum SDK: API 24)

## Quick Start
1. Clone the repository to your local machine.
2. Open the directory via Android Studio.
3. Execute `Build > Clean Project` followed by `Rebuild Project` to ensure all XML resources and `R` classes are properly generated.
4. Run on a virtual emulator or connected physical Android device.
