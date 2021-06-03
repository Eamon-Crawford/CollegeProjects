package dataClasses

import org.joda.time.DateTime
import java.util.*

data class Lecture(
        val uuid: UUID,
        val courseCode: String,
        val moduleCode: String,
        val date: DateTime,
        val endTime: DateTime,
        val location: String,
        val type: String,
        val expectedAttendanceNumber: Int,
        val actualAttendanceNumber: Int,
        val expectedAttendance: List<String>,
        var actualAttendance: List<String>
) {
        override fun equals(other: Any?): Boolean{
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as Lecture

                if (!date.isEqual(other.date)) return false
                if (!endTime.isEqual(other.endTime)) return false

                return true
        }

        override fun hashCode(): Int{
                return this.hashCode()
        }
}
