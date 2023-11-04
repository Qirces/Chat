import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.sql.DriverManager

fun main() {
    ChatDb().makeTable()
}

class ChatDb {
    init {
        try {
            Database.connect(
                {
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/chat", "postgres", "qirces")
                }
            )
        } catch (e: Exception) {
            println(e)
        }
    }

    fun checkUser(login: String, pass: String): Boolean {
        return transaction {
            return@transaction User.find { (Users.login eq login) and (Users.password eq pass) }.count() == 1
        }
    }

    fun isLoginFree(login: String): Boolean {
        return transaction {
            return@transaction User.find { Users.login eq login }.count() == 0
        }
    }

    fun addUser(login: String, password: String) {
        transaction {
            addLogger(StdOutSqlLogger)
            User.new {
                this.login = login
                this.password = password
            }
        }
    }

    fun addHistory(login: String, time : String, text: String) {
        transaction{
            addLogger(StdOutSqlLogger)
            History.new{
                this.time = time
                this.login = login
                this.text = text
            }
        }
    }

    fun addHistory2(login: String, login2 : String, time : String, text: String) {
        transaction{
            addLogger(StdOutSqlLogger)
            History2.new{
                this.time = time
                this.sender = login
                this.destination = login2
                this.text = text
            }
        }
    }

    fun makeTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(Users, Histories)
        }
    }

    class User(id: EntityID<Int>) : Entity<Int>(id) {
        companion object : EntityClass<Int, User>(Users)

        var login by Users.login
        var password by Users.password
    }

    object Users : IntIdTable("id") {
        val login = varchar("login", 50).primaryKey()
        val password = varchar("password", 50)
    }

    class History(id: EntityID<Int>) : Entity<Int>(id) {
        companion object : EntityClass<Int, History>(Histories)

        var time by Histories.time
        var login by Histories.login
        var text by Histories.text
    }
    
    object Histories : IntIdTable("history") {
        val time = varchar("time",30)
        val login = varchar("login", 50)
        val text = varchar("text",200)
    }
    class History2(id: EntityID<Int>) : Entity<Int>(id) {
        companion object : EntityClass<Int, History2>(Histories2)

        var time by Histories2.time
        var sender by Histories2.sender
        var destination by Histories2.destination
        var text by Histories2.text
    }

    object Histories2 : IntIdTable("history2") {
        val time = varchar("time",30)
        val sender = varchar("login", 50)
        val destination = varchar("login", 50)
        val text = varchar("text",200)
    }

}