package example.zio

import example.zio.services._
import zio._

object Main extends ZIOAppDefault {
  private val program =
    for {
      _ <- Console.print("Query: ")
      query <- Console.readLine
      results <- Capi.searchForContent(query)
      _ <- ZIO.foreachDiscard(results)(result => Console.printLine(s"${result.webPublicationDate} - ${result.webTitle}"))
    } yield ()

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    program.forever.provide(CapiLive.layer, ConfigLive.layer)
}

// Reading list
// - https://zio.dev/next/datatypes/