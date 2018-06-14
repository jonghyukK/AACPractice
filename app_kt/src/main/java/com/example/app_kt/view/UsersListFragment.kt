package com.example.app_kt.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.app_kt.App
import com.example.app_kt.Dlog
import com.example.app_kt.R
import com.example.app_kt.viewmodel.data.UsersList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.users_fragment.*
import java.net.ConnectException


class UsersListFragment : MvvmFragment() {

    val userListViewModel = App.injectUserListViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        subscribe(userListViewModel.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
//                    Log.d("dddddd", "Received UIModel with {$it.users.size} users.")

                    Dlog.d("Received UIModel with {$it.users.size} users.")
                    showUsers(it)
                }, {
                    Log.w("ddddddd", it)
                })
        )

        // test
    }

    fun showUsers(data: UsersList) {
         if (data.error == null) {
             listView.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, data.users)
         } else if (data.error is ConnectException) {
//             Log.d("ddddddd", "No connection, maybe inform user that data loaded from DB.")
             Dlog.d("No connection, maybe inform user that data loaded from DB.")
         } else {
             showError()
         }
    }

    fun showError() {
        Toast.makeText(context, "An error occureed : (", Toast.LENGTH_SHORT).show()
    }
}