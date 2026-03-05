package com.example.smartcampuscompanion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartcampuscompanion.data.local.dao.TaskDao
import com.example.smartcampuscompanion.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}
