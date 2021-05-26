package requestObjects

import daoUtils.ModuleDaoUtils.getExpectedAttendance
import daoUtils.ModuleDaoUtils.getExpectedAttendanceNumber
import dataClasses.Lecture
import org.joda.time.DateTime
import java.util.*

data class LectureRequest(
        val courseCode : String,
        val moduleCode : String,
        val date : String,
        val endTime : String,
        val location : String,
        val type: String
)

fun LectureRequest.toLectureObject() =
        Lecture(
        uuid = UUID.randomUUID(),
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
