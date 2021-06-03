package daoUtilsTest

import ModuleDaoInstance
import daoUtils.ModuleDaoUtils.getExpectedAttendance
import daoUtils.ModuleDaoUtils.getExpectedAttendanceNumber
import dataClasses.Module
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ModuleDaoUtilsTest {
    private val uuid = UUID.randomUUID()
    private val regStudentIds: List<String> = listOf("11231231", "21231232", "31231233", "41231234")
    val moduleObject = Module(
        uuid = uuid,
        moduleCode = "CA4004",
        moduleTitle = "Soft. Eng.:Process,Principles & Methods",
        regStudentIds = regStudentIds,
        expectedAttendanceNumber = 4
    )

    @Test
    fun testGetExpectedAttendance() {
        mockkObject(ModuleDaoInstance)
        every { ModuleDaoInstance.getModule("CA4004") } returns moduleObject

        val result = getExpectedAttendance("CA4004")
        assert(result.size == 4)
        assertEquals(regStudentIds, result)
    }

    @Test
    fun testGetExpectedAttendanceNumber() {
        mockkObject(ModuleDaoInstance)
        every { ModuleDaoInstance.getModule("CA4004") } returns moduleObject

        val result = getExpectedAttendanceNumber("CA4004")
        assertEquals(4, result)
    }
}