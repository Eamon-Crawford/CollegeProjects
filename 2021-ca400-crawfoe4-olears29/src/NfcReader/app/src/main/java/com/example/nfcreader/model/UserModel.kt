package com.example.nfcreader.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


data class UserModel(
        @Expose
        @SerializedName("uuid")
        val uuid: UUID,
        @Expose
        @SerializedName("studentId")
        val studentId: String,
        @Expose
        @SerializedName("firstName")
        val firstName: String,
        @Expose
        @SerializedName("lastName")
        val lastName: String,
        @Expose
        @SerializedName("courseCode")
        val courseCode: String,
        @Expose
        @SerializedName("moduleCodes")
        val moduleCodes: List<String>,
        @Expose
        @SerializedName("flag")
        val flag: Boolean
)
