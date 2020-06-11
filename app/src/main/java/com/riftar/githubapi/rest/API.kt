package com.riftar.githubapi.rest

import com.riftar.githubapi.db.entities.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("search/users")
    fun getUserByQuery(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Call<GetSearchUserResponse>

}