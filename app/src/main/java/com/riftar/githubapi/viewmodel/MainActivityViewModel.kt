package com.riftar.githubapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.NetworkState
import com.riftar.githubapi.repository.UserPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val userPagedListRepository: UserPagedListRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val userPagedList : LiveData<PagedList<User>> by lazy {
        userPagedListRepository.fetchLiveUserPagedList(compositeDisposable, "rif")
    }

    val networkState: LiveData<NetworkState> by lazy {
        userPagedListRepository.getNetworkState()
    }

    fun fetchUser(keyword: String) : LiveData<PagedList<User>>{
        return userPagedListRepository.fetchLiveUserPagedList(compositeDisposable, keyword)
       // return userPagedList
    }

    fun listIsEmpty(): Boolean{
        return userPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}