package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Avatar::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun  playList(): PlayList
}