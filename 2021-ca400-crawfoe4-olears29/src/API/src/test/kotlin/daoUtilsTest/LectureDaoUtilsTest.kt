package daoUtilsTest

import LectureDaoInstance
import daoUtils.LectureDaoUtils.bookingClashChecker
import daoUtils.LectureDaoUtils.getNextLecture
import daoUtils.LectureDaoUtils.listToString
import daoUtils.LectureDaoUtils.regStudentForLecture
import daoUtils.LectureDaoUtils.returnLecturesAfterNow
import daoUtils.LectureDaoUtils.stringToList
import dataClasses.Lecture
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LectureDaoUtilsTest {
    private val uuid = UUID.randomUUID()
    private val uuid2 = UUID.randomUUID()
    private val listOfStudentIds: List<String> = listOf("12345678", "12312312", "32132132", "87654321")
    private val stringOfStudentIds: String = "12345678, 12312312, 32132132, 87654321"
    private val listOfSingleStudentId: List<String> = listOf("12312312")
    private val lectureObject = Lecture(
        uuid = uuid,
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.parse("2021-02-01T12:00:00.000"),
        endTime = DateTime.parse("2021-02-01T13:30:00.000"),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOfSingleStudentId
    )
    private val clashingLectureObject = Lecture(
        uuid = UUID.randomUUID(),
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.parse("2021-02-01T12:30:00.000"),
        endTime = DateTime.parse("2021-02-01T14:00:00.000"),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOfSingleStudentId
    )
    private val futureLectureObject = Lecture(
        uuid = uuid2,
        courseCode = "CASE4",
        moduleCode = "CA4003",
        date = DateTime.parse("2025-02-01T12:00:00.000"),
        endTime = DateTime.parse("2025-02-01T13:30:00.000"),
        location = "GLA.L102",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOfSingleStudentId
    )
    private val farFutureLectureObject = Lecture(
        uuid = uuid2,
        courseCode = "CASE4",
        moduleCode = "CA4003",
        date = DateTime.parse("2121-02-01T12:00:00.000"),
        endTime = DateTime.parse("2121-02-01T13:30:00.000"),
        location = "GLA.L102",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOfSingleStudentId
    )
    private val listOfLectures = listOf<Lecture>(lectureObject, futureLectureObject, farFutureLectureObject)


    @Test
    fun testAttendanceToList() {
        val result = stringToList(stringOfStudentIds)
        Assertions.assertEquals(listOfStudentIds, result)
    }

    @Test
    fun testAttendanceToString() {
        val result = listToString(listOfStudentIds)
        Assertions.assertEquals(stringOfStudentIds, result)
    }

    @Test
    fun testRegStudentForLecture() {
        mockkObject(LectureDaoInstance)
        every { LectureDaoInstance.getLecture(uuid) } returns lectureObject

        regStudentForLecture(uuid, "12345678")

        val expectedList = listOf<String>("12312312", "12345678")
        verify { LectureDaoInstance.updateLectureAttendance(uuid, expectedList) }
    }

    @Test
    fun testRegStudentForLectureCatchExceptionMarkedAsAttending() {
        mockkObject(LectureDaoInstance)
        every { LectureDaoInstance.getLecture(uuid) } returns lectureObject
        assertFailsWith<Exception>("User is already marked as attending") { regStudentForLecture(uuid, "12312312") }
    }

    @Test
    fun testRegStudentForLectureCatchExceptionNotRegistered() {
        mockkObject(LectureDaoInstance)
        every { LectureDaoInstance.getLecture(uuid) } returns lectureObject
        assertFailsWith<Exception>("User is not registered for module") { regStudentForLecture(uuid, "404") }
    }

    @Test
    fun testReturnLecturesAfterNow() {
        val result = returnLecturesAfterNow(listOfLectures)
        assert(result.size == 2)
        assertEquals(futureLectureObject, result[0])
        assertEquals(farFutureLectureObject, result[1])
    }

    @Test
    fun testReturnLecturesAfterNowNull() {
        val result = returnLecturesAfterNow(listOf())
        assert(result.isEmpty())
    }

    @Test
    fun testGetNextLecture() {
        val result = getNextLecture(listOfLectures)
        assertEquals(futureLectureObject, result)
    }

    @Test
    fun testGetNextLectureNull() {
        val result = getNextLecture(listOf())
        assertNull(result)
    }

    @Test
    fun testBookingClashChecker_NoLecture() {
        mockkObject(LectureDaoInstance)
        every { LectureDaoInstance.getAllLecturesAtLocation(lectureObject.location) } returns listOf()

        val result = bookingClashChecker(lectureObject)
        assert(result.isEmpty())
        assertEquals(listOf(), result)
    }
    @Test
    fun testBookingClashChecker_TwoClashes() {
        mockkObject(LectureDaoInstance)
        every { LectureDaoInstance.getAllLecturesAtLocation(lectureObject.location) } returns listOf(lectureObject, clashingLectureObject)

        val result = bookingClashChecker(lectureObject)
        assert(result.size == 2)
        assertEquals(lectureObject, result[0])
        assertEquals(clashingLectureObject, result[1])
    }

    @Test
    fun testBookingClashChecker_OneClash() {
        mockkObject(LectureDaoInstance)
        every { LectureDaoInstance.getAllLecturesAtLocation(lectureObject.location) } returns listOf(clashingLectureObject)

        val result = bookingClashChecker(lectureObject)
        assert(result.size == 1)
        assertEquals(clashingLectureObject, result[0])
    }
}