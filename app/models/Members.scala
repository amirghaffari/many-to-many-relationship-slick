package models

import play.api.libs.json._

case class Member(id: Long, name: String)

object Member {
  implicit val bookFormat = Json.format[Member]
}
