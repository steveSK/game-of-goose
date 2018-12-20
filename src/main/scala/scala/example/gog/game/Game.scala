package scala.example.gog.game


import scala.example.gog.game.Game.MovedType.MovedType
import scala.example.gog.game.Game.{Moved, MovedType, Player}


class Game private(val players: List[Player]) {

  if (players.size < 2) {
    throw new IllegalStateException("The game needs to have atleast two players!")
  }

  private val gamePlan = new GamePlan(players)

  private var playerTurn = players(0)

  private var isFinished = false

  def _isFinished = isFinished

  def _playerTurn = playerTurn

  private val isWinningMove = (moves: List[Moved]) => moves.exists(_.moveType == MovedType.WINNING)

  def play(player: Player, diceSum: Int): (Player, List[Moved]) = {
    if (player != playerTurn) {
      throw new IllegalArgumentException(s"Player $player is not on the turn!")
    }
    if (isFinished) {
      throw new IllegalStateException(s"The game already finished")
    }
    val moves = gamePlan.move(player, diceSum)
    if (isWinningMove(moves)) {
      isFinished = true
    }
    val nextPlayer = getNextPlayer(player)
    playerTurn = nextPlayer
    (playerTurn, moves)
  }


  def getNextPlayer(player: Player): Player = {
    if (players.indexOf(player) == players.size - 1) {
      players(0)
    } else {
      gamePlan.players(players.indexOf(player) + 1)
    }
  }

}

object Game {

  case class Moved(player: Player, startPos: Int, endPos: Int, moveType: MovedType)

  case class Player(name: String)

  object MovedType extends Enumeration {
    type MovedType = Value
    val WINNING, NORMAL, BRIDGE, GOOSE, BOUNCED, MOVEBACK = Value
  }


  def createGame(players: List[Player]): Game = {
    val turnOrderPlayers = scala.util.Random.shuffle(players)
    new Game(turnOrderPlayers)
  }

}
