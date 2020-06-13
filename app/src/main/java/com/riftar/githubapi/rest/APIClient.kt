package com.riftar.githubapi.rest

import com.riftar.githubapi.repository.Constant.API_KEY
import com.riftar.githubapi.repository.Constant.BaseURL
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

    fun getClient(): API {

        val client =
            OkHttpClient().newBuilder().addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", API_KEY)
                    .build()
                chain.proceed(request)
            }.build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BaseURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}