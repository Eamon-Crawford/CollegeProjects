package requestObjects

import dataClasses.Module
import java.util.*

data class ModuleRequest(
    val moduleCode: String,
    val moduleTitle: String,
    val regStudentIds: List<String>
)

fun ModuleRequest.toModuleObject() = Module(
    uuid = UUID.randomUUID(),
    moduleCode = moduleCode,
    moduleTitle = moduleTitle,
    regStudentIds = regStudentIds,
    expectedAttendanceNumber = regStudentIds.size
)
