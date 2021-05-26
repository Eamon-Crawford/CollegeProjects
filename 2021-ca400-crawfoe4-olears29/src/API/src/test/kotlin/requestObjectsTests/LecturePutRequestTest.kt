package requestObjectsTests

import daoUtils.ModuleDaoUtils
import dataClasses.Lecture
import io.mockk.every
import io.mockk.mockkObject
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import requestObjects.LecturePutRequest
import requestObjects.toLectureObject
import java.util.*

class LecturePutRequestTest {
    private val uuid = UUID.randomUUID()
    private val expectedAttendanceWith4Ids: List<String> = listOf("11231231", "21231232", "31231233", "41231234")
    private val actualAttendanceWith4Ids: List<String> = listOf("21231232", "31231233", "41231234")
    private val lecturePutRequest = LecturePutRequest(
        uuid = uuid,
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = "2021-02-01T12:00:00.000",
        endTime = "2021-02-01T13:30:00.000",
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 3,
        expectedAttendance = expectedAttendanceWith4Ids,
        actualAttendance = actualAttendanceWith4Ids
    )

    //some values are handled dynamically later on, so they get reset for now
    private val lecturePutRequestAsALectureObject = Lecture(
        uuid = uuid,
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.parse("2021-02-01T12:00:00.000"),
        endTime = DateTime.parse("2021-02-01T13:30:00.000"),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = expectedAttendanceWith4Ids,
        actualAttendance = listOf()
    )

    @Test
    fun testToLectureObject() {
        mockkObject(ModuleDaoUtils)
        every { ModuleDaoUtils.getExpectedAttendanceNumber(any())} returns 4
        every { ModuleDaoUtils.getExpectedAttendance(any())} returns expectedAttendanceWith4Ids

        val lectureObject = lecturePutRequest.toLectureObject()
        Assertions.assertEquals(lecturePutRequestAsALectureObject, lectureObject)
    }
}