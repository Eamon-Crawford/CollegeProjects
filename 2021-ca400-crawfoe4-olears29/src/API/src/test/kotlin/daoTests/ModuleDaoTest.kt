package daoTests

import dao.ModuleDao
import dataClasses.Module
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModuleDaoTest {
    private val testDb = Database.connect("jdbc:postgresql://localhost:5432/test_db", driver = "org.postgresql.Driver",
        user = "postgres", password = "postMondo360")

    private val moduleDaoTestInstance = ModuleDao(testDb)

    @BeforeAll
    fun bootstrap() {
        moduleDaoTestInstance.init()
    }



    private val uuid = UUID.fromString("ee6ee294-b403-4f69-98af-6984705594ee")
    private val uuid2 = UUID.fromString("dd9dd294-b403-4f69-98af-6984705594ed")
    private val regStudentIds: List<String> = listOf("11231231", "21231232", "31231233", "41231234")
    private val moduleObject = Module(
        uuid = uuid,
        moduleCode = "CA4004",
        moduleTitle = "Soft. Eng.:Process,Principles & Methods",
        regStudentIds = regStudentIds,
        expectedAttendanceNumber = 4
    )

    private val updatedModuleObject = Module(
        uuid = uuid,
        moduleCode = "CA4003",
        moduleTitle = "Compilers",
        regStudentIds = regStudentIds,
        expectedAttendanceNumber = 4
    )

    private val moduleObject2 = Module(
        uuid = uuid2,
        moduleCode = "CA4005",
        moduleTitle = "Cryptography and Security Protocols",
        regStudentIds = regStudentIds,
        expectedAttendanceNumber = 4
    )

    @Test
    fun `Create a module, get it and del it`() {
        moduleDaoTestInstance.createModule(moduleObject)
        val response: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertEquals(moduleObject, response)
        moduleDaoTestInstance.deleteModule(uuid)
        val delModule: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertNull(delModule)
    }

    @Test
    fun `Create a module, get it by module code and del it`() {
        moduleDaoTestInstance.createModule(moduleObject)
        val response: Module? = moduleDaoTestInstance.getModule(moduleObject.moduleCode)
        Assertions.assertEquals(moduleObject, response)
        moduleDaoTestInstance.deleteModule(uuid)
        val delModule: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertNull(delModule)
    }

    @Test
    fun `Create a module, update it, get it and del it`() {
        moduleDaoTestInstance.createModule(moduleObject)
        val response = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertEquals(moduleObject, response)
        moduleDaoTestInstance.updateModule(updatedModuleObject)
        val updatedResponse = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertEquals(updatedModuleObject, updatedResponse)
        moduleDaoTestInstance.deleteModule(uuid)
        val delModule: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertNull(delModule)
    }

    @Test
    fun `Create two modules, get them and del them`() {
        moduleDaoTestInstance.createModule(moduleObject)
        moduleDaoTestInstance.createModule(moduleObject2)
        val responses = moduleDaoTestInstance.getAllModules()
        Assertions.assertEquals(moduleObject, responses[0])
        Assertions.assertEquals(moduleObject2, responses[1])

        moduleDaoTestInstance.deleteModule(responses[0].uuid)
        moduleDaoTestInstance.deleteModule(responses[1].uuid)
        val delModule: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertNull(delModule)
        val delModule2: Module? = moduleDaoTestInstance.getModule(uuid2)
        Assertions.assertNull(delModule2)
    }

    @Test
    fun `Register student for module Successfully`() {
        moduleDaoTestInstance.createModule(moduleObject)
        moduleDaoTestInstance.regStudentForModule("CA4004", "20020020")
        val response = moduleDaoTestInstance.getModule("CA4004")
        Assertions.assertNotNull(response)
        if (response != null) {
            Assertions.assertTrue(response.regStudentIds.contains("20020020"))
        }
        moduleDaoTestInstance.deleteModule(uuid)
        val delModule: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertNull(delModule)
    }

    @Test
    fun `Register student for module Failure module null`() {
        assertFailsWith<Exception>("Module Not Found: Student wasn't registered") {
            moduleDaoTestInstance.regStudentForModule("NotARealCode", "20020020")
        }
    }

    @Test
    fun `Register student for module Failure already registered`() {
        moduleDaoTestInstance.createModule(moduleObject)
        assertFailsWith<Exception>("Student already registered") {
            moduleDaoTestInstance.regStudentForModule("CA4004", "11231231")
        }
        moduleDaoTestInstance.deleteModule(uuid)
        val delModule: Module? = moduleDaoTestInstance.getModule(uuid)
        Assertions.assertNull(delModule)
    }
}
