package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import java.sql.Date
import scala.concurrent.{Await, ExecutionContext, Future}


/**
  * Helper for pagination.
  */
case class Page[A](items: Seq[A], pageIndex: Int, pageOffset: Int, total: Int) {
  lazy val prev = Option(pageIndex - 1).filter(_ >= 0)
  lazy val next = Option(pageIndex + 1).filter(_ => (pageOffset + items.size) < total)
}

/**
 * A repository for Books.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class BookRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  type BookData = (Long, String, Date, Option[Long])

  def constructBook: BookData => Book = {
    case (id, name, publishDate, memberId) =>
      Book(id, name, publishDate, memberId)
  }

  def constructBookWithMember: (Long, String, Date, Option[Long], Option[String]) => Book = {
    case (id, name, publishDate, memberId, member) =>
      Book(id, name, publishDate, memberId, member)
  }

  private class BookTable(tag: Tag) extends Table[Book](tag, "book") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def publishDate = column[Date]("publish_date")

    def memberId = column[Option[Long]]("member_id")

    def member = foreignKey("member_fk",memberId,members)(_.id)

    def extractBook: PartialFunction[Book, BookData] = {
      case Book(id, name, publishDate, memberId,_,_) =>
        (id, name, publishDate, memberId)
    }

    def * = (id, name, publishDate, memberId) <> (constructBook, extractBook.lift)

  }

  /**
   * The starting point for all queries on the book table.
   */
  private val books = TableQuery[BookTable]

  /**
   * List all the books in the database.
   */
  def list(): Future[Seq[Book]] = db.run {
    books.result
  }

  def totalBooks(filter:String): Future[Int] = db.run {
    books.filter(_.name like filter).length.result
  }

  def getBooks(pageSize: Int = 10, pageOffset: Int = 0, orderBy: Int = 1, filter: String = "%"): Future[Seq[Book]] = db.run {
    val filterQuery=books.filter(_.name like filter)

    val jointWithMember = for {
      (b, m) <- filterQuery joinLeft members on (_.memberId === _.id)
    } yield (b.id, b.name, b.publishDate, b.memberId, m.map(v=> v.name))

    val sortedQuery = orderBy match {
      case 1 => jointWithMember.sortBy(_._2) // sort by book name
      case 2 => jointWithMember.sortBy(_._3) // sort by book publish date
      case 3 => jointWithMember.sortBy(_._5) // sort by member name
      case _ => jointWithMember
    }

    sortedQuery.drop(pageOffset).take(pageSize).result.map(records=> records.map( record=> constructBookWithMember(record._1,record._2, record._3, record._4,record._5)))
  }

  def getBookAuthors(bookId: Long): Future[Seq[Author]] =
    db.run {
      val innerJoin = for {
        (ab, a) <- authorBooks join authors on (_.authorId === _.id)
      } yield (a, ab.bookId)
      innerJoin.filter(_._2 === bookId).sortBy(_._1.name).map(_._1).result
    }


private class MemberTable(tag: Tag) extends Table[Member](tag, "member") {

 /** The ID column, which is the primary key, and auto incremented */
 def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

 def name = column[String]("name")

 def * = (id, name) <> ((Member.apply _).tupled, Member.unapply)
}

/**
 * The starting point for all queries on the members table.
 */
private lazy val members = TableQuery[MemberTable]

private class AuthorBookTable (tag: Tag) extends Table[AuthorBook](tag, "author_book") {

 /** The ID column, which is the primary key, and auto incremented */
 def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

 def authorId = column[Long]("author_id")

 def bookId = column[Long]("book_id")

  def memberId = column[Option[Long]]("member_id")

  def author = foreignKey("author_fk",authorId,authors)(_.id)

  def book = foreignKey("book_fk",bookId,books)(_.id)

 def * = (id, authorId, bookId) <> ((AuthorBook.apply _).tupled, AuthorBook.unapply)
}

/**
 * The starting point for all queries on the author_book table.
 */
private lazy val authorBooks = TableQuery[AuthorBookTable]

private class AuthorTable (tag: Tag) extends Table[Author](tag, "author") {

 /** The ID column, which is the primary key, and auto incremented */
 def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

 def name = column[String]("name")

 def * = (id, name) <> ((Author.apply _).tupled, Author.unapply)
}

/**
 * The starting point for all queries on the author table.
 */
private lazy val authors = TableQuery[AuthorTable]

}
