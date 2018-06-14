package com.example.app_kt

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.example.app_kt.repository.UserRepository
import com.example.app_kt.repository.api.UserApi
import com.example.app_kt.repository.db.AppDatabase
import com.example.app_kt.viewmodel.UserListViewModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager



class App: Application() {

    companion object {

        var DEBUG = false

        private lateinit var retrofit: Retrofit
        private lateinit var userApi: UserApi
        private lateinit var userRepository: UserRepository
        private lateinit var userListViewModel: UserListViewModel
        private lateinit var appDatabase: AppDatabase

        fun injectUserApi() = userApi
        fun injectUserListViewModel() = userListViewModel
        fun injectUserDao() = appDatabase.userDao()
    }

    override fun onCreate() {
        super.onCreate()
        DEBUG = isDebuggable(this)

        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://randomapi.com/api/")
                .build()


        userApi = retrofit.create(UserApi::class.java)
        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mvvm-database").build()

        userRepository = UserRepository(userApi, appDatabase.userDao())
        userListViewModel = UserListViewModel(userRepository)
    }

    private fun isDebuggable(context: Context): Boolean {
        var debuggable = false

        val pm = context.packageManager
        try {
            val appinfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
            /* debuggable variable will remain false */
        }

        return debuggable
    }

}