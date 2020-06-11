package com.riftar.githubapi.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.UserRepository

class UserViewModel(private var repository: UserRepository):ViewModel() {

    fun getUserByQuery(query: String, page: Int, context: Context) = repository.getUserByQuery(query, page, context)
}