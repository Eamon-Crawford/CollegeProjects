package table

import org.jetbrains.exposed.sql.Table

object LectureTable : Table(){
    val uuid = uuid("uuid").primaryKey()
    val courseCode = text("course_code")
    val moduleCode = text("module_code")
    val date = datetime("date")
    val endTime = datetime("end_time")
    val location = text("location")
    val type = text("type")
    val expectedAttendanceNumber = integer("expected_attendance_number")
    val actualAttendanceNumber = integer("actual_attendance_number")
    val expectedAttendance = text("expected_attendance")
    val actualAttendance = text("actual_attendance")
}
