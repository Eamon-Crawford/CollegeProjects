package daoTests

import dao.StudentUserDao
import dataClasses.StudentUser
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentUserDaoTest {
    private val testDb = Database.connect("jdbc:postgresql://localhost:5432/test_db", driver = "org.postgresql.Driver",
        user = "postgres", password = "postMondo360")

    private val studentUserTestInstance = StudentUserDao(testDb)

    @BeforeAll
    fun bootstrap() {
        studentUserTestInstance.init()
    }

    private val uuid = UUID.fromString("ee6ee294-b403-4f69-98af-6984705594ee")
    private val uuid2 = UUID.fromString("dd9dd294-b403-4f69-98af-6984705594ed")
    private val uuid3 = UUID.fromString("ab4ffe23-b403-4f69-98af-6984705594ed")
    private val moduleCodesCASE4: List<String> = listOf("CA4003", "CA4004", "CA4005", "CA4007","CA4009","CA4012")
    private val moduleCodesCASE3: List<String> = listOf("CA3003", "CA3004", "CA3005", "CA3007","CA3009","CA3012")

    private val user1 = StudentUser(
        uuid = uuid,
        firstName = "John",
        lastName = "Test",
        studentId = "12345678",
        courseCode = "CASE4",
        moduleCodes = moduleCodesCASE4
    )
    private val updateUser1 = StudentUser(
        uuid = uuid,
        firstName = "John",
        lastName = "Test",
        studentId = "12345678",
        courseCode = "CASE3",
        moduleCodes = moduleCodesCASE3
    )
    private val user2 = StudentUser(
        uuid = uuid2,
        firstName = "Tom",
        lastName = "Test",
        studentId = "87654321",
        courseCode = "CASE3",
        moduleCodes = moduleCodesCASE3
    )
    private val user3 = StudentUser(
        uuid = uuid3,
        firstName = "Tom",
        lastName = "Test",
        studentId = "12312312",
        courseCode = "CASE3",
        moduleCodes = moduleCodesCASE3
    )

    @Test
    fun `Create a user, get it and del it`() {
        studentUserTestInstance.createStudentUser(user1)
        val response: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertEquals(user1, response)

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)
    }

    @Test
    fun `Create a user, get it by student id and del it`() {
        studentUserTestInstance.createStudentUser(user1)
        val response: StudentUser? = studentUserTestInstance.getStudentUser(user1.studentId)
        Assertions.assertEquals(user1, response)

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)
    }

    @Test
    fun `Create a user, update it, get it and del it`() {
        studentUserTestInstance.createStudentUser(user1)
        val response: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertEquals(user1, response)

        studentUserTestInstance.updateStudentUser(updateUser1)
        val updatedResponse: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertEquals(updateUser1, updatedResponse)

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)
    }

    @Test
    fun `Create two users, get them and del them`() {
        studentUserTestInstance.createStudentUser(user1)
        studentUserTestInstance.createStudentUser(user2)
        val response = studentUserTestInstance.getAllStudentUsers()
        Assertions.assertEquals(user1, response[0])
        Assertions.assertEquals(user2, response[1])

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)

        studentUserTestInstance.deleteStudentUser(uuid2)
        val delUser2: StudentUser? = studentUserTestInstance.getStudentUser(uuid2)
        Assertions.assertNull(delUser2)
    }

    @Test
    fun `Create three users, get number of students in two modules`() {
        studentUserTestInstance.createStudentUser(user1)
        studentUserTestInstance.createStudentUser(user2)
        studentUserTestInstance.createStudentUser(user3)
        val response = studentUserTestInstance.getAllStudentUsers()
        Assertions.assertEquals(user1, response[0])
        Assertions.assertEquals(user2, response[1])
        Assertions.assertEquals(user3, response[2])

        val numOfUserInModule =
            studentUserTestInstance.getNumberOfStudentUsersInModule("CA4004")
        Assertions.assertEquals(1, numOfUserInModule)

        val numOfUserInModule2 =
            studentUserTestInstance.getNumberOfStudentUsersInModule("CA3004")
        Assertions.assertEquals(2, numOfUserInModule2)

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)

        studentUserTestInstance.deleteStudentUser(uuid2)
        val delUser2: StudentUser? = studentUserTestInstance.getStudentUser(uuid2)
        Assertions.assertNull(delUser2)

        studentUserTestInstance.deleteStudentUser(uuid3)
        val delUser3: StudentUser? = studentUserTestInstance.getStudentUser(uuid3)
        Assertions.assertNull(delUser3)
    }

    @Test
    fun `Create three users, get all students in two modules`() {
        studentUserTestInstance.createStudentUser(user1)
        studentUserTestInstance.createStudentUser(user2)
        studentUserTestInstance.createStudentUser(user3)
        val response = studentUserTestInstance.getAllStudentUsers()
        Assertions.assertEquals(user1, response[0])
        Assertions.assertEquals(user2, response[1])
        Assertions.assertEquals(user3, response[2])

        val allUsersInModule =
            studentUserTestInstance.getAllStudentUsersInModule("CA4004")
        Assertions.assertEquals(listOf(user1), allUsersInModule)

        val allUsersInModule2 =
            studentUserTestInstance.getAllStudentUsersInModule("CA3004")
        Assertions.assertEquals(listOf(user2, user3), allUsersInModule2)

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)

        studentUserTestInstance.deleteStudentUser(uuid2)
        val delUser2: StudentUser? = studentUserTestInstance.getStudentUser(uuid2)
        Assertions.assertNull(delUser2)

        studentUserTestInstance.deleteStudentUser(uuid3)
        val delUser3: StudentUser? = studentUserTestInstance.getStudentUser(uuid3)
        Assertions.assertNull(delUser3)
    }

    @Test
    fun `Create user, add module to student`() {
        studentUserTestInstance.createStudentUser(user1)
        val response: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertEquals(user1, response)

        studentUserTestInstance.addModuleToStudent("TestModule", user1.uuid)
        val updatedResponse: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNotNull(updatedResponse)

        if(updatedResponse != null) {
            Assertions.assertTrue(updatedResponse.moduleCodes.contains("TestModule"))
        }

        studentUserTestInstance.deleteStudentUser(uuid)
        val delUser: StudentUser? = studentUserTestInstance.getStudentUser(uuid)
        Assertions.assertNull(delUser)
    }
}
