package models

import play.api.libs.json._
import java.sql.Date

case class Book(id: Long, name: String, publishDate: Date, memberId: Option[Long] = None, member: Option[String] = None, authors: Seq[Author]= Seq.empty)
{

  def updateAuthors(authors: Seq[Author]) = {
    this.copy(authors=authors)
  }
}

//a companion object for Book
object Book {
  implicit val bookFormat = Json.format[Book]
}
