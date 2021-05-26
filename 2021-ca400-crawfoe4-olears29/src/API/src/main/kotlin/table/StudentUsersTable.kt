package table

import org.jetbrains.exposed.sql.Table

object StudentUsersTable : Table(){
    val uuid = uuid("uuid").primaryKey()
    val firstName = text("first_name")
    val lastName = text("last_name")
    val studentId = text("student_id").uniqueIndex()
    val courseCode = text("course_code")
    val moduleCodes = text("module_codes") // kotlin exposed doesn't support array types even though postgresql does, using a string that we will split instead
}