package code
package comet

/**
 * Simple case class to encapsulate a message
 */

case class ChatMessage(user: String, message: String)
case class LeaderboardMessage(user: String, correctGuesses: Int, incorrectGuesses: Int)
case class LeaderboardCorrect(user: String)
case class LeaderboardIncorrect(user: String)

case class TriviaPing()

sealed trait UserMessageType
case class GiveUp(message: String) extends UserMessageType
case class Guess(message: String) extends UserMessageType
case class Garbage(message: String) extends UserMessageType
case class Greeting(origin: String, message: String) extends UserMessageType
