package com.example.smartcampuscompanion.di

import android.content.Context
import androidx.room.Room
import com.example.smartcampuscompanion.data.local.AppDatabase
import com.example.smartcampuscompanion.data.local.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.local.dao.TaskDao
import com.example.smartcampuscompanion.data.local.dao.UserDao
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import com.example.smartcampuscompanion.data.repository.TaskRepositoryImpl
import com.example.smartcampuscompanion.data.repository.UserRepository
import com.example.smartcampuscompanion.domain.repository.TaskRepository

object AppModule {
    @Volatile
    private var databaseInstance: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return databaseInstance ?: synchronized(this) {
            databaseInstance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "smart_campus_database"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { databaseInstance = it }
        }
    }

    fun provideAnnouncementDao(database: AppDatabase): AnnouncementDao = database.announcementDao()
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    fun provideAnnouncementRepository(dao: AnnouncementDao): AnnouncementRepository =
        AnnouncementRepository(dao)

    fun provideTaskRepository(dao: TaskDao): TaskRepository =
        TaskRepositoryImpl(dao)

    fun provideUserRepository(dao: UserDao): UserRepository =
        UserRepository(dao)
}
