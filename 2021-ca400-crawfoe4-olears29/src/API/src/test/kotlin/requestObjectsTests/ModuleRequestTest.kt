package requestObjectsTests

import dataClasses.Module
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import requestObjects.ModuleRequest
import requestObjects.toModuleObject
import java.util.*

class ModuleRequestTest {
    private val uuid = UUID.randomUUID()
    private val regStudentIds: List<String> = listOf("11231231", "21231232", "31231233", "41231234")
    private val moduleRequest = ModuleRequest(
        moduleCode = "CA4004",
        moduleTitle = "Soft. Eng.:Process,Principles & Methods",
        regStudentIds = regStudentIds
    )

    private val moduleRequestAsAModuleObject = Module(
        uuid = uuid,
        moduleCode = "CA4004",
        moduleTitle = "Soft. Eng.:Process,Principles & Methods",
        regStudentIds = regStudentIds,
        expectedAttendanceNumber = 4
    )

    @Test
    fun testToModuleObject() {
        mockkStatic(UUID::class)
        every {UUID.randomUUID()} returns uuid

        val moduleObject = moduleRequest.toModuleObject()
        Assertions.assertEquals(moduleRequestAsAModuleObject, moduleObject)
    }
}