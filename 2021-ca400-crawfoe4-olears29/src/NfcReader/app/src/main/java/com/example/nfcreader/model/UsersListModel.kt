package com.example.nfcreader.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UsersListModel(
    @Expose
    @SerializedName("users")
    val usersList: List<UserModel>
)
