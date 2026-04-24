package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.remote.CampusApiService
import com.example.smartcampuscompanion.data.remote.dto.AnnouncementCreateRequestDto
import com.example.smartcampuscompanion.data.remote.dto.AnnouncementDto
import com.example.smartcampuscompanion.domain.model.Announcement
import com.example.smartcampuscompanion.features.auth.data.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import java.time.Instant

class AnnouncementRepository(
    private val api: CampusApiService,
    private val sessionManager: SessionManager
) {
    private val announcementsDtoState = MutableStateFlow<List<AnnouncementDto>>(emptyList())
    private val readAnnouncementIds = MutableStateFlow<Set<Long>>(sessionManager.getReadAnnouncementIds())

    fun getAnnouncements(): Flow<List<Announcement>> = combine(
        announcementsDtoState,
        readAnnouncementIds
    ) { dtos, readIds ->
        dtos.map { it.toDomain(readIds.contains(it.id)) }
    }.onStart {
        reloadReadAnnouncementIds()
        refreshAnnouncements()
    }

    private suspend fun refreshAnnouncements() {
        try {
            val dtos = api.getAnnouncements()
            announcementsDtoState.value = dtos
        } catch (_: Exception) {
            // keep old state
        }
    }

    suspend fun addAnnouncement(announcement: Announcement) {
        val request = AnnouncementCreateRequestDto(
            title = announcement.title,
            content = announcement.content,
            category = announcement.category,
            isImportant = announcement.isImportant
        )
        api.createAnnouncement(request)
        refreshAnnouncements()
    }

    suspend fun markAnnouncementAsRead(id: Long) {
        sessionManager.saveReadAnnouncementId(id)
        readAnnouncementIds.update { it + id }
    }

    fun getUnreadAnnouncementCount(): Flow<Int> = getAnnouncements()
        .map { announcements -> announcements.count { !it.isRead } }
        .catch { emit(0) }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        api.deleteAnnouncement(announcement.id)
        refreshAnnouncements()
    }

    private fun AnnouncementDto.toDomain(isRead: Boolean): Announcement {
        return Announcement(
            id = id,
            title = title,
            content = content,
            category = category,
            isImportant = isImportant,
            createdAt = createdAt.toEpochMillis(),
            isRead = isRead
        )
    }

    private fun reloadReadAnnouncementIds() {
        readAnnouncementIds.value = sessionManager.getReadAnnouncementIds()
    }

    private fun String?.toEpochMillis(): Long {
        if (this.isNullOrBlank()) return System.currentTimeMillis()
        return try {
            Instant.parse(this).toEpochMilli()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }
}

