package dataClasses

import java.util.*

data class StudentUser(
    val uuid: UUID,
    val firstName: String,
    val lastName: String,
    val studentId: String,
    val courseCode : String,
    val moduleCodes: List<String>,
)
