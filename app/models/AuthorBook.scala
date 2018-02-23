package models

import play.api.libs.json._

case class AuthorBook(id: Long, authorId: Long, bookId: Long)

object AuthorBook {
  implicit val authorBookFormat = Json.format[AuthorBook]
}
