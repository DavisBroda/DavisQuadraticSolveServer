package davis

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Future

/**
 * Entry point into the app. Starts a server that listens at localhost:8080/quad, and returns the real number
 * solutions of a quadratic equation provided in format [-]ax&#94;2[ +|- bx][ +|- c] = 0.
 * <br><br>
 * Example strings: <br>
 * -x&#94;2 + 3 + 1 = 0<br>
 * 2x&#94;2 - 1 = 0
 * */
object QuadraticApp extends App {

  // Execution starts here
  QuadraticAppImpl.startServer()

}

/**
 * Actual implementation of server functionality. Moved here as delayed initialization in app causes
 * unit tests to not work properly on QuadraticApp itself
 * */
object QuadraticAppImpl{
  implicit val system = ActorSystem(Behaviors.empty, "my-system")

  implicit val executionContext = system.executionContext


  // Listed on /quad endpoint, parse parameter as a quadratic equation, and return solutions
  private val route = path("quad") {
    get {
      parameters("str") { in =>
        val response = for {
          parsed <- StringParser.parseQuadratic(in)
          solved <- QuadraticSolver.solve(parsed._1, parsed._2, parsed._3)
        } yield {
          if (solved.isEmpty) {
            "quadratic had no real number solutions"
          } else {
            s"Quadratic had solutions ${solved.map(d => s"x = $d").mkString(", ")}"
          }

        }

        response match {
          case Left(e) =>
            val ent = HttpEntity(ContentTypes.`text/plain(UTF-8)`, e.getMessage)
            complete(StatusCodes.OK, ent)
          case Right(s) =>
            val ent = HttpEntity(ContentTypes.`text/plain(UTF-8)`, s)
            complete(StatusCodes.OK, ent)
        }
      }
    }

  }

  def startServer(): Future[Http.ServerBinding] = {
    Http()(system).newServerAt("localhost", 8080).bind(route)
  }

  def stopServer(f: Future[Http.ServerBinding]): Unit = {
    f.flatMap(_.unbind())
  }
}