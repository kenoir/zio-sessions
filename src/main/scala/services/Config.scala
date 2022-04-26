package example.zio.services

import zio._

case class Config(apiKey: String)

object ConfigLive {
  val load: ZIO[Any, RuntimeException, Config] =
    for {
      optApiKey <- zio.System.env("API_KEY")
      apiKey <- ZIO.fromOption(optApiKey).orElseFail(new IllegalArgumentException("No API_KEY in env"))
    } yield Config(apiKey)

  val layer: ZLayer[Any, Throwable, Config] =  ZLayer.fromZIO(load)
}
