package dataClasses

import java.util.*

data class Module(
    val uuid: UUID,
    val moduleCode: String,
    val moduleTitle: String,
    val regStudentIds: List<String>,
    val expectedAttendanceNumber: Int
)