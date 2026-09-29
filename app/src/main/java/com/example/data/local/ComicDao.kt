package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {
    @Query("SELECT * FROM comics ORDER BY createdAt DESC")
    fun getAllComics(): Flow<List<ComicEntity>>

    @Query("SELECT * FROM comics WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteComics(): Flow<List<ComicEntity>>

    @Query("SELECT * FROM comics WHERE id = :id LIMIT 1")
    suspend fun getComicById(id: Long): ComicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComic(comic: ComicEntity): Long

    @Update
    suspend fun updateComic(comic: ComicEntity)

    @Delete
    suspend fun deleteComic(comic: ComicEntity)

    @Query("DELETE FROM comics WHERE id = :id")
    suspend fun deleteComicById(id: Long)

    @Query("UPDATE comics SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
}
