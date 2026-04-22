package com.example.smartcampuscompanion.di

import android.content.Context
import androidx.room.Room
import com.example.smartcampuscompanion.data.AppDatabase
import com.example.smartcampuscompanion.data.UserDao
import com.example.smartcampuscompanion.repository.UserRepository

object AppModule {

    // Provide Room Database
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // Provide DAO
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    // Provide Repository
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
}
