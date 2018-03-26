package controllers

  import javax.inject._

  import models._
  import play.api.data.{Form, FormError}
  import play.api.mvc._

  import scala.concurrent.{ExecutionContext, Future}
  import models.UserForm._

  class BookController @Inject()(repo: BookRepository,
                                 cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc){

  /**
    * redirect to the application home.
    */

  val Home = {
    Redirect(routes.BookController.getBooks(0, 1, ""))
  }

  def getFlashData(request: MessagesRequestHeader,keys:List[String]): List[(String, String)] = {
    var result: List[(String, String)] = List.empty
    for (key <- keys) {
      var value = request.flash.get(key)
      value match {
        case Some(message) => {
          result = result :+ (key, message)
        }
        case _ =>
      }
    }
    result
  }


  def index = Action {
    implicit request =>
      val flashData=getFlashData(request,List("success","failure"))
      if(flashData.length>0)
          Home.flashing(flashData :_*)
      else
          Home
  }

  def getBooks(pageIndex: Int, orderBy: Int, filter: String, userForm: Form[UserData]=loginForm) = Action.async { implicit request =>
    getBookPage(pageIndex, orderBy, filter, userForm).map( result=>
      Ok(views.html.book(result._1,result._2,result._3,result._4))
    )
  }

  def getBookPage(pageIndex: Int=0, orderBy: Int=1, filter: String="", userForm: Form[UserData]=loginForm) = {
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
            (Page(booksWithAuthors, pageIndex, pageOffset, totalSize), orderBy, filter, userForm)
          }
        }
    }
  }

  def loginPost = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        var errors = changeErrorToGlobal(formWithErrors.errors) ++ formWithErrors.globalErrors  // concatenation of global & non-global errors
        getBookPage(0, 1, "", addErrorToFormGlobal(formWithErrors.discardingErrors,errors)).map(result=>
          Ok(views.html.book(result._1,result._2,result._3,result._4))
        )
      },
      userData  => {
        Future{Redirect(controllers.routes.BookController.index).flashing("success" -> "logged in successfully")
          .withSession("username" -> userData.username)}
      })
  }

  def nonEmptyKey(a: Any) = a match {
    case error: FormError => !UserForm.isEmpty(error.key)
    case _ => false
  }

  def changeErrorToGlobal(errors:Seq[FormError]): Seq[FormError] ={
    val nonGlobalError=errors.filter(nonEmptyKey(_)) // excludes global error, i.e. errors with empty key
    for(error <- nonGlobalError)
      System.out.println("***********8 key="+error.key+"  message="+error.message)
    nonGlobalError.map(error=> error.copy(key=""))// make the errors global by setting the key empty
  }

  // adds all the errors back to the form
  def addErrorToFormGlobal[T](form: Form[T],errors:Seq[FormError]): Form[T] ={
    errors match {
      case Nil => form
      case error :: tail =>
        addErrorToFormGlobal(form.withError(error),tail)
    }
  }

  def check(username: String, password: String): Boolean = {
    username == "admin" && password == "12345"
  }
}
