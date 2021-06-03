package daoUtils

import ModuleDaoInstance
import dataClasses.Module

object ModuleDaoUtils {
    fun getExpectedAttendance(moduleCode: String): List<String> {
        val module: Module? = ModuleDaoInstance.getModule(moduleCode)
        return module?.regStudentIds ?: listOf()
    }

    fun getExpectedAttendanceNumber(moduleCode: String): Int {
        val module: Module? = ModuleDaoInstance.getModule(moduleCode)
        return module?.regStudentIds?.size ?: listOf<String>().size
    }
}