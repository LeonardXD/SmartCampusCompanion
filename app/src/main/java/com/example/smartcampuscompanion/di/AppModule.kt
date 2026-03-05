package com.example.smartcampuscompanion.di

import android.content.Context
import androidx.room.Room
import com.example.smartcampuscompanion.data.local.AppDatabase
import com.example.smartcampuscompanion.data.local.dao.TaskDao
import com.example.smartcampuscompanion.data.repository.TaskRepositoryImpl
import com.example.smartcampuscompanion.domain.repository.TaskRepository

object AppModule {

    private var database: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "task_database"
            ).build()
            database = instance
            instance
        }
    }

    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao
    }

    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }
}
