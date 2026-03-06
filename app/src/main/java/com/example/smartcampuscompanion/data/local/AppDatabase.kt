package com.example.smartcampuscompanion.data.local
@Database(
    entities = [AnnouncementEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}

