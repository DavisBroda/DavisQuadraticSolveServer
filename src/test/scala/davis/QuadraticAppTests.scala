package davis

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ResponseEntity.fromJava
import akka.http.scaladsl.model.{ContentTypes, HttpRequest}
import org.scalatest.flatspec.AnyFlatSpec

import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.stream._

class QuadraticAppTests extends AnyFlatSpec{

  implicit val system = QuadraticAppImpl.system
  implicit val executionContext = QuadraticAppImpl.executionContext
  implicit val materializer = SystemMaterializer

  it should "parse valid quadratic at quad endpoint" in {

    val f = QuadraticAppImpl.startServer()

    // x^2 -3x +2 = 0  gives roots x=1, x=2
    val rawParams = "x^2 -3x +2 = 0"
    val encoded = URLEncoder.encode(rawParams, Charset.forName("UTF-8"))

    val requestF = Http().singleRequest(HttpRequest(uri = s"http://localhost:8080/quad?str=$encoded"))

    Await.result(requestF, Duration.apply(5, TimeUnit.SECONDS))

    assert(requestF.isCompleted)
    val res = requestF.value.get.get.entity.toStrict(Duration(3, TimeUnit.SECONDS)).map(_.data.utf8String).value.get.get

    println(res)

    QuadraticAppImpl.stopServer(f)

    assert(res.contains("x = 2.0"))
    assert(res.contains("x = 1.0"))

  }

  it should "fail if str parameter not specified" in {

    val f = QuadraticAppImpl.startServer()

    val requestF = Http().singleRequest(HttpRequest(uri = s"http://localhost:8080/quad"))

    Await.result(requestF, Duration.apply(5, TimeUnit.SECONDS))

    assert(requestF.isCompleted)
    val res = requestF.value.get.get.entity.toStrict(Duration(3, TimeUnit.SECONDS)).map(_.data.utf8String).value.get.get

    QuadraticAppImpl.stopServer(f)

    assert(res.contains("missing required query parameter 'str'"))

  }

}
