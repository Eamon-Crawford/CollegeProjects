package dao

import daoUtils.LectureDaoUtils.listToString
import daoUtils.LectureDaoUtils.stringToList
import daoUtils.StudentUserDaoUtils.studentsInModule
import dataClasses.StudentUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import table.StudentUsersTable
import java.io.Closeable
import java.util.*


class StudentUserDao(private val db: Database) : StudentUserDAOInterface {

    override fun init() = transaction(db) {
        SchemaUtils.create(StudentUsersTable)
    }

    override fun createStudentUser(studentUser : StudentUser) = transaction(db){
        val moduleCodesString = listToString(studentUser.moduleCodes)// kotlin exposed doesn't support array types even though postgresql does, removing [ ] from string
        StudentUsersTable.insert {
            it[uuid] = studentUser.uuid
            it[firstName] = studentUser.firstName
            it[lastName] = studentUser.lastName
            it[studentId] = studentUser.studentId
            it[courseCode] = studentUser.courseCode
            it[moduleCodes] = moduleCodesString
        }
        Unit
    }

    override fun updateStudentUser(studentUser: StudentUser) = transaction(db){
        val moduleCodesString = listToString(studentUser.moduleCodes)// kotlin exposed doesn't support array types even though postgresql does, removing [ ] from string
        StudentUsersTable.update({ StudentUsersTable.uuid eq studentUser.uuid }) {
            it[firstName] = studentUser.firstName
            it[lastName] = studentUser.lastName
            it[studentId] = studentUser.studentId
            it[courseCode] = studentUser.courseCode
            it[moduleCodes] = moduleCodesString
        }
        Unit
    }

    override fun deleteStudentUser(uuid: UUID) = transaction(db){
        StudentUsersTable.deleteWhere { StudentUsersTable.uuid eq uuid }
        Unit
    }

    override fun getStudentUser(uuid: UUID): StudentUser? = transaction(db){
        StudentUsersTable.select { StudentUsersTable.uuid eq uuid }.map{
            StudentUser(
                    it[StudentUsersTable.uuid],
                    it[StudentUsersTable.firstName],
                    it[StudentUsersTable.lastName],
                    it[StudentUsersTable.studentId],
                    it[StudentUsersTable.courseCode],
                    stringToList(it[StudentUsersTable.moduleCodes]), // kotlin exposed doesnt support array types even though postgresql does, using a string that we will split instead
            )
        }.singleOrNull()
    }

    override fun getStudentUser(studentId : String): StudentUser? = transaction(db){
        StudentUsersTable.select { StudentUsersTable.studentId eq studentId }.map{
            StudentUser(
                    it[StudentUsersTable.uuid],
                    it[StudentUsersTable.firstName],
                    it[StudentUsersTable.lastName],
                    it[StudentUsersTable.studentId],
                    it[StudentUsersTable.courseCode],
                    stringToList(it[StudentUsersTable.moduleCodes]), // kotlin exposed doesnt support array types even though postgresql does, using a string that we will split instead
            )
        }.singleOrNull()
    }

    override fun getNumberOfStudentUsersInModule(moduleCode: String): Int = transaction(db){
        val studentUserList = StudentUsersTable.selectAll().map{
            StudentUser(
                    it[StudentUsersTable.uuid],
                    it[StudentUsersTable.firstName],
                    it[StudentUsersTable.lastName],
                    it[StudentUsersTable.studentId],
                    it[StudentUsersTable.courseCode],
                    stringToList(it[StudentUsersTable.moduleCodes]), // kotlin exposed doesn't support array types even though postgresql does, using a string that we will split instead
            )
        }.toList()

        return@transaction studentsInModule(moduleCode, studentUserList).size
    }

    override fun getAllStudentUsersInModule(moduleCode: String): List<StudentUser> = transaction(db){
        val studentUserList = StudentUsersTable.selectAll().map{
            StudentUser(
                    it[StudentUsersTable.uuid],
                    it[StudentUsersTable.firstName],
                    it[StudentUsersTable.lastName],
                    it[StudentUsersTable.studentId],
                    it[StudentUsersTable.courseCode],
                    stringToList(it[StudentUsersTable.moduleCodes]), // kotlin exposed doesn't support array types even though postgresql does, using a string that we will split instead
            )
        }.toList()

        return@transaction studentsInModule(moduleCode, studentUserList)
    }

    override fun getAllStudentUsers(): List<StudentUser> = transaction(db){
        StudentUsersTable.selectAll().map {
            StudentUser(
                it[StudentUsersTable.uuid],
                it[StudentUsersTable.firstName],
                it[StudentUsersTable.lastName],
                it[StudentUsersTable.studentId],
                it[StudentUsersTable.courseCode],
                stringToList(it[StudentUsersTable.moduleCodes]), // kotlin exposed doesn't support array types even though postgresql does, using a string that we will split instead
            )
        }.toList()
    }

    override fun addModuleToStudent(module: String, uuid: UUID) = transaction(db){
        val student = getStudentUser(uuid)
        val mutableList: MutableList<String> = student?.moduleCodes as MutableList<String>
        mutableList.add(module)
        val moduleCodesString = listToString(mutableList)
        StudentUsersTable.update({ StudentUsersTable.uuid eq uuid }) {
            it[moduleCodes] = moduleCodesString
        }
        Unit
    }

    override fun close() {}
}

interface StudentUserDAOInterface : Closeable {
    fun init()
    fun createStudentUser(studentUser: StudentUser)
    fun updateStudentUser(studentUser: StudentUser)
    fun deleteStudentUser(uuid: UUID)
    fun getStudentUser(uuid: UUID) : StudentUser?
    fun getStudentUser(studentId: String) : StudentUser?
    fun getNumberOfStudentUsersInModule(moduleCode: String) : Int
    fun getAllStudentUsersInModule(moduleCode: String) : List<StudentUser>
    fun getAllStudentUsers() : List<StudentUser>
    fun addModuleToStudent(module: String, uuid: UUID)
}