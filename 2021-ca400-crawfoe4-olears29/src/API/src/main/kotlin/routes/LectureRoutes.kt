package routes

import LectureDaoInstance
import daoUtils.LectureDaoUtils.bookingClashChecker
import daoUtils.LectureDaoUtils.regStudentForLecture
import dataClasses.Lecture
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.joda.time.DateTime
import requestObjects.LecturePutRequest
import requestObjects.LectureRequest
import requestObjects.toLectureObject
import java.util.*

fun Route.lectureRouting() {
    route("/lecture") {
        get {
            call.respond(mapOf("lecture" to LectureDaoInstance.getAllLectures()))
        }
        post {
            try {
                //ToDo add check that module exists in the module table
                val lecture = call.receive<LectureRequest>()
                val lectureObject = lecture.toLectureObject()
                val clashingLectures = bookingClashChecker(lectureObject)

                if (clashingLectures.isNotEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, clashingLectures)
                } else {
                    LectureDaoInstance.createLecture(lectureObject)
                    call.respond(200)
                }
            } catch (e: Exception) {
                print(e)
            }
        }
        put {
            try {
                val lecture = call.receive<LecturePutRequest>().toLectureObject()
                val clashingLectures = bookingClashChecker(lecture)

                if (clashingLectures.isNotEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, clashingLectures)
                } else {
                    LectureDaoInstance.updateLecture(lecture)
                    call.respond(200)
                }
            } catch (e: Exception) {
                print(e)
            }
        }
        delete("/{uuid}") {
            val strUuid: String? = call.parameters["uuid"]
            if (strUuid != null) {
                val uuid: UUID = UUID.fromString(call.parameters["uuid"])
                LectureDaoInstance.deleteLecture(uuid)
                call.respond(200)

            }
        }
        get("/{uuid}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            if (uuid != null) {
                val response = LectureDaoInstance.getLecture(uuid)
                if (response != null)
                    call.respond(response)
                else call.respond("No lectures found for uuid")
            }
        }
        get("/module/{module_code}") {
            val moduleCode = call.parameters["module_code"]
            if (moduleCode != null) {
                val response = LectureDaoInstance.getAllModuleLectures(moduleCode)
                if (!response.isNullOrEmpty())
                    call.respond(response)
                else call.respond("No lectures found for module code")
            }
        }
        get("/module/{module_code}/{date}") {
            val moduleCode = call.parameters["module_code"]
            val date : DateTime = DateTime.parse(call.parameters["date"])
            if (moduleCode != null) {
                val allLectures = LectureDaoInstance.getAllModuleLectures(moduleCode)
                if (!allLectures.isNullOrEmpty()) {
                    val weeksLectures: MutableList<Lecture> = mutableListOf()
                    for (lecture in allLectures) {
                        if (lecture.date.weekOfWeekyear().get() == date.weekOfWeekyear().get()) {
                            weeksLectures.add(lecture)
                        }
                    }
                    call.respond(weeksLectures)
                }
                else call.respond("No lectures found for module code during the week given")
            }
        }
        get("/course/{course_code}") {
            val courseCode = call.parameters["course_code"]
            if (courseCode != null) {
                val response = LectureDaoInstance.getAllCourseLectures(courseCode)
                if (!response.isNullOrEmpty())
                    call.respond(response)
                else call.respond("No lectures found for course code")
            }
        }
        get("/course/{course_code}/{date}") {
            val courseCode = call.parameters["course_code"]
            val date : DateTime = DateTime.parse(call.parameters["date"])
            if (courseCode != null) {
                val allLectures = LectureDaoInstance.getAllCourseLectures(courseCode)
                if (!allLectures.isNullOrEmpty()) {
                    val weeksLectures: MutableList<Lecture> = mutableListOf()
                    for (lecture in allLectures) {
                        if (lecture.date.weekOfWeekyear().get() == date.weekOfWeekyear().get()) {
                            weeksLectures.add(lecture)
                        }
                    }
                    call.respond(weeksLectures)
                }
                else call.respond("No lectures found for course code during the week given")
            }
        }
        get("/next-module/{module_code}") {
            val moduleCode = call.parameters["module_code"]
            if (moduleCode != null) {
                val response = LectureDaoInstance.getNextLectureForModule(moduleCode)
                if (response != null)
                    call.respond(response)
                else call.respond("No new upcoming lectures found for moduleCode")
            }
        }
        get("/next-course/{course_code}") {
            val courseCode = call.parameters["course_code"]
            if (courseCode != null) {
                val response = LectureDaoInstance.getNextLectureForCourse(courseCode)
                if (response != null)
                    call.respond(response)
                else call.respond("No new upcoming lectures found for courseCode")
            }
        }
        get("/location/{location}") {
            val location = call.parameters["location"]
            if (location != null) {
                val response = LectureDaoInstance.getAllLecturesAtLocation(location)
                if (response != null)
                    call.respond(response)
                else call.respond("No lectures found for this location")
            }
        }
        get("/location/{location}/{date}") {
            val location = call.parameters["location"]
            val date : DateTime = DateTime.parse(call.parameters["date"])
            if (location != null) {
                val allLectures = LectureDaoInstance.getAllLecturesAtLocation(location)
                if (allLectures != null) {
                    val weeksLectures: MutableList<Lecture> = mutableListOf()
                    for (lecture in allLectures) {
                        if (lecture.date.weekOfWeekyear().get() == date.weekOfWeekyear().get()) {
                            weeksLectures.add(lecture)
                        }
                    }
                    call.respond(weeksLectures)
                }
                else call.respond("No lectures found for this location at provided time")
            }
        }
        get("/reg-device/{location}") {
            val location = call.parameters["location"]
            if (location != null) {
                val response = LectureDaoInstance.getLectureWithLocationAtNow(location)
                if (!response.isNullOrEmpty()) {
                    call.respond(response)
                } else {
                    call.respond("No lectures found for this location at current time")
                }
            }
        }

        get("/reg-user/{uuid}/{student-id}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            val studentId = call.parameters["student-id"]
            if (uuid != null && studentId != null) {
                try {
                    regStudentForLecture(uuid, studentId)
                    call.respond(200)
                } catch (e:Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message.toString())
                }
            } else {
                call.respond(404)
            }

        }
    }
}