package com.riftar.githubapi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.Constant.FIRST_PAGE
import com.riftar.githubapi.repository.Constant.POST_PER_PAGE
import com.riftar.githubapi.rest.API
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.log

class UserDataSource(private val apiService: API, private val compositeDisposable: CompositeDisposable, private val keyword:String): PageKeyedDataSource<Int, User>() {

    private var page = FIRST_PAGE
    private var perPage = POST_PER_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getUserByQuery(keyword, perPage, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.items, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                        //Log.d("debug", "loadInitial: ${it.items[0].login}")
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        //Log.d("debug", "loadInitial: $it")
                    })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        compositeDisposable.add(
            apiService.getUserByQuery(keyword, perPage, params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        //should check if it end of list
                        callback.onResult(it.items,  params.key+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)

                        Log.d("debug", "loadInitial: $it")
                    })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {

    }
}