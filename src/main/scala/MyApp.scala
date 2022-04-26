package example.zio

import zio._

import java.io.IOException
import java.time.OffsetDateTime

object MyApp extends App {

  def printSomething(s: Serializable): IO[IOException, Unit] =
    Console.print(s.toString)

  val getCurrentDateTime: UIO[OffsetDateTime] =
    Clock.currentDateTime

  val getRandomNumber =
    Random.nextInt

  val program = for {
    dateTime <- getCurrentDateTime
    randomNumber <- getRandomNumber
    _ <- printSomething(dateTime.plusDays(randomNumber))
  } yield {}

  val runtime = Runtime.default
  runtime.unsafeRunSync(program)
}
