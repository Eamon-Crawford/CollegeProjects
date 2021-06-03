package requestObjects

import dataClasses.Module
import java.util.*

data class ModulePutRequest(
    val uuid: UUID,
    val moduleCode: String,
    val moduleTitle: String,
    val regStudentIds: List<String>
)

fun ModulePutRequest.toModuleObject() = Module(
    uuid = uuid,
    moduleCode = moduleCode,
    moduleTitle = moduleTitle,
    regStudentIds = regStudentIds,
    expectedAttendanceNumber = regStudentIds.size
)
