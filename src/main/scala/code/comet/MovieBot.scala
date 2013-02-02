package code
package comet

import net.liftweb._
import http._
import actor._

object MovieBot extends LiftActor with ListenerManager {

  def createUpdate = "cool"

  override def lowPriority = {
    case ChatMessage(user, msg) => Thread.sleep(1000); ChatServer ! ChatMessage("MovieBot", user + ": " + msg)
  }
}
