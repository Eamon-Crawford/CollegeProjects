package com.example.nfcreader.api

import com.example.nfcreader.model.LectureModel
import com.example.nfcreader.model.UsersListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlaceholderAPI {
    @GET("users")
    fun getUsers(): Call<UsersListModel>
    @GET("lecture/reg-device/{roomCode}")
    fun getCurrentLectures(@Path(value = "roomCode", encoded = true) roomCode: String): Call<List<LectureModel>>
    @GET("lecture/reg-user/{uuid}/{studentId}")
    fun regUserForLecture(@Path(value = "studentId", encoded = true) studentId: String, @Path(value = "uuid", encoded = true) uuid: String): Call<String>
}