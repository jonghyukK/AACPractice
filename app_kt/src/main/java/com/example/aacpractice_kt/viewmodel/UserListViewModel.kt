package com.example.aacpractice_kt.viewmodel

import android.util.Log
import io.reactivex.Observable
import com.example.aacpractice_kt.repository.UserRepository
import com.example.aacpractice_kt.viewmodel.data.UsersList
import java.util.*
import java.util.concurrent.TimeUnit


class UserListViewModel(val userRepository: UserRepository) {

    fun getUsers(): Observable<UsersList> {

        return userRepository.getUsers()
                .debounce(400, TimeUnit.MILLISECONDS)
                .map {
                    Log.d("ddddddd", "Mapping users to UIData ...")
                    UsersList(it.take(10), "Top 10 Users")
                }
                .onErrorReturn {
                    UsersList(emptyList(), "An error occurred", it)
                }
    }
}