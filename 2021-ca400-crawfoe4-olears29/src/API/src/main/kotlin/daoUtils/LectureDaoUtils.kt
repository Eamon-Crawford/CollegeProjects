package daoUtils

import LectureDaoInstance
import dataClasses.Lecture
import java.util.*

object LectureDaoUtils {
    fun stringToList(studentIds : String): List<String> {
        return studentIds.split(",").map { it.trim() }
    }

    fun listToString(studentIds: List<String>) : String {
        print("DEBUG:" + studentIds.toString().drop(1).dropLast(1))
        return studentIds.toString().drop(1).dropLast(1)
    }

    fun regStudentForLecture(uuid: UUID, studentId: String) {
        val lecture = LectureDaoInstance.getLecture(uuid)
        val mutableList: MutableList<String> = mutableListOf()
        if (lecture?.actualAttendance?.size!! > 0) lecture.actualAttendance.forEach { student_id ->
            mutableList.add(student_id)
        }
        if (lecture.expectedAttendance.contains(studentId) && !lecture.actualAttendance.contains(studentId)) {
            mutableList.add(studentId)
            LectureDaoInstance.updateLectureAttendance(uuid, mutableList)
        } else if (lecture.actualAttendance.contains(studentId)) {
            throw Exception("User is already marked as attending")
        } else {
            throw Exception("User is not registered for module")
        }
    }

    fun returnLecturesAfterNow(lectureList : List<Lecture>): List<Lecture> {
        val futureLectures: MutableList<Lecture> = mutableListOf()
        for (lecture in lectureList) {
            if (lecture.date.isAfterNow) {
                futureLectures.add(lecture)
            }
        }
        return futureLectures
    }

    fun getNextLecture(lectureList :List<Lecture>): Lecture? {
        val futureLectures = returnLecturesAfterNow(lectureList)
        return if (!futureLectures.isNullOrEmpty()) {
            var nextLecture = futureLectures[0]
            for (lecture in futureLectures) {
                if (lecture.date.isAfterNow) {
                    if (lecture.date.isBefore(nextLecture.date)) {
                        nextLecture = lecture
                    }
                }
            }
            nextLecture
        } else {
            null
        }
    }

    fun bookingClashChecker(lecture : Lecture): List<Lecture> {
        val clashingLectures = mutableListOf<Lecture>()
        val otherLectures = LectureDaoInstance.getAllLecturesAtLocation(lecture.location)
        for (otherLecture in otherLectures) {
            if (!((lecture.date.isBefore(otherLecture.date) && lecture.endTime.isBefore(otherLecture.date))
                        || (lecture.date.isAfter(otherLecture.endTime) && lecture.endTime.isAfter(otherLecture.endTime)))) {
                clashingLectures.add(otherLecture)
            }
        }
        return clashingLectures
    }
}