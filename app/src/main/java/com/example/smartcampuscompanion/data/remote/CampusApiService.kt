package com.example.smartcampuscompanion.data.remote

import com.example.smartcampuscompanion.data.remote.dto.AnnouncementDto
import com.example.smartcampuscompanion.data.remote.dto.AuthResponseDto
import com.example.smartcampuscompanion.data.remote.dto.DepartmentDto
import com.example.smartcampuscompanion.data.remote.dto.LoginRequestDto
import com.example.smartcampuscompanion.data.remote.dto.RegisterRequestDto
import com.example.smartcampuscompanion.data.remote.dto.TaskCreateRequestDto
import com.example.smartcampuscompanion.data.remote.dto.TaskDto
import com.example.smartcampuscompanion.data.remote.dto.TaskUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CampusApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("logout")
    suspend fun logout()

    @GET("tasks")
    suspend fun getTasks(): List<TaskDto>

    @POST("tasks")
    suspend fun createTask(@Body request: TaskCreateRequestDto): TaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body request: TaskUpdateRequestDto
    ): TaskDto

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int)

    @GET("announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>

    @POST("announcements")
    suspend fun createAnnouncement(@Body request: com.example.smartcampuscompanion.data.remote.dto.AnnouncementCreateRequestDto): AnnouncementDto

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(@Path("id") id: Long)

    @GET("departments")
    suspend fun getDepartments(): List<DepartmentDto>
}
