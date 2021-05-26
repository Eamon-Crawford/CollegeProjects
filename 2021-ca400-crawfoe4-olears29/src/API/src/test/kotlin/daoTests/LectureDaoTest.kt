package daoTests

import dao.LectureDao
import daoUtils.ModuleDaoUtils
import dataClasses.Lecture
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.jetbrains.exposed.sql.Database
import org.joda.time.DateTime
import org.junit.jupiter.api.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LectureDaoTest {
    private val testDb = Database.connect("jdbc:postgresql://localhost:5432/test_db", driver = "org.postgresql.Driver",
        user = "postgres", password = "postMondo360")

    private val lectureTestInstance = LectureDao(testDb)

    @BeforeAll
    fun bootstrap() {
        lectureTestInstance.init()
        mockkObject(ModuleDaoUtils)
        every { ModuleDaoUtils.getExpectedAttendance(lecture1.moduleCode) } returns lecture1.expectedAttendance
        every { ModuleDaoUtils.getExpectedAttendance(lecture2.moduleCode) } returns lecture2.expectedAttendance
    }

    @AfterAll
    fun unmock() {
        unmockkObject(ModuleDaoUtils)
    }

    private val uuid = UUID.randomUUID()
    private val uuid2 = UUID.randomUUID()
    private val uuid3 = UUID.randomUUID()
    private val listOfStudentIds: List<String> = listOf("12345678", "12312312", "32132132", "87654321")
    private val listOfSingleStudentId: List<String> = listOf("12312312")

    private val lecture1 = Lecture(
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
        actualAttendance = listOf()
    )
    private val updatedLecture1 = Lecture(
        uuid = uuid,
        courseCode = "CASE4",
        moduleCode = "CA4005",
        date = DateTime.parse("2021-02-01T12:00:00.000"),
        endTime = DateTime.parse("2021-02-01T13:30:00.000"),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOf()
    )
    private val lecture2 = Lecture(
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
    private val lectureNow = Lecture(
        uuid = uuid,
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.now(),
        endTime = DateTime.now().plusHours(2),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOf()
    )
    private val futureLecture = Lecture(
        uuid = uuid2,
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.now().plusMonths(2),
        endTime = DateTime.now().plusMonths(2).plusHours(2),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOf()
    )
    private val pastLecture = Lecture(
        uuid = uuid3,
        courseCode = "CASE4",
        moduleCode = "CA4004",
        date = DateTime.now().minusMonths(2),
        endTime = DateTime.now().minusMonths(2).plusHours(2),
        location = "GLA.L101",
        type = "lab",
        expectedAttendanceNumber = 4,
        actualAttendanceNumber = 0,
        expectedAttendance = listOfStudentIds,
        actualAttendance = listOf()
    )

    @Test
    fun `Create a lecture, get it and del it`() {
        lectureTestInstance.createLecture(lecture1)
        val response: Lecture? = lectureTestInstance.getLecture(lecture1.uuid)
        Assertions.assertEquals(lecture1, response)

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)
    }

    @Test
    fun `Create a lecture, updated it, get it and del it`() {
        lectureTestInstance.createLecture(lecture1)
        val response: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertEquals(lecture1, response)

        lectureTestInstance.updateLecture(updatedLecture1)
        val updatedResponse: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertEquals(updatedLecture1, updatedResponse)

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)
    }

    @Test
    fun `Create two lectures, get all and del them`() {
        lectureTestInstance.createLecture(lecture1)
        lectureTestInstance.createLecture(lecture2)

        val response = lectureTestInstance.getAllLectures()
        Assertions.assertEquals(lecture1, response[0])
        Assertions.assertEquals(lecture2, response[1])

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)

        lectureTestInstance.deleteLecture(uuid2)
        val delLecture2: Lecture? = lectureTestInstance.getLecture(uuid2)
        Assertions.assertNull(delLecture2)
    }

    @Test
    fun `Create two lectures, get all module & course lectures and del them`() {
        lectureTestInstance.createLecture(lecture1)
        lectureTestInstance.createLecture(lecture2)

        val moduleResponse = lectureTestInstance.getAllModuleLectures(lecture1.moduleCode)
        Assertions.assertEquals(lecture1, moduleResponse[0])

        val courseResponse = lectureTestInstance.getAllCourseLectures(lecture1.courseCode)
        Assertions.assertEquals(lecture1, courseResponse[0])
        Assertions.assertEquals(lecture2, courseResponse[1])

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)

        lectureTestInstance.deleteLecture(uuid2)
        val delLecture2: Lecture? = lectureTestInstance.getLecture(uuid2)
        Assertions.assertNull(delLecture2)
    }

    @Test
    fun `Create two lectures, get next lecture for course and del them`() {
        lectureTestInstance.createLecture(lecture1)
        lectureTestInstance.createLecture(lecture2)
        val nextCourseResponse = lectureTestInstance.getNextLectureForCourse(lecture1.courseCode)
        Assertions.assertEquals(lecture2, nextCourseResponse)

        val nextModuleResponse = lectureTestInstance.getNextLectureForModule(lecture1.moduleCode)
        Assertions.assertNull(nextModuleResponse)

        val nextModuleResponse2 = lectureTestInstance.getNextLectureForModule(lecture2.moduleCode)
        Assertions.assertEquals(lecture2, nextModuleResponse2)

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)

        lectureTestInstance.deleteLecture(uuid2)
        val delLecture2: Lecture? = lectureTestInstance.getLecture(uuid2)
        Assertions.assertNull(delLecture2)
    }

    @Test
    fun `Create two lectures, get lectures at location and del them`() {
        lectureTestInstance.createLecture(lecture1)
        lectureTestInstance.createLecture(lecture2)
        val locationResponse = lectureTestInstance.getAllLecturesAtLocation(lecture1.location)
        Assertions.assertEquals(lecture1, locationResponse[0])

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)

        lectureTestInstance.deleteLecture(uuid2)
        val delLecture2: Lecture? = lectureTestInstance.getLecture(uuid2)
        Assertions.assertNull(delLecture2)
    }

    @Test
    fun `Create two lectures, get lectures at location now and del them`() {
        lectureTestInstance.createLecture(lectureNow)
        lectureTestInstance.createLecture(futureLecture)
        lectureTestInstance.createLecture(pastLecture)
        val locationResponse = lectureTestInstance.getLectureWithLocationAtNow(lectureNow.location)
        Assertions.assertTrue(locationResponse.size == 1)
        Assertions.assertEquals(lectureNow, locationResponse[0])

        lectureTestInstance.deleteLecture(uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertNull(delLecture)

        lectureTestInstance.deleteLecture(uuid2)
        val delLecture2: Lecture? = lectureTestInstance.getLecture(uuid2)
        Assertions.assertNull(delLecture2)

        lectureTestInstance.deleteLecture(uuid3)
        val delLecture3: Lecture? = lectureTestInstance.getLecture(uuid3)
        Assertions.assertNull(delLecture3)
    }

    @Test
    fun `Create a lecture, updated attendance, get it and del it`() {
        lectureTestInstance.createLecture(lecture1)
        val response: Lecture? = lectureTestInstance.getLecture(uuid)
        Assertions.assertEquals(lecture1, response)

        lectureTestInstance.updateLectureAttendance(lecture1.uuid, listOfStudentIds)
        val updatedResponse: Lecture? = lectureTestInstance.getLecture(lecture1.uuid)
        Assertions.assertEquals(listOfStudentIds, updatedResponse?.actualAttendance)

        lectureTestInstance.deleteLecture(lecture1.uuid)
        val delLecture: Lecture? = lectureTestInstance.getLecture(lecture1.uuid)
        Assertions.assertNull(delLecture)
    }
}