package IRC

import IRC.Messages.{Connected, Disconnected, Message, Quit}
import akka.actor.ActorRef
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.{ConnectEvent, DisconnectEvent, MessageEvent, QuitEvent}

class InitializationListener(actor: ActorRef) extends ListenerAdapter {
  override def onConnect(event: ConnectEvent): Unit = {
    actor ! Connected
  }

  override def onDisconnect(event: DisconnectEvent): Unit = {
    actor ! Disconnected
  }

  override def onQuit(event: QuitEvent): Unit = {
    actor ! Quit
  }

  override def onMessage(event: MessageEvent): Unit = {
    actor ! Message(event.getMessage, event.getUser.getNick)
  }
}
