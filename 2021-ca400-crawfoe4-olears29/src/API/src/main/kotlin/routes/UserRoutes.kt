package routes

import LectureDaoInstance
import ModuleDaoInstance
import StudentUserDaoInstance
import dataClasses.Lecture
import dataClasses.StudentUser
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.joda.time.DateTime
import requestObjects.StudentUserRequest
import requestObjects.toStudentUserObject
import java.util.*

fun Route.userRouting() {
    route("/users") {
        get {
            call.respond(mapOf("users" to StudentUserDaoInstance.getAllStudentUsers()))
        }

        post {
            try {
                val user = call.receive<StudentUserRequest>()
                val studentUserObject = user.toStudentUserObject()
                StudentUserDaoInstance.createStudentUser(studentUserObject)
                for (module in studentUserObject.moduleCodes) {
                    ModuleDaoInstance.regStudentForModule(module, studentUserObject.studentId)
                }
                call.respond(200)
            } catch(e: Exception) {
                println(e)
                call.respond(HttpStatusCode.BadRequest, e.toString())
            }
        }

        put {
            val studentUser = call.receive<StudentUser>()
            StudentUserDaoInstance.updateStudentUser(studentUser)
            call.respond(200)
        }

        delete("/{uuid}") {
            val strUuid: String? = call.parameters["uuid"]
            if (strUuid != null) {
                val uuid: UUID = UUID.fromString(call.parameters["uuid"])
                StudentUserDaoInstance.deleteStudentUser(uuid)
                call.respond(200)
            } else {
                call.respond(404)
            }
        }

        get("/{uuid}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            if (uuid != null) {
                val response = StudentUserDaoInstance.getStudentUser(uuid)
                if (response != null)
                    call.respond(response)
                else call.respond("No such user found!")
            }
        }

        get("/student/{studentId}") {
            val studentId  = call.parameters["studentId"]
            if (studentId != null) {
                val response = StudentUserDaoInstance.getStudentUser(studentId)
                if (response != null)
                    call.respond(response)
                else call.respond("No such user found!")
            }
        }

        get("/module/{moduleCode}") {
            //ToDo change this to pull from ModuleDao, should be faster. Need to tight up Module creation.
            val moduleCode = call.parameters["moduleCode"]
            if (moduleCode != null) {
                val response = StudentUserDaoInstance.getAllStudentUsersInModule(moduleCode)
                if (!response.isNullOrEmpty())
                    call.respond(response)
                else call.respond("No students for that module found!")
            }
        }
        get("/week-timetable/{studentId}") {
            //ToDO move to lecture route?
            val lecturesThisWeek: MutableList<Lecture> = mutableListOf()
            val studentId  = call.parameters["studentId"]
            val studentUser = StudentUserDaoInstance.getStudentUser(studentId!!)
            for (module in studentUser!!.moduleCodes) {
                val lectureList = LectureDaoInstance.getAllModuleLectures(module)
                for (lecture in lectureList) {
                    if (lecture.date.weekOfWeekyear().get() == DateTime.now().weekOfWeekyear().get()) {
                        lecturesThisWeek.add(lecture)
                    }
                }
            }
            if (!lecturesThisWeek.isNullOrEmpty())
                call.respond(lecturesThisWeek)
            else call.respond("No lectures found")

        }

        get("/future-timetable/{studentId}") {
            val futureLectures: MutableList<Lecture> = mutableListOf()
            val studentId  = call.parameters["studentId"]
            val studentUser = StudentUserDaoInstance.getStudentUser(studentId!!)
            for (module in studentUser!!.moduleCodes) {
                val lectureList = LectureDaoInstance.getAllModuleLectures(module)
                for (lecture in lectureList) {
                    if (lecture.date.isAfterNow) {
                        futureLectures.add(lecture)
                    }
                }
            }
            if (!futureLectures.isNullOrEmpty())
                call.respond(futureLectures)
            else call.respond("No lectures found")

        }

        get("/complete-timetable/{studentId}") {
            val allLectures: MutableList<Lecture> = mutableListOf()
            val studentId  = call.parameters["studentId"]
            val studentUser = StudentUserDaoInstance.getStudentUser(studentId!!)
            for (module in studentUser!!.moduleCodes) {
                val lectureList = LectureDaoInstance.getAllModuleLectures(module)
                for (lecture in lectureList) {
                    allLectures.add(lecture)
                }
            }
            if (!allLectures.isNullOrEmpty())
                call.respond(allLectures)
            else call.respond("No lectures found")
        }
    }
}