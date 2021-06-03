package routes

import ModuleDaoInstance
import daoUtils.StudentUserDaoUtils.addModuleToStudentUser
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import requestObjects.ModulePutRequest
import requestObjects.ModuleRequest
import requestObjects.toModuleObject
import java.util.*

fun Route.modulesRouting() {
    route("/modules") {
        get {
            call.respond(mapOf("modules" to ModuleDaoInstance.getAllModules()))
        }

        post {
            val moduleRequest = call.receive<ModuleRequest>()
            val moduleObject = moduleRequest.toModuleObject()
            ModuleDaoInstance.createModule(moduleObject)
            call.respond(200)
        }

        put {
            val module = call.receive<ModulePutRequest>().toModuleObject()
            ModuleDaoInstance.updateModule(module)
            call.respond(200)
        }

        delete("/{uuid}") {
            val strUuid: String? = call.parameters["uuid"]
            if (strUuid != null) {
                val uuid: UUID = UUID.fromString(call.parameters["uuid"])
                ModuleDaoInstance.deleteModule(uuid)
                call.respond(200)
            } else {
                call.respond(404)
            }
        }

        get("/{uuid}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            if (uuid != null) {
                val response = ModuleDaoInstance.getModule(uuid)
                if (response != null)
                    call.respond(response)
                else call.respond("No such user found!")
            }
        }

        get("/module-code/{moduleCode}") {
            val moduleCode  = call.parameters["moduleCode"]
            if (moduleCode != null) {
                val response = ModuleDaoInstance.getModule(moduleCode)
                if (response != null)
                    call.respond(response)
                else call.respond("No such user found!")
            }
        }

        get("/register/{moduleCode}/{studentId}") {
            try {
                val moduleCode  = call.parameters["moduleCode"]
                val studentId  = call.parameters["studentId"]
                if (moduleCode != null && studentId != null) {
                    val response = ModuleDaoInstance.regStudentForModule(moduleCode, studentId)
                    addModuleToStudentUser(studentId, moduleCode)
                    if (response != null)
                        call.respond(response)
                    else call.respond("No such user found!")
                }
            } catch(e: Exception) {
                println(e)
                call.respond(HttpStatusCode.BadRequest, e.toString())
            }
        }
    }
}