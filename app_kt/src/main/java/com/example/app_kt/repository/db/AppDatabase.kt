package com.example.app_kt.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.app_kt.repository.data.User


@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}