package code
package comet

import net.liftweb._
import http._
import actor._
import scala.util.Random
import com.sortable.MovieStore
import scala.math.abs

object MovieBot extends LiftActor with ListenerManager {

  private val movieStore = new MovieStore()
  private val movieList: List[String] = movieStore.getIds.toList

  private val GiveUpRegex = """^([iI] don\'?t know|[iI] dunno|[iI] give up|[tT]ell [mM]e)(.*)""".r
  private val GuessRegex = """^([iI]s it)?(.*)""".r

  private var currentMovie: String = movieList.head
  private var currentTriviaList: List[String] = movieStore.getTriviaForMovieId(currentMovie).toList
  private var lastRequestTime = System.currentTimeMillis
  private var clueList: List[Int] = Nil

  private val r = new Random()

  private def getRandomMovie: String = {
    val id = movieList(abs(r.nextInt % movieList.length))
    id
  }

  private def timeSinceLastRequest = {
    System.currentTimeMillis - lastRequestTime
  }

  def createUpdate = "cool"

  // correctGuess
  private def correctGuess(message: String): Boolean = {
    movieStore.checkGuess(message)
  }

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
        case GiveUp(message) => {
          ChatServer ! ChatMessage("MovieBot", "If you say it, answers will come: %s".format(movieStore.getTitle))
          currentMovie = getRandomMovie
          currentTriviaList = movieStore.getTriviaForMovieId(currentMovie).toList
        }
        case Guess(message) => {
          if( correctGuess(message) ) {
            currentMovie = getRandomMovie
            currentTriviaList = movieStore.getTriviaForMovieId(currentMovie).toList
            ChatServer ! ChatMessage("MovieBot", "Nice one, %s! You're the person now, dawg.".format(user))
            LeaderboardServer ! LeaderboardCorrect(user)
          } else {
            ChatServer ! ChatMessage("MovieBot", "You really think it's %s, %s?".format(message, user))
            LeaderboardServer ! LeaderboardIncorrect(user)
          }
        }
        case _ => ChatServer ! ChatMessage("MovieBot", "ME FAIL TO PARSE MESSAGE")
      }
    }
    case TriviaPing() => {
      if (clueList.length == currentTriviaList.length) {
        clueList = Nil
      }
      val id = abs(r.nextInt % currentTriviaList.length)
      val trivia = 
        if( clueList.contains(id) ) {
          if( id > currentTriviaList.length - 2) {
            clueList = clueList ::: List(0)
            currentTriviaList(0)
          } else {
            clueList = clueList ::: List(id + 1)
            currentTriviaList(id + 1)
          }
        } else {
          clueList = clueList ::: List(id)
          currentTriviaList(id)
        }
      ChatServer ! ChatMessage("MovieBot", trivia)
    }
  }
}
