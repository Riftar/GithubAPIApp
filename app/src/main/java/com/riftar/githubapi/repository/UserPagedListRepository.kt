package com.riftar.githubapi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.Constant.POST_PER_PAGE
import com.riftar.githubapi.rest.API
import io.reactivex.disposables.CompositeDisposable

class UserPagedListRepository(private val apiService: API) {
    lateinit var userPagedList : LiveData<PagedList<User>>
    lateinit var userDataSourceFactory: UserDataSourceFactory

    fun fetchLiveUserPagedList(compositeDisposable: CompositeDisposable, keyword:String){
        userDataSourceFactory = UserDataSourceFactory(apiService, compositeDisposable, keyword)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(POST_PER_PAGE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun getLiveUserPagedList(): LiveData<PagedList<User>>{
        return userPagedList
    }

    fun getUser(): LiveData<PagedList<User>>{
        return userPagedList
    }
    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<UserDataSource, NetworkState>(
            userDataSourceFactory.userLiveDataSource, UserDataSource::networkState
        )
    }
}