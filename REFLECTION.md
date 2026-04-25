During the midterm phase, our team experienced challenges in managing multiple feature branches, especially when two members edited the same file for the task manager module. This caused a merge conflict during integration into the develop branch. We resolved the conflict by reviewing both versions of the code, discussing which logic to keep, and testing the app afterward. From this experience, we learned the importance of frequent pulls, clear communication, and proper branch management.

In the final phase, I implemented several critical fixes and features:
1. **Dark Mode Support**: Added a persistent dark mode toggle in settings using SharedPreferences and StateFlow for immediate UI updates.
2. **Announcement Management**: Built a complete admin interface for posting and deleting announcements, with backend support in Laravel.
3. **Task Validation**: Fixed timezone offsets by switching to UTC/ISO 8601 for API communication and added validation to prevent past-due task creation.
4. **Persistent Notifications**: Resolved a bug where notification read status was lost on app restart by persisting viewed announcement IDs in SharedPreferences.
5. **State Management**: Refactored the app to use a shared repository instance for announcements, ensuring notification counts remain consistent across different screens.
