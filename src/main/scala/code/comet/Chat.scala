package code
package comet

import net.liftweb._
import http._
import util._
import Helpers._

class Chat extends CometActor with CometListener {
  // state
  private var msgs: Vector[ChatMessage] = Vector()

  /**
   * When component is insantiated, register as a listener with teh chat server
   */
  def registerWith = ChatServer

  /**
   * Process messages
   */
  override def lowPriority = {
    case v: Vector[ChatMessage] => msgs = v; reRender()
  }

  /** 
   * put the messages in the li elements and clear elements that have clearable
   */
  def render = "li *" #> msgs.map { x => x.user + ": " + x.message}  & ClearClearable
}
