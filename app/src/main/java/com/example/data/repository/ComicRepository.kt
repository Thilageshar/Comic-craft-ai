package com.example.data.repository

import com.example.data.local.ComicDao
import com.example.data.local.ComicEntity
import com.example.data.model.ComicStory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ComicRepository(private val comicDao: ComicDao) {

    val allComics: Flow<List<ComicStory>> = comicDao.getAllComics().map { entities ->
        entities.map { it.toDomain() }
    }

    val favoriteComics: Flow<List<ComicStory>> = comicDao.getFavoriteComics().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getComicById(id: Long): ComicStory? {
        return comicDao.getComicById(id)?.toDomain()
    }

    suspend fun saveComic(story: ComicStory): Long {
        val entity = ComicEntity.fromDomain(story)
        return comicDao.insertComic(entity)
    }

    suspend fun updateComic(story: ComicStory) {
        val entity = ComicEntity.fromDomain(story)
        comicDao.updateComic(entity)
    }

    suspend fun deleteComic(story: ComicStory) {
        comicDao.deleteComicById(story.id)
    }

    suspend fun deleteComicById(id: Long) {
        comicDao.deleteComicById(id)
    }

    suspend fun setFavorite(id: Long, isFavorite: Boolean) {
        comicDao.updateFavorite(id, isFavorite)
    }
}
