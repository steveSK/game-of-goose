package scala.example.gog

import org.scalatest.{FlatSpec, Matchers}

import scala.example.gog.game.Game
import scala.example.gog.game.Game.MovedType.MovedType
import scala.example.gog.game.Game.{Move, MovedType, Player}

class GameTest extends FlatSpec with Matchers {

  val players = List(Player("test1"), Player("test2"))

  val createGame = () => Game.createGame(players)

  val moveShouldBeEqual = (move: Move, player: Player, startPos: Int, endPos: Int, moveType: MovedType) => {
    move.startPos shouldEqual startPos
    move.player shouldEqual player
    move.endPos shouldEqual endPos
    move.moveType shouldEqual moveType
  }


  it should "Create game" in {
    val game = createGame()
    game.players.size shouldEqual 2

  }

  it should "play Player which is on Turn" in {
    val game = createGame()

    val playerOnTurn = game._playerTurn

    val (player, moved) = game.play(playerOnTurn, 4)

    player shouldEqual players.filter(p => p !== playerOnTurn)(0)

    moveShouldBeEqual(moved(0), playerOnTurn, 0, 4, MovedType.NORMAL)

  }

  it should "throw an exception when Player which is not on Turn plays" in {
    val game = createGame()

    val playerOnTurn = game._playerTurn
    val playerNotOnTurn = players.filter(p => p !== playerOnTurn)(0)

    an[IllegalArgumentException] should be thrownBy {
      game.play(playerNotOnTurn, 5)
    }

  }

  it should "be finished when some player won" in {
    val game = createGame()

    val player1 = game._playerTurn
    val player2 = players.filter(p => p !== player1)(0)

    game.play(player1, 12) // player1 moves to 12
    game.play(player2, 11) // player2 moves to 11
    game.play(player1, 12) // player1 moves to 24
    game.play(player2, 10) // player2 moves to 22
    game.play(player1, 12) // player1 moves to 36
    game.play(player2, 12) // player2 moves to 34
    game.play(player1, 12) // player1 moves to 48
    game.play(player2, 12) // player2 moves to 44
    game.play(player1, 12) // player1 moves to 60
    game.play(player2, 12) // player2 moves to 58

    val lastMoves = game.play(player1, 3)._2  // player1 moves to 63 and wins

    lastMoves.exists(x => x.moveType == MovedType.WINNING) shouldEqual true

    game._isFinished shouldEqual true

  }

  it should "thrown an exception when player plays on finished game" in {
    val game = createGame()

    val player1 = game._playerTurn
    val player2 = players.filter(p => p !== player1)(0)

    game.play(player1, 12) // player1 moves to 12
    game.play(player2, 11) // player2 moves to 11
    game.play(player1, 12) // player1 moves to 24
    game.play(player2, 10) // player2 moves to 22
    game.play(player1, 12) // player1 moves to 36
    game.play(player2, 12) // player2 moves to 34
    game.play(player1, 12) // player1 moves to 48
    game.play(player2, 12) // player2 moves to 44
    game.play(player1, 12) // player1 moves to 60
    game.play(player2, 12) // player2 moves to 58

    game.play(player1, 3)._2  // player1 moves to 63 and wins

    an[IllegalStateException] should be thrownBy {
      game.play(player2, 5)
    }



  }


}
