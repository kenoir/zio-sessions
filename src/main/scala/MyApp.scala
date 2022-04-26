package example.zio

import services._

import zio._

// Reading list
// - https://zio.dev/next/datatypes/

object Main extends ZIOAppDefault {
  private val program =
    for {
      _ <- Console.print("Query: ")
      query <- Console.readLine
      results <- Capi.searchForContent(query)
      _ <- ZIO.foreachDiscard(results)(result =>
        Console.printLine(s"${result.webTitle}\n${result.webUrl}\n"))
    } yield ()

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    program.forever.provide(CapiLive.layer, ConfigLive.layer)
}
