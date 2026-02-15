package com.example.smartcampuscompanion.data.repository
class AnnouncementRepository(
    private val dao: AnnouncementDao
) {

    suspend fun getAllAnnouncements(): List<Announcement> =
        dao.getAll().map { it.toDomain() }

    suspend fun addAnnouncement(title: String, content: String) {
        dao.insert(
            AnnouncementEntity(
                title = title,
                content = content,
                category = "General",
                isImportant = false,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteAnnouncement(id: Long) {
        dao.delete(id)
    }
}

