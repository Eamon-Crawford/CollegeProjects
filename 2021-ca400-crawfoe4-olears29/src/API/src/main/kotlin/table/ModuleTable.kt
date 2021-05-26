package table

import org.jetbrains.exposed.sql.Table

object ModuleTable: Table(){
    val uuid = uuid("uuid").primaryKey()
    val moduleCode = text("module_code").uniqueIndex()
    val moduleTitle = text("module_title")
    val regStudentIds = text("reg_student_ids")
    val expectedAttendanceNumber = integer("expected_attendance")
}
