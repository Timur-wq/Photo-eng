package com.example.myapplication

import androidx.room.*

@Dao
interface PlayList {
    @Query("SELECT * FROM tunes ORDER BY title")
    suspend fun select1(): List<Avatar?>?

    @Query("DELETE FROM tunes")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(vararg tunes: Avatar?)//vararg - передача множества аргументов

    @Delete
    suspend fun delete(vararg tunes: Avatar?)

    @Update
    suspend fun update(vararg tunes: Avatar?)
}