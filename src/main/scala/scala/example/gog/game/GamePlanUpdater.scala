package scala.example.gog.game

import scala.example.gog.game.Game.{Move, Player}

trait GamePlanUpdater {

  def updateGamePlan(player: Player, gamePlan: Map[Player, Int], moves: List[Move]): (Map[Player, Int], List[Move])

}
