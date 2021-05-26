package requestObjectsTests

import dataClasses.StudentUser
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import requestObjects.StudentUserRequest
import requestObjects.toStudentUserObject
import java.util.*

class StudentUserRequestTest {
    private val uuid = UUID.randomUUID()
    private val moduleCodes: List<String> = listOf("CA4003", "CA4004", "CA4005", "CA4007","CA4009","CA4012",)
    private val studentUserRequest = StudentUserRequest(
        firstName = "John",
        lastName = "test",
        studentId = "12345678",
        courseCode = "CASE4",
        moduleCodes = moduleCodes
    )

    private val studentUserRequestAsAStudentUserObject = StudentUser(
        uuid = uuid,
        firstName = "John",
        lastName = "test",
        studentId = "12345678",
        courseCode = "CASE4",
        moduleCodes = moduleCodes
    )

    @Test
    fun testToStudentUserObject() {
        mockkStatic(UUID::class)
        every {UUID.randomUUID()} returns uuid

        val studentUserObject = studentUserRequest.toStudentUserObject()
        Assertions.assertEquals(studentUserRequestAsAStudentUserObject, studentUserObject)
    }
}