package com.riftar.moviedb.di

import com.riftar.githubapi.repository.UserRepository
import com.riftar.githubapi.viewmodel.UserViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dbModule = module {
//    single { MovieDB.getAppDataBase(context = get()) }
//    factory { get<MovieDB>().movieDao() }
}
val repositoryModule = module {
    single { UserRepository() }
}
val uiModule = module {
    viewModel{ UserViewModel(get())}
}