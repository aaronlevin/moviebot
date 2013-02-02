package code
package comet

import net.liftweb._
import http._
import actor._
import scala.concurrent._
import ExecutionContext.Implicits.global


object MovieBotPinger {

  def ping: Future[Unit] = {
    future {
      while(true) {
        Thread.sleep(15000)
        MovieBot ! TriviaPing()
      }
    }
  }
}
