package requestObjects

import daoUtils.ModuleDaoUtils.getExpectedAttendance
import daoUtils.ModuleDaoUtils.getExpectedAttendanceNumber
import dataClasses.Lecture
import org.joda.time.DateTime
import java.util.*

data class LecturePutRequest(
    val uuid: UUID,
    val courseCode : String,
    val moduleCode : String,
    val date : String,
    val endTime : String,
    val location : String,
    val type: String,
    val expectedAttendanceNumber : Int,
    val actualAttendanceNumber: Int,
    val expectedAttendance: List<String>,
    var actualAttendance: List<String>
)

fun LecturePutRequest.toLectureObject() =
    Lecture(
        uuid = uuid,
        courseCode = courseCode,
        moduleCode = moduleCode,
        date = DateTime.parse(date),
        endTime = DateTime.parse(endTime),
        location = location,
        type = type,
        expectedAttendanceNumber = getExpectedAttendanceNumber(moduleCode),
        actualAttendanceNumber = 0,
        expectedAttendance = getExpectedAttendance(moduleCode),
        actualAttendance = listOf()
    )

