package dao

import StudentUserDaoInstance
import daoUtils.LectureDaoUtils.listToString
import daoUtils.LectureDaoUtils.stringToList
import dataClasses.Module
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import table.ModuleTable
import java.io.Closeable
import java.util.*

class ModuleDao(private val db: Database) : ModuleDAOInterface {

    override fun init() = transaction(db) {
        SchemaUtils.create(ModuleTable)
    }

    override fun createModule(module : Module) = transaction(db){
        val studentIdsString = listToString(module.regStudentIds)// kotlin exposed doesn't support array types even though postgresql does, removing [ ] from string
        ModuleTable.insert {
            it[uuid] = module.uuid
            it[moduleCode] = module.moduleCode
            it[moduleTitle] = module.moduleTitle
            it[regStudentIds] = studentIdsString
            it[expectedAttendanceNumber] = module.regStudentIds.size
        }
        Unit
    }

    override fun updateModule(module: Module) = transaction(db){
        val studentIdsString = listToString(module.regStudentIds)// kotlin exposed doesn't support array types even though postgresql does, removing [ ] from string
        ModuleTable.update({ ModuleTable.uuid eq module.uuid }) {
            it[moduleCode] = module.moduleCode
            it[moduleTitle] = module.moduleTitle
            it[regStudentIds] = studentIdsString
            it[expectedAttendanceNumber] = module.regStudentIds.size
        }
        Unit
    }

    override fun deleteModule(uuid: UUID) = transaction(db){
        ModuleTable.deleteWhere { ModuleTable.uuid eq uuid }
        Unit
    }

    override fun getModule(uuid: UUID): Module? = transaction(db){
        ModuleTable.select { ModuleTable.uuid eq uuid }.map{
            Module(
                it[ModuleTable.uuid],
                it[ModuleTable.moduleCode],
                it[ModuleTable.moduleTitle],
                stringToList(it[ModuleTable.regStudentIds]),
                it[ModuleTable.expectedAttendanceNumber]
            )
        }.singleOrNull()
    }

    override fun getModule(moduleCode : String): Module? = transaction(db){
        ModuleTable.select { ModuleTable.moduleCode eq moduleCode }.map{
            Module(
                it[ModuleTable.uuid],
                it[ModuleTable.moduleCode],
                it[ModuleTable.moduleTitle],
                stringToList(it[ModuleTable.regStudentIds]),
                it[ModuleTable.expectedAttendanceNumber]
            )
        }.singleOrNull()
    }

    override fun getAllModules(): List<Module> = transaction(db){
        ModuleTable.selectAll().map {
            Module(
                it[ModuleTable.uuid],
                it[ModuleTable.moduleCode],
                it[ModuleTable.moduleTitle],
                stringToList(it[ModuleTable.regStudentIds]),
                it[ModuleTable.expectedAttendanceNumber]
            )
        }.toList()
    }

    override fun regStudentForModule(givenModuleCode: String, studentId: String) = transaction(db){
        val module = getModule(givenModuleCode)
        if (module != null && !module.regStudentIds.contains(studentId)) {

            val mutableRegStudentsList : MutableList<String> = module.regStudentIds as MutableList<String>
            mutableRegStudentsList.add(studentId)
            val studentIdsString = listToString(mutableRegStudentsList)

            ModuleTable.update({ ModuleTable.uuid eq module.uuid }) {
                it[moduleCode] = module.moduleCode
                it[moduleTitle] = module.moduleTitle
                it[regStudentIds] = studentIdsString
                it[expectedAttendanceNumber] = module.regStudentIds.size
            }

            val student = StudentUserDaoInstance.getStudentUser(studentId)
            if (student?.moduleCodes?.contains(givenModuleCode) == false) {
                StudentUserDaoInstance.addModuleToStudent(givenModuleCode, student.uuid)
            }
        }else if(module?.regStudentIds?.contains(studentId) == true) {
            throw Exception("Student already registered")
        } else {
            throw Exception("Module Not Found: Student wasn't registered")
        }
    }

    override fun close() {}
}

interface ModuleDAOInterface : Closeable {
    fun init()
    fun createModule(module : Module)
    fun updateModule(module: Module)
    fun deleteModule(uuid: UUID)
    fun getModule(uuid: UUID) : Module?
    fun getModule(moduleCode: String) : Module?
    fun getAllModules() : List<Module>
    fun regStudentForModule(givenModuleCode: String, studentId: String)
}