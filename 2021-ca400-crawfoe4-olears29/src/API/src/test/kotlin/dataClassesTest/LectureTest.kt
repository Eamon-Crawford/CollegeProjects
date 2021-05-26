package dataClassesTest

import dataClasses.Lecture
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class LectureTest {
    private val expectedAttendanceWith4Ids: List<String> = listOf("11231231", "21231232", "31231233", "41231234")
    private val actualAttendanceWith4Ids: List<String> = listOf("21231232", "31231233", "41231234")
    val pastLecture = Lecture(
        uuid = UUID.randomUUID(),
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.parse("2021-02-01T12:00:00.000"),
        endTime = DateTime.parse("2021-02-01T13:30:00.000"),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 3,
        expectedAttendance = expectedAttendanceWith4Ids,
        actualAttendance = actualAttendanceWith4Ids
    )
    val futureLecture = Lecture(
        uuid = UUID.randomUUID(),
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.parse("2121-02-01T12:00:00.000"),
        endTime = DateTime.parse("2121-02-01T13:30:00.000"),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 3,
        expectedAttendance = expectedAttendanceWith4Ids,
        actualAttendance = actualAttendanceWith4Ids

    )

    @Test
    fun testLectureListTypes() {
        Assertions.assertEquals(pastLecture.expectedAttendance, futureLecture.expectedAttendance)
    }

    @Test
    fun testLectureIsBeforeNow() {
        Assertions.assertFalse(pastLecture.date.isAfterNow)
        Assertions.assertTrue(pastLecture.date.isBeforeNow)
    }

    @Test
    fun testLectureIsAfterNow() {
        Assertions.assertTrue(futureLecture.date.isAfterNow)
        Assertions.assertFalse(futureLecture.date.isBeforeNow)
    }

    @Test
    fun testLectureAttendanceValuesMatch() {
        for (studentId in pastLecture.actualAttendance) {
            Assertions.assertTrue(pastLecture.expectedAttendance.contains(studentId))
        }
        Assertions.assertEquals(pastLecture.expectedAttendance.size, pastLecture.expectedAttendanceNumber)
        Assertions.assertEquals(pastLecture.actualAttendance.size, pastLecture.actualAttendanceNumber)
    }
}