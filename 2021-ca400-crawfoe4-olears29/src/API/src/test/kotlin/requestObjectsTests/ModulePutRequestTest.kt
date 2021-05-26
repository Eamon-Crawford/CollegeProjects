package requestObjectsTests

import dataClasses.Module
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import requestObjects.ModulePutRequest
import requestObjects.toModuleObject
import java.util.*

class ModulePutRequestTest {
    private val uuid = UUID.randomUUID()
    private val regStudentIds: List<String> = listOf("11231231", "21231232", "31231233", "41231234")
    private val modulePutRequest = ModulePutRequest(
        uuid = uuid,
        moduleCode = "CA4004",
        moduleTitle = "Soft. Eng.:Process,Principles & Methods",
        regStudentIds = regStudentIds
    )

    private val modulePutRequestAsAModuleObject = Module(
        uuid = uuid,
        moduleCode = "CA4004",
        moduleTitle = "Soft. Eng.:Process,Principles & Methods",
        regStudentIds = regStudentIds,
        expectedAttendanceNumber = 4
    )

    @Test
    fun testToModuleObject() {
        val moduleObject = modulePutRequest.toModuleObject()
        Assertions.assertEquals(modulePutRequestAsAModuleObject, moduleObject)
    }
}