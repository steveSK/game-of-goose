package scala.example.gog.game

import scala.example.gog.game.Game.{Move, MovedType, Player}

class GamePlan(val players: List[Player]) {

  private val gamePlanUpdater = new GamePlanUpdaterWithPlayerSwap()

  private var gamePlan = players.map(p => p -> 0).toMap

  private val findPlayerOnPos = (pos: Int) => gamePlan.find(x => x._2 == pos).map(_._1)


  def move(player: Player, diceSum: Int): List[Move] = {
    validatePlayer(player)
    val startPos = gamePlan.get(player).get
    var moves = doMove(player, startPos, diceSum)

    val (newPlan, newMoves) = gamePlanUpdater.updateGamePlan(player, gamePlan, moves)
    gamePlan = newPlan

    newMoves
  }

  private def validatePlayer(player: Player): Unit = {
    if (!players.contains(player)) {
      throw new IllegalArgumentException("Player: + " + player.name + " is not in the game")
    }
  }

  private def doMove(player: Player, lastPos: Int, diceSum: Int): List[Move] = {
    GameRules.movePlayer(player,lastPos,diceSum)
  }

}
