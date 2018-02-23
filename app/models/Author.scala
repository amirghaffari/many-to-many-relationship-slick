package models

import play.api.libs.json._

case class Author(id: Long, name: String)

object Author {
  implicit val authorFormat = Json.format[Author]
}
