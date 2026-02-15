package com.example.smartcampuscompanion.data.repository
import com.example.smartcampuscompanion.data.local.dao.AnnouncementDao
import com.example.smartcampuscompanion.domain.mapper.toDomain
import com.example.smartcampuscompanion.domain.mapper.toEntity
import com.example.smartcampuscompanion.domain.model.Announcement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnnouncementRepository(
    private val dao: AnnouncementDao
) {

    fun getAnnouncements(): Flow<List<Announcement>> =
        dao.getAllAnnouncements().map { list -> list.map { it.toDomain() } }

    suspend fun addAnnouncement(announcement: Announcement) {
        dao.insertAnnouncement(announcement.toEntity())
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        dao.deleteAnnouncement(announcement.toEntity())
    }
}

