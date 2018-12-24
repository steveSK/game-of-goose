package scala.example.gog.cli

import scala.example.gog.game.Game.{Move, MovedType, Player}
import scala.example.gog.game._


object Commands {

  val game_not_started = "No game is running, to move, first you need to start the game!"
  val game_stopped = "Game was stopped!"
  val game_started = "Game was started!"
  val command_not_known = "Command not known"
  val not_enough_players = "There have to be atleast two players to start the game!"
  val game_already_started = "Game already started! You can't add new players!"
  val no_game_exist = "No Game is running!"
  val illegal_dice_value = (roll0: Int, roll1: Int) => s"Dice Rolls values $roll0 $roll1 are not in right range, values have to be between 1-6!"


  val player_not_known = (player: String) => s"Player $player not found!"
  val already_exist = (name: String) => s"Player $name: already existing player"
  val player_added = (player: String) => s"players: $player"
  val player_is_not_on_turn = (player: String) => s"player $player is not on turn!"


  val player_rolls = (player: String, diceRoll0: Int, diceRoll1: Int) => s"$player rolls $diceRoll0, $diceRoll1."
  val player_moves_to_pos = (player: String, start: String, pos: String) => s"$player moves from $start to $pos"
  val player_goose_jump = (player: String, pos: Int) => s"The Goose. $player moves again and goes to $pos"
  val player_bridge_jump = (player: String) => s"$player jumps to ${GameRules.bridge._2}"
  val player_wins = (player: String, start: Int) => player_moves_to_pos(player, start.toString, GameRules.winning.toString) + s" $player Wins!!! Game is Finished :)"
  val player_bounced = (player: String, start: Int, bouncedPos: Int) => player_moves_to_pos(player, start.toString, GameRules.winning.toString) +
    s" $player bounces! $player returns to $bouncedPos"
  val player_move_back = (player: String, currentPos: Int, nextPos: String) => s"On $currentPos there is $player, who returns to $nextPos"
  val player_turn = (player: String) => s"$player is on Turn!"

  trait Command[A] {
    def execute() : (A, String)
  }

  case class StartGame(players: List[Player]) extends Command[Option[Game]]{
    def execute: (Option[Game], String) = {
      if (players.size < 2) {
        (None, not_enough_players)
      } else {
        val game = Game.createGame(players)
        (Some(game), game_started + " " + player_turn(game.players(0).name))
      }
    }
  }

  case class MovePlayer(game: Game, player: Player, diceRoll0: Option[Int] = None, diceRoll1: Option[Int] = None) extends Command[Option[Player]] {

    private val convertPosToString = (pos: Int) => pos match {
      case 0 => "Start"
      case 6 => "Bridge"
      case _ => pos.toString

    }

    private val convertMoveToMessage = (move: Move) => move.moveType match {
      case MovedType.NORMAL => {
        player_moves_to_pos(move.player.name, convertPosToString(move.startPos), convertPosToString(move.endPos))
      }
      case MovedType.WINNING => {
        player_wins(move.player.name, move.startPos)
      }
      case MovedType.BRIDGE => {
        player_bridge_jump(move.player.name)
      }
      case MovedType.BOUNCED => {
        player_bounced(move.player.name, move.startPos, move.endPos)
      }
      case MovedType.GOOSE => {
        player_goose_jump(move.player.name, move.endPos)
      }
      case MovedType.MOVEBACK => {
        player_move_back(move.player.name, move.startPos, convertPosToString(move.endPos))
      }
    }

    private val isValidRange = (diceRoll: Int) => diceRoll > 0 && diceRoll < 7

    def execute(): (Option[Player], String) = {
      if (!game.players.contains(player)) {
        (None, player_not_known(player.name))
      } else if (game._playerTurn != player) {
        (None, player_is_not_on_turn(player.name))
      } else {
        (diceRoll0, diceRoll1) match {
          case (Some(r0), Some(r1)) => {
            if (!isValidRange(r0) || !isValidRange(r1)) {
              (None, illegal_dice_value(r0, r1))
            } else {
              movePlayer(player, r0, r1)
            }
          }
          case _ => {
            val diceRoll = Dice.rollDice()
            movePlayer(player, diceRoll._1, diceRoll._2)
          }
        }
      }
    }

    private def movePlayer(player: Player, roll0: Int, roll1: Int): (Option[Player], String) = {
      val diceSum = roll0 + roll1

      val playerMoved = game.play(player, diceSum)
      val nextPlayer = playerMoved._1
      val moves = playerMoved._2

      var movesMessages = player_rolls(player.name, roll0, roll1) + " " + moves.map(x => convertMoveToMessage(x)).mkString(", ")
      if (!game._isFinished) {
        movesMessages = movesMessages + ". " + player_turn(nextPlayer.name)
      }
      (Some(nextPlayer), movesMessages)
    }

  }

  case class AddPlayer(player: Player, players: List[Player]) extends Command[List[Player]] {
    def execute(): (List[Player], String) = {
      if (players.exists(_ == player)) {
        (Nil, already_exist(player.name))
      } else {
        (player :: players, player_added((player :: players).map(x => x.name).mkString(", ")))
      }
    }
  }

}
