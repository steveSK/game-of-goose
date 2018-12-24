package scala.example.gog.game

import scala.collection.mutable
import scala.example.gog.game.Game.{Moved, MovedType, Player}

class GamePlan(val players: List[Player]) {

  private val gamePlan = mutable.HashMap.empty[Player, Int]

  private val findPlayerOnPos = (pos: Int) => gamePlan.find(x => x._2 == pos).map(_._1)

  for (player <- players) {
    gamePlan.put(player, 0)
  }

  def move(player: Player, diceSum: Int): List[Moved] = {
    validateMove(player, diceSum)
    val startPos = gamePlan.get(player).get
    var moves = doMove(player, startPos, diceSum)

    val endPos = moves.last.endPos
    val playerToMoveBack = findPlayerOnPos(endPos)
    if (playerToMoveBack.isDefined) {
      updatePlan(playerToMoveBack.get, startPos)
      moves = moves :+ Moved(playerToMoveBack.get, endPos, startPos, MovedType.MOVEBACK)
    }
    updatePlan(player, endPos)

    moves
  }

  private def validateMove(player: Player, diceSum: Int): Unit = {

    if (!players.contains(player)) {
      throw new IllegalArgumentException("Player: + " + player.name + " is not in the game")
    }

    if (diceSum < 2 || diceSum > 12) {
      throw new IllegalArgumentException("Dice sum is not in the range of [2,12]")
    }
  }

  private def doMove(player: Player, lastPos: Int, diceSum: Int): List[Moved] = {
    val toPos = lastPos + diceSum

    (lastPos, toPos) match {
      case (_, GamePlanDefinition.winning) => {
        List(Moved(player, lastPos, GamePlanDefinition.winning, MovedType.WINNING))
      }
      case (_, GamePlanDefinition.bridge._1) => {
        List(Moved(player, lastPos, toPos, MovedType.NORMAL), Moved(player, toPos, GamePlanDefinition.bridge._2, MovedType.BRIDGE))
      }
      case (from, to) if (!GamePlanDefinition.goosePos.contains(from) && GamePlanDefinition.goosePos.contains(to)) => {
        Moved(player, from, to, MovedType.NORMAL) :: doMove(player, to, diceSum)
      }
      case (from, to) if (GamePlanDefinition.goosePos.contains(from) && !GamePlanDefinition.goosePos.contains(to)) => {
        List(Moved(player, from, to, MovedType.GOOSE))
      }
      case (from, to) if (GamePlanDefinition.goosePos.contains(from) && GamePlanDefinition.goosePos.contains(to)) => {
        Moved(player, from, to, MovedType.GOOSE) :: doMove(player, to, diceSum)
      }
      case (_, to) if (to > GamePlanDefinition.winning) => {
        val bounceBack = to - GamePlanDefinition.winning
        List(Moved(player, lastPos, GamePlanDefinition.winning - bounceBack, MovedType.BOUNCED))
      }
      case _ => {
        List(Moved(player, lastPos, toPos, MovedType.NORMAL))
      }
    }
  }

  private def updatePlan(player: Player, pos: Int): Unit = {
    gamePlan.put(player, pos)
  }

}
