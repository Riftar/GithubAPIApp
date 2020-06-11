package com.riftar.githubapi.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.rest.API
import com.riftar.githubapi.rest.GetSearchUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {
    var listUser = MutableLiveData<List<User>>()
    private val api: API
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(API::class.java)
    }

    fun getUserByQuery(query: String, page: Int, context: Context?) : LiveData<List<User>> {
        api.getUserByQuery(query = query, page = page, perPage = 10)
            .enqueue(object : Callback<GetSearchUserResponse>{
                override fun onResponse(call: Call<GetSearchUserResponse>, response: Response<GetSearchUserResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            listUser.postValue(responseBody.items)

                        } else {
                            Toast.makeText(context, "error_fetch_movies111", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "error_fetch_movies222", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GetSearchUserResponse>, t: Throwable) {
                    Toast.makeText(context, "error_fetch_movies333", Toast.LENGTH_SHORT).show()
                }
            })
        return listUser
    }

}