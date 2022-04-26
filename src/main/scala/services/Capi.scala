package example.zio.services

import com.gu.contentapi.client.{ContentApiClient, GuardianContentClient}
import com.gu.contentapi.client.model.v1.{Content, Tag}
import zio.{ZIO, ZLayer}

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
  val layer: ZLayer[Config, Throwable, Capi] =
    ZLayer.fromZIO(
      for {
        config <- ZIO.service[Config]
        client = new GuardianContentClient(config.apiKey)
      } yield new Capi {

        override def searchForContent(query: String): ZIO[Any, Throwable, List[Content]] =
          ZIO.fromFuture { implicit ec =>
            val search = ContentApiClient.search.q(query)
            client.getResponse(search).map(_.results.toList)
          }

        override def searchForTags(query: String): ZIO[Any, Throwable, List[Tag]] =
          ZIO.fromFuture { implicit ec =>
            val search = ContentApiClient.tags.q(query)
            client.getResponse(search).map(_.results.toList)
          }
      })
}