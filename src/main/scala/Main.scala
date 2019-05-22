import IRC._
import akka.actor.{ActorSystem, Props}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Main extends App{
  var config = new ClientConfig()

  val actorSystem = ActorSystem("irc-bot")
  val client = actorSystem.actorOf(Props[ClientActor], "client")

  implicit val globalContext: ExecutionContextExecutor = ExecutionContext.global

  client ! Initialize(config)
  client ! JoinChannel("#thijs")
  client ! JoinChannel("#yogscast")
  client ! JoinChannel("#dakotaz")
}
