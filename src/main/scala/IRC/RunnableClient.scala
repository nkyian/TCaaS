package IRC

import org.pircbotx.hooks.managers.ThreadedListenerManager
import org.pircbotx.{Configuration, PircBotX}

class RunnableClient(private var client: PircBotX) extends Runnable {

  def getClient: PircBotX = client

  def joinChannel(channel: String): Unit = {
    client.sendIRC().joinChannel(channel)
  }

  override def run(): Unit = {
    client.startBot()
  }
}