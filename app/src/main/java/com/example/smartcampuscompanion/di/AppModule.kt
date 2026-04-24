package com.example.smartcampuscompanion.di

import android.content.Context
import androidx.room.Room
import com.example.smartcampuscompanion.data.local.AppDatabase
import com.example.smartcampuscompanion.data.local.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.local.dao.TaskDao
import com.example.smartcampuscompanion.data.local.dao.UserDao
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import com.example.smartcampuscompanion.data.repository.DepartmentRepository
import com.example.smartcampuscompanion.data.repository.TaskRepositoryImpl
import com.example.smartcampuscompanion.data.repository.UserRepository
import com.example.smartcampuscompanion.data.remote.CampusApiService
import com.example.smartcampuscompanion.core.utils.Constants
import com.example.smartcampuscompanion.domain.repository.TaskRepository
import com.example.smartcampuscompanion.features.auth.data.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    @Volatile
    private var databaseInstance: AppDatabase? = null
    @Volatile
    private var retrofitInstance: Retrofit? = null

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

    fun provideApiService(sessionManager: SessionManager): CampusApiService {
        return provideRetrofit(sessionManager).create(CampusApiService::class.java)
    }

    fun provideAnnouncementRepository(api: CampusApiService, sessionManager: SessionManager): AnnouncementRepository =
        AnnouncementRepository(api, sessionManager)

    fun provideTaskRepository(api: CampusApiService): TaskRepository =
        TaskRepositoryImpl(api)

    fun provideUserRepository(api: CampusApiService, sessionManager: SessionManager): UserRepository =
        UserRepository(api, sessionManager)

    fun provideDepartmentRepository(api: CampusApiService): DepartmentRepository =
        DepartmentRepository(api)

    private fun provideRetrofit(sessionManager: SessionManager): Retrofit {
        return retrofitInstance ?: synchronized(this) {
            retrofitInstance ?: Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(provideOkHttpClient(sessionManager))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also { retrofitInstance = it }
        }
    }

    private fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sessionManager.getToken()
                val request = chain.request().newBuilder().apply {
                    if (!token.isNullOrBlank()) {
                        addHeader("Authorization", "Bearer $token")
                    }
                    addHeader("Accept", "application/json")
                }.build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }
}
