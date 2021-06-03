package requestObjects

import dataClasses.StudentUser
import java.util.*

data class StudentUserRequest(
        val firstName: String,
        val lastName: String,
        val studentId: String,
        val courseCode : String,
        val moduleCodes : List<String>
)

fun StudentUserRequest.toStudentUserObject() = StudentUser(
        uuid = UUID.randomUUID(),
        firstName = firstName,
        lastName = lastName,
        studentId = studentId,
        courseCode = courseCode,
        moduleCodes = moduleCodes
)


