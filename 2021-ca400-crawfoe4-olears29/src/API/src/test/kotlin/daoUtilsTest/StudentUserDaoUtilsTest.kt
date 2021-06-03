package daoUtilsTest

import daoUtils.StudentUserDaoUtils.studentsInModule
import dataClasses.StudentUser
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class StudentUserDaoUtilsTest {
    private val moduleCodesCASE4: List<String> = listOf("CA4003", "CA4004", "CA4005", "CA4007","CA4009","CA4012")
    private val moduleCodesCASE3: List<String> = listOf("CA3003", "CA3004", "CA3005", "CA3007","CA3009","CA3012")

    val user1 = StudentUser(
        uuid = UUID.randomUUID(),
        firstName = "John",
        lastName = "Test",
        studentId = "12345678",
        courseCode = "CASE4",
        moduleCodes = moduleCodesCASE4
    )
    val user2 = StudentUser(
        uuid = UUID.randomUUID(),
        firstName = "Tom",
        lastName = "Test",
        studentId = "87654321",
        courseCode = "CASE4",
        moduleCodes = moduleCodesCASE3
    )

    val listOfStudentUsers = listOf(user1, user2)


    @Test
    fun testStudentsInModule() {
        val result = studentsInModule("CA4004",listOfStudentUsers)
        assert(result.size == 1)
        assertEquals(user1, result[0])
    }

    @Test
    fun testStudentsInModule_NoStudents() {
        val result = studentsInModule("CA4004",listOf(user2))
        assert(result.isEmpty())
        assertEquals(listOf(), result)
    }


}