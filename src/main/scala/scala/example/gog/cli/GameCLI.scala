package scala.example.gog.cli


import scala.example.gog.cli.Commands._
import scala.example.gog.game.Game
import scala.example.gog.game.Game.Player

object GameCLI {


  private var players = List.empty[Player]
  private var game = None: Option[Game]


  object CommandLineParser {

    implicit class Regex(sc: StringContext) {
      def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
    }

    def readLine(line: String): String = {

      line match {
        case r"start game" => {
          doStart()
        }

        case r"stop game" => {
          doStop()
        }

        case r"add player ([0-9a-zA-Z]+$$)${player}" => {
          doAddPlayer(Player(player))
        }

        case r"move player ([0-9a-zA-Z]+$$)${player}" => {
          doMove(Player(player), MovePlayer(game.get, Player(player)))
        }

        case r"move player ([0-9a-zA-Z]+\b)${player} ([0-9]+\b)${roll0} ([0-9]+\b)${roll1}" => {
          doMove(Player(player), MovePlayer(game.get, Player(player), Some(roll0.toInt), Some(roll1.toInt)))
        }

        case _ =>
          command_not_known
      }
    }

    def doStart(): String = {
      if (!game.isDefined) {
        val result = StartGame(players.toList).start
        game = result._1
        result._2
      } else {
        game_already_started
      }
    }

    def doStop(): String = {
      if (game.isDefined) {
        game = None
        game_stopped
      } else {
        no_game_exist
      }
    }

    def doAddPlayer(player: Player): String = {
      if (!game.isDefined) {
        val result = AddPlayer(player, players).addPlayer()
        players = result._1
        result._2
      } else {
        game_already_started
      }
    }

    def doMove(player: Player, movePlayer: => MovePlayer): String = {
      if (!game.isDefined) {
        game_not_started
      } else {
        val result = movePlayer.move()
        if (game.get._isFinished) {
          game = None
        }
        result._2
      }
    }
  }

}
