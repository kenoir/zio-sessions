package example.zio

import zio._

import java.io.IOException
import java.time.OffsetDateTime

//object MyApp extends App {
//
//  def printSomething(s: Serializable): IO[IOException, Unit] =
//    Console.print(s.toString)
//
//  val getCurrentDateTime: UIO[OffsetDateTime] =
//    Clock.currentDateTime
//
//  val getRandomNumber =
//    Random.nextInt
//
//  val program = for {
//    dateTime <- getCurrentDateTime
//    randomNumber <- getRandomNumber
//    _ <- printSomething(dateTime.plusDays(randomNumber))
//  } yield {}
//
//  val runtime = Runtime.default
//  runtime.unsafeRunSync(program)
//}

// service definition

import com.gu.contentapi.client.{ContentApiClient, GuardianContentClient}
import com.gu.contentapi.client.model.v1.Content
import com.gu.contentapi.client.model.v1.Tag

trait Capi {
  def searchForContent(query: String): ZIO[Any, Throwable, List[Content]]
  def searchForTags(query: String): ZIO[Any, Throwable, List[Tag]]
}

object Capi {
  // Capability accessors
  def searchForContent(query: String): ZIO[Capi, Throwable, List[Content]] =
    ZIO.serviceWithZIO[Capi](_.searchForContent(query))
  def searchForTags(query: String): ZIO[Capi, Throwable, List[Tag]] =
    ZIO.serviceWithZIO[Capi](_.searchForTags(query))
}

object CapiLive {
  val layer: ZLayer[Any, Throwable, Capi] =
    ZLayer.succeed(new Capi {
      override def searchForContent(query: String): ZIO[Any, Throwable, List[Content]] =
        ZIO.succeed(Nil)

      override def searchForTags(query: String): ZIO[Any, Throwable, List[Tag]] =
        ZIO.succeed(Nil)
    })
}

object Main extends ZIOAppDefault {
  private val program =
    for {
      _ <- Console.print("Query: ")
      query <- Console.readLine
      results <- Capi.searchForContent(query)
      _ <- ZIO.foreachDiscard(results)(result => Console.printLine(s"${result.webTitle}"))
    } yield ()

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = program.provide(CapiLive.layer)
}

// Reading list
// - https://zio.dev/next/datatypes/