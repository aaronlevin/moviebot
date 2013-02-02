package code
package comet

import net.liftweb._
import http._
import actor._

/** 
 * A signleton that provides chat features to all clients
 */
object ChatServer extends LiftActor with ListenerManager {
  private var msgs = Vector(ChatMessage("MovieBot", "I AM READY FOR MOVIE, YES"))

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
    case s: ChatMessage => 
      msgs :+= s; updateListeners()
  }
}
