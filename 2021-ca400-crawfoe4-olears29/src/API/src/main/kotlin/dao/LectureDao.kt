package dao

import daoUtils.LectureDaoUtils.getNextLecture
import daoUtils.LectureDaoUtils.listToString
import daoUtils.LectureDaoUtils.stringToList
import daoUtils.ModuleDaoUtils.getExpectedAttendance
import dataClasses.Lecture
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import table.LectureTable
import java.io.Closeable
import java.util.*


class LectureDao(private val db: Database) : LectureDAOInterface {

    override fun init() = transaction(db) {
        SchemaUtils.create(LectureTable)
    }

    override fun createLecture(lecture: Lecture) = transaction(db){
        val expectedAttendanceList = getExpectedAttendance(lecture.moduleCode)

        LectureTable.insert {
            it[uuid] = lecture.uuid
            it[courseCode] = lecture.courseCode
            it[moduleCode] = lecture.moduleCode
            it[date] = lecture.date
            it[endTime] = lecture.endTime
            it[location] = lecture.location
            it[type] = lecture.type
            it[expectedAttendanceNumber] = expectedAttendanceList.size
            it[actualAttendanceNumber] = 0
            it[expectedAttendance] = listToString(expectedAttendanceList)
            it[actualAttendance] = ""
        }
        Unit
    }

    override fun updateLecture(lecture: Lecture) = transaction(db){
        LectureTable.update({ LectureTable.uuid eq lecture.uuid }) {
            it[courseCode] = lecture.courseCode
            it[moduleCode] = lecture.moduleCode
            it[date] = lecture.date
            it[endTime] = lecture.endTime
            it[location] = lecture.location
            it[type] = lecture.type
            it[expectedAttendanceNumber] = lecture.expectedAttendanceNumber
            it[actualAttendanceNumber] = lecture.actualAttendanceNumber
            it[expectedAttendance] = listToString(lecture.expectedAttendance)
            it[actualAttendance] = listToString(lecture.actualAttendance)
        }
        Unit
    }

    override fun deleteLecture(uuid: UUID) = transaction(db){
        LectureTable.deleteWhere { LectureTable.uuid eq uuid }
        Unit
    }

    override fun getLecture(uuid: UUID): Lecture? = transaction(db){
        LectureTable.select { LectureTable.uuid eq uuid }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])

            )
        }.singleOrNull()
    }

    override fun getAllModuleLectures(moduleCode: String): List<Lecture> = transaction(db){
        LectureTable.select { LectureTable.moduleCode eq moduleCode }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }.sortedBy { it.date}
    }

    override fun getAllCourseLectures(courseCode: String): List<Lecture> = transaction(db){
        LectureTable.select { LectureTable.courseCode eq courseCode }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }.sortedBy { it.date}
    }

    override fun getAllLectures(): List<Lecture> = transaction(db){
        LectureTable.selectAll().map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }.sortedBy { it.date}
    }

    override fun getNextLectureForModule(moduleCode: String): Lecture? = transaction(db){
        val lectureList = LectureTable.select { LectureTable.moduleCode eq moduleCode }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }

        return@transaction getNextLecture(lectureList)
    }

    override fun getNextLectureForCourse(courseCode: String): Lecture? = transaction(db){
        val lectureList = LectureTable.select { LectureTable.courseCode eq courseCode }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }

        return@transaction getNextLecture(lectureList)
    }
    override fun getAllLecturesAtLocation(location: String): List<Lecture> = transaction(db){
       LectureTable.select { LectureTable.location eq location }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }.sortedBy { it.date}
    }
    override fun getLectureWithLocationAtNow(location: String): List<Lecture> = transaction(db){
        val lectureList: List<Lecture> = LectureTable.select { LectureTable.location eq location }.map{
            Lecture(
                it[LectureTable.uuid],
                it[LectureTable.courseCode],
                it[LectureTable.moduleCode],
                it[LectureTable.date],
                it[LectureTable.endTime],
                it[LectureTable.location],
                it[LectureTable.type],
                it[LectureTable.expectedAttendanceNumber],
                it[LectureTable.actualAttendanceNumber],
                stringToList(it[LectureTable.expectedAttendance]),
                stringToList(it[LectureTable.actualAttendance])
            )
        }
        val response = mutableListOf<Lecture>()
        for(lecture in lectureList) {
            if (lecture.date.minusMinutes(15).isBefore(DateTime.now())
                && lecture.endTime.isAfter(DateTime.now())) {
                response.add(lecture)
            }
        }
        return@transaction response
    }

    override fun updateLectureAttendance(uuid: UUID, attendanceList: List<String>) = transaction(db){
        LectureTable.update({ LectureTable.uuid eq uuid }) {
            it[actualAttendanceNumber] = attendanceList.size
            it[actualAttendance] = listToString(attendanceList)
        }
        Unit
    }

    override fun close() {}
}

interface LectureDAOInterface : Closeable {
    fun init()
    fun createLecture(lecture: Lecture)
    fun updateLecture(lecture: Lecture)
    fun deleteLecture(uuid: UUID)
    fun getLecture(uuid: UUID) : Lecture?
    fun getAllModuleLectures(moduleCode: String): List<Lecture>
    fun getAllCourseLectures(courseCode: String): List<Lecture>
    fun getAllLectures() : List<Lecture>
    fun getNextLectureForModule(moduleCode: String): Lecture?
    fun getNextLectureForCourse(courseCode: String): Lecture?
    fun getAllLecturesAtLocation(location: String): List<Lecture>
    fun getLectureWithLocationAtNow(location: String): List<Lecture>
    fun updateLectureAttendance(uuid: UUID, attendanceList: List<String>)

}

