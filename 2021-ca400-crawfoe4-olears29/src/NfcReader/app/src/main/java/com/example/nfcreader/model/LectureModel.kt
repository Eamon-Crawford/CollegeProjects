package com.example.nfcreader.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import java.util.*


data class LectureModel(
        @Expose
        @SerializedName("uuid")
        val uuid: UUID,
        @SerializedName("courseCode")
        val courseCode: String,
        @Expose
        @SerializedName("moduleCode")
        val moduleCode: String,
        @Expose
        @SerializedName("date")
        val date: DateTime,
        @Expose
        @SerializedName("endTime")
        val endTime: DateTime,
        @Expose
        @SerializedName("location")
        val location: String,
        @Expose
        @SerializedName("type")
        val type: String,
        @Expose
        @SerializedName("expectedAttendanceNumber")
        val expectedAttendanceNumber: Int,
        @Expose
        @SerializedName("actualAttendanceNumber")
        val actualAttendanceNumber: Int,
        @Expose
        @SerializedName("expectedAttendance")
        val expectedAttendance: List<String>,
        @Expose
        @SerializedName("actualAttendance")
        val actualAttendance: List<String>
)
