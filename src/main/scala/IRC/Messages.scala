package IRC

object Messages {
  case object Connected
  case object Disconnected
  case object Quit
  case class Message(message: String, username: String)
}
