
import dao.LectureDao
import dao.ModuleDao
import dao.StudentUserDao
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.joda.time.DateTimeZone
import routes.lectureRouting
import routes.modulesRouting
import routes.userRouting

private val database = Database.connect("jdbc:postgresql://localhost:5432/project_db", driver = "org.postgresql.Driver",
    user = "postgres", password = "postMondo360")
val LectureDaoInstance = LectureDao(database)
val ModuleDaoInstance = ModuleDao(database)
val StudentUserDaoInstance = StudentUserDao(database)



fun main() {
    DateTimeZone.setDefault(DateTimeZone.forID("Europe/Dublin"))
    val port = System.getenv("PORT")?.toInt() ?: 23567
    embeddedServer(Netty, port) {
        StudentUserDaoInstance.init()
        LectureDaoInstance.init()
        ModuleDaoInstance.init()
        install(CallLogging)
        install(ContentNegotiation) {
            jackson {}
        }
        routing {
            lectureRouting()
            userRouting()
            modulesRouting()

        }
    }.start(wait = true)
}

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module() {

}

