package com.example.smartcampuscompanion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartcampuscompanion.data.local.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.local.dao.TaskDao
import com.example.smartcampuscompanion.data.local.entity.AnnouncementEntity
import com.example.smartcampuscompanion.data.local.entity.TaskEntity

@Database(
    entities = [AnnouncementEntity::class, TaskEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun taskDao(): TaskDao
}

