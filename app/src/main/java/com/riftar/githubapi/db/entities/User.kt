package com.riftar.githubapi.db.entities

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("login")
    var login: String? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("avatar_url")
    var avatarUrl: String? = null
}