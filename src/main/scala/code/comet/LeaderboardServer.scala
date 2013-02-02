package code
package comet

import net.liftweb._
import http._
import actor._

/** 
 * A signleton that provides chat features to all clients
 */
object LeaderboardServer extends LiftActor with ListenerManager {
  private var msgs = Vector[LeaderboardMessage]()

  /** when we update the listeners, what messages do we send?
   * We send the messages, which is an immutable data structure, so it can be shared with lots of threads
   * with any danger or locking
   */
  def createUpdate = msgs

  /**
   * process messages that are sent to the actor.
   * In this case, we're looking for String that are sent to the ChatServer.
   * We append them to our vector of messages and then update all the listeners
   */
  override def lowPriority = {
    case LeaderboardIncorrect(user) =>
       msgs.partition { x => x.user == user } match {
        case (Vector(), theRest) => msgs :+= LeaderboardMessage(user, 0,1)
        case (matches, theRest) => msgs = theRest :+ matches.head.copy(incorrectGuesses = matches.head.incorrectGuesses + 1)
      }
      updateListeners()
    case LeaderboardCorrect(user) => 
      msgs.partition { x => x.user == user } match {
        case (Vector(), theRest) => msgs :+= LeaderboardMessage(user, 1,0)
        case (matches, theRest) => msgs = theRest :+ matches.head.copy(correctGuesses = matches.head.correctGuesses + 1)
      }
      updateListeners()
  }
}
