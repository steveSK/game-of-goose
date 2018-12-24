package scala.example.gog.game

import scala.example.gog.game.Game.{Move, MovedType, Player}

object GameRules {

  val goosePos = List(5, 9, 14, 18, 23, 27)

  val winning = 63

  val bridge = (6, 12)


  def movePlayer(player: Player, lastPos: Int, diceSum: Int): List[Move] = {
    if (diceSum < 2 || diceSum > 12) {
      throw new IllegalArgumentException("Dice sum is not in the range of [2,12]")
    }
    val toPos = lastPos + diceSum

    (lastPos, toPos) match {
      case (_, GameRules.winning) => {
        List(Move(player, lastPos, GameRules.winning, MovedType.WINNING))
      }
      case (_, GameRules.bridge._1) => {
        List(Move(player, lastPos, toPos, MovedType.NORMAL), Move(player, toPos, GameRules.bridge._2, MovedType.BRIDGE))
      }
      case (from, to) if (!GameRules.goosePos.contains(from) && GameRules.goosePos.contains(to)) => {
        Move(player, from, to, MovedType.NORMAL) :: movePlayer(player, to, diceSum)
      }
      case (from, to) if (GameRules.goosePos.contains(from) && !GameRules.goosePos.contains(to)) => {
        List(Move(player, from, to, MovedType.GOOSE))
      }
      case (from, to) if (GameRules.goosePos.contains(from) && GameRules.goosePos.contains(to)) => {
        Move(player, from, to, MovedType.GOOSE) :: movePlayer(player, to, diceSum)
      }
      case (_, to) if (to > GameRules.winning) => {
        val bounceBack = to - GameRules.winning
        List(Move(player, lastPos, GameRules.winning - bounceBack, MovedType.BOUNCED))
      }
      case _ => {
        List(Move(player, lastPos, toPos, MovedType.NORMAL))
      }
    }
  }

}
