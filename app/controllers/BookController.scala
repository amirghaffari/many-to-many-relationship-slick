package controllers

import javax.inject._
import models._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

class BookController @Inject()(repo: BookRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /**
    * redirect to the application home.
    */
  val Home = Redirect(routes.BookController.getBooks(0, 1, ""))

  def index = Action {
    Home
  }

  def getBooks(pageIndex: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    val pageSize=3
    val pageOffset=pageSize*pageIndex
    val filterStr = ("%" + filter + "%")
    val totalSize=repo.totalBooks(filterStr)
    // flatMap converts Future[Future[Result]] to Future[Result]
    repo.getBooks(pageSize, pageOffset,orderBy = orderBy, filterStr).flatMap {
      books => /*books is Seq[Book]*/
      // Future.sequence is taking the Seq[Future[Book]] and turning it into a Future[Seq[Book]]
      Future.sequence(
        books.map { b =>
          repo.getBookAuthors(b.id).map(authors => b.updateAuthors(authors))
        }
      ).flatMap { booksWithAuthors =>
        totalSize.map{ totalSize=>
          Ok(views.html.book(Page(booksWithAuthors, pageIndex, pageOffset, totalSize), orderBy, filter))
        }
      }
    }
  }

}

