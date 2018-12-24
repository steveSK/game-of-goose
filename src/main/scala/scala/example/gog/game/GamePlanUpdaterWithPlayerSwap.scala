package scala.example.gog.game

import scala.example.gog.game.Game.{Move, MovedType, Player}

class GamePlanUpdaterWithPlayerSwap extends GamePlanUpdater {

  private val findPlayerOnPos = (gamePlan: Map[Player, Int], pos: Int) => gamePlan.find(x => x._2 == pos).map(_._1)

  override def updateGamePlan(player: Player, gamePlan: Map[Game.Player, Int], moves: List[Game.Move]): (Map[Player, Int], List[Game.Move]) = {

    if (moves.isEmpty) {
      throw new IllegalArgumentException("Moves can not be empty")
    }

    if (!gamePlan.contains(player)) {
      throw new IllegalArgumentException(s"Player $player is not in the game")
    }

    if (moves.exists(move => move.player != player)) {
      throw new IllegalArgumentException("List of moves contains more than one player, list of moves should be uniformed")

    }

    val startPos = moves(0).startPos
    val endPos = moves.last.endPos

    val playerToMoveBack = findPlayerOnPos(gamePlan, endPos)

    if (playerToMoveBack.isDefined) {
      val newMoves = moves :+ Move(playerToMoveBack.get, endPos, startPos, MovedType.MOVEBACK)
      (gamePlan + (player -> endPos) + (playerToMoveBack.get -> startPos), newMoves)
    } else {
      (gamePlan + (player -> endPos), moves)
    }


  }


}
