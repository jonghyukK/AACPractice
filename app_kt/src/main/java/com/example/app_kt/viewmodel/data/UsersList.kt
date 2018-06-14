package com.example.app_kt.viewmodel.data

import com.example.app_kt.repository.data.User

data class UsersList(val users: List<User>, val message: String, val error: Throwable? = null)