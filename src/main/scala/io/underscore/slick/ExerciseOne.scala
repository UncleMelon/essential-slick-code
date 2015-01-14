package io.underscore.slick

import java.sql.Timestamp
import java.util.Properties

import scala.slick.driver.SQLiteDriver.simple._


object ExerciseOne extends App {

  final case class Message(id: Long,from: String, content: String, when: Timestamp)

  final class MessageTable(tag: Tag) extends Table[Message](tag, "message") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def sender = column[String]("sender")  
    def content = column[String]("content")
    def ts = column[Timestamp]("ts")
    def * = (id,sender, content, ts) <> (Message.tupled, Message.unapply)
  }

  lazy val messages = TableQuery[MessageTable]

  
  val props = new Properties()
  props.setProperty("date_precision","SECONDS")  
  
  Database.forURL("jdbc:sqlite:essential-slick.db", user = "essential", password = "trustno1", driver = "org.sqlite.JDBC", prop = props) withSession {
    implicit session ⇒

      //Define a query 
      val query = for {  
        message ← messages
        if message.sender === "HAL"
      } yield message

      //Execute a query.
      val messages_from_HAL: List[Message] = query.list

      println(s" ${messages_from_HAL}")
  }

}