package code
package comet

import net.liftweb._
import http._
import actor._
import scala.util.Random
import com.sortable.MovieStore

object MovieBot extends LiftActor with ListenerManager {

  private val movieStore = new MovieStore()
  private val movieList: List[String] = movieStore.getIds.toList

  private val GiveUpRegex = """^([iI] don\'?t know|[iI] dunno|[iI] give up|[tT]ell [mM]e)(.*)""".r
  private val GuessRegex = """^([iI]s it)?(.*)""".r

  private var currentMovie: String = movieList.head

  private val r = new Random()

  private def getRandomMovie: String = {
    val id = movieList(r.nextInt % movieList.length) 
    id
  }

  def createUpdate = "cool"

  // correctGuess
  private def correctGuess(message: String): Boolean = false

  private def messageClassifier(message: String): UserMessageType = {
    message match {
      case GiveUpRegex(giveup, theRest) => GiveUp(message)
      case GuessRegex(_, guess) => Guess(guess)
      case _ => Guess(message)
    }
  }

  override def lowPriority = {
    case ChatMessage(user, msg) => {
      Thread.sleep(1000) 
      messageClassifier(msg) match {
        case GiveUp(message) => ChatServer ! ChatMessage("MovieBot", "Ah, %s, don't give up buddy!".format(user))
        case Guess(message) => {
          if( correctGuess(message) ) {
            var currentMovie = getRandomMovie
            ChatServer ! ChatMessage("MovieBot", "Nice one, %s! You're the person now, dawg.")
          } else {
            ChatServer ! ChatMessage("MovieBot", "You really think it's %s, %s?".format(message, user))
          }
        }
        case _ => ChatServer ! ChatMessage("MovieBot", "ME FAIL TO PARSE MESSAGE")
      }
    }
    case TriviaPing() => ChatServer ! ChatMessage("MovieBot", "pinged")
  }
}
