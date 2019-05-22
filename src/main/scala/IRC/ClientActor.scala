package IRC

import IRC.Messages.{Connected, Disconnected, Message, Quit}
import akka.actor.{Actor, ActorRef, Stash}
import org.pircbotx.{Configuration, PircBotX}

case class Initialize(config: ClientConfig)
case object Stop

case class JoinChannel(channel: String)

class ClientActor extends Actor with Stash{
  import context._

  private var runnableClient: Option[RunnableClient] = None
  private var runnableClientThread: Option[Thread] = None

  override def receive: Receive = {
    case Initialize(config) =>
      initialize(config)
    case Connected =>
      System.out.println("CONNECTED!")
      become(initializedClient)
      unstashAll()
    case _ => stash()
  }

  def initializedClient: Receive = {
    case Stop =>
      System.out.println("GOT STOP")
      handleStop()
    case Disconnected =>
      handleStop()
    case Quit =>
      System.out.println("GOT QUIT")
      handleStop()
    case JoinChannel(channel) => joinChannel(channel)
    case Message(message, username) => System.out.println(username + ":" + message)
    case _ => stash()
  }

  private def initialize(config: ClientConfig): Unit = {
    val pircBotXClient: PircBotX = new PircBotX(config.toPircBotXConfig(self))
    val client = new RunnableClient(pircBotXClient)

    val thread = new Thread(client)

    runnableClient = Some(client)
    runnableClientThread = Some(thread)

    thread.start()
  }

  private def handleStop(): Unit = {
    stop()
    become(receive)
    unstashAll()
  }

  private def start() : Unit = {
    runnableClientThread match {
      case Some(thread) => thread.start()
      case None => throw new Exception("ClientActor not initialized")
    }
  }

  private def stop() : Unit = {
    runnableClient match {
      case Some(client) =>
        client.getClient.stopBotReconnect()
        client.getClient.sendIRC().quitServer()
        runnableClient = None
        runnableClientThread match {
          case Some(_) =>
            runnableClientThread = None
          case None => throw new Exception("Client thread not running")
        }
      case None => throw new Exception("Client not initialized")
    }
  }

  private def joinChannel(channel: String): Unit = {
    runnableClient match {
      case Some(client) => client.joinChannel(channel)
      case None => throw new Exception("Client not initialized")
    }
  }
}
