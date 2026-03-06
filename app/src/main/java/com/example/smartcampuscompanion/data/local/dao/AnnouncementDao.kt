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

    @Delete
    suspend fun deleteAnnouncement(entity: AnnouncementEntity)
}

