package com.example.smartcampuscompanion.data.local.dao

import androidx.room.*
import com.example.smartcampuscompanion.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(entity: AnnouncementEntity)

    @Query("UPDATE announcements SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("SELECT COUNT(*) FROM announcements WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Delete
    suspend fun deleteAnnouncement(entity: AnnouncementEntity)
}

