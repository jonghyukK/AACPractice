package com.example.app_kt.repository

import android.util.Log
import com.example.app_kt.repository.api.UserApi
import com.example.app_kt.repository.data.User
import com.example.app_kt.repository.db.UserDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class UserRepository(val userApi: UserApi, val userDao: UserDao) {

    fun getUsers(): Observable<List<User>> {

        return Observable.concatArray(
                getUsersFromDb(),
                getUsersFromApi()
        )
    }

    fun getUsersFromDb(): Observable<List<User>> {
        return userDao.getUsers().filter{ it.isNotEmpty() }
                .toObservable()
                .doOnNext{
                    Log.d("ddddddd", "Dispatching ${it.size} users from DB...")
                }
    }

    fun getUsersFromApi(): Observable<List<User>> {
        return userApi.getUsers()
                .doOnNext {
                    Log.d("ddddddd", "Dispatching ${it.size} users from API ...")
                    storeUserInDb(it)
                }
    }

    fun storeUserInDb(users: List<User>){
        Observable.fromCallable { userDao.insertAll(users)}
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d("ddddddd", "Inserted ${users.size} users from API in DB ...")
                }
    }
}