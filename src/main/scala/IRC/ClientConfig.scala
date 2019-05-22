package IRC

import akka.actor.ActorRef
import org.pircbotx.Configuration
import org.pircbotx.cap.EnableCapHandler

import scala.util.Random

case class OauthTokenNotSet(message: String) extends Exception(message)

case class ClientConfig(private var username: Option[String], private val oauthToken: Option[String]) {

  username match {
    case Some(_) =>
      oauthToken match {
        case Some(_) =>
        case None => throw OauthTokenNotSet("Oauth token not set")
      }
    case None =>
  }

  import ClientConfig._
  def this() = this(None, None)

  def apply(): ClientConfig = new ClientConfig()

  def getTwitchUsername: String = {
    username match {
      case Some(x) => x
      case None =>
        val rnd = new Random()
        "justinfan" + (1 + rnd.nextInt( (5000 - 1) + 1)).toString
    }
  }

  def toPircBotXConfig(actorRef: ActorRef): Configuration = {
    new Configuration.Builder()
      .setAutoNickChange(false)
      .setOnJoinWhoEnabled(false)
      .setAutoReconnect(true)
      .setCapEnabled(true)
      .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
      .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
      .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
      .setName(getTwitchUsername)
      .setRealName(getTwitchUsername)
      .addServer("irc.twitch.tv")
      .addListener(new InitializationListener(actorRef))
      .authenticate(username, oauthToken)
      .buildConfiguration()
  }

}

object ClientConfig {
  implicit class ConfigurationExtra(val configurationBuilder: Configuration.Builder) {
    def authenticate(username: Option[String], oauthToken: Option[String]): Configuration.Builder = {
      username match {
        case Some(value) =>
          oauthToken match {
            case Some(token) => configurationBuilder.setServerPassword(token)
            case None => throw OauthTokenNotSet("Oauth token not set")
          }
        case None =>
          configurationBuilder
      }
    }
  }
}