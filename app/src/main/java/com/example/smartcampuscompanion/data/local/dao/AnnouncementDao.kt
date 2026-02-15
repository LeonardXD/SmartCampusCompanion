package com.example.smartcampuscompanion.data.local.dao
@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    suspend fun getAll(): List<AnnouncementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AnnouncementEntity)

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun delete(id: Long)
}

