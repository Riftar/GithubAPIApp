package com.riftar.githubapi.rest

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.riftar.githubapi.db.entities.User


data class GetSearchUserResponse (
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<User>
)