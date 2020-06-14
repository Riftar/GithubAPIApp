package com.riftar.githubapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.*
import com.riftar.githubapi.rest.API
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val apiService: API): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    lateinit var userDataSourceFactory: UserDataSourceFactory

    lateinit var userPagedList : LiveData<PagedList<User>>

    lateinit var mNetworkState: LiveData<NetworkState>
    val isFetched = MutableLiveData<Boolean>()

    init {
        isFetched.postValue(false)
        Log.d("debug", "viewmodel: ")
    }
    fun fetchLiveUserPagedList(keyword:String){
        isFetched.postValue(true)
        userDataSourceFactory = UserDataSourceFactory(apiService, compositeDisposable, keyword)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(Constant.POST_PER_PAGE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun getNetworkState(): LiveData<NetworkState>{
        mNetworkState= Transformations.switchMap<UserDataSource, NetworkState>(
            userDataSourceFactory.userLiveDataSource, UserDataSource::networkState
        )
        return mNetworkState
    }

    fun listIsEmpty(): Boolean {
        return userPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}