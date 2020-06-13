package com.riftar.githubapi.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.rest.API
import io.reactivex.disposables.CompositeDisposable

class UserDataSourceFactory(private val apiService: API, private val compositeDisposable: CompositeDisposable, private val keyword:String): DataSource.Factory<Int, User>() {

    val userLiveDataSource = MutableLiveData<UserDataSource>()

    override fun create(): DataSource<Int, User> {
        val userDataSource = UserDataSource(apiService, compositeDisposable, keyword)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }
}