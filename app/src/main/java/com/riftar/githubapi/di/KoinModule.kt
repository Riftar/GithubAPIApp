package com.riftar.moviedb.di

import com.riftar.githubapi.repository.UserPagedListRepository
import com.riftar.githubapi.repository.UserRepository
import com.riftar.githubapi.rest.API
import com.riftar.githubapi.rest.APIClient
import com.riftar.githubapi.viewmodel.MainActivityViewModel
import com.riftar.githubapi.viewmodel.UserViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dbModule = module {
//    single { MovieDB.getAppDataBase(context = get()) }
//    factory { get<MovieDB>().movieDao() }
    //factory { APIClient.getClient() }
}
val repositoryModule = module {
    single { UserRepository() }
    //single {UserPagedListRepository(get())}
}
val uiModule = module {
    viewModel{ UserViewModel(get())}
   // viewModel { MainActivityViewModel(get()) }
}