package scala.example.gog

import org.scalatest._

import scala.example.gog.game.Dice


class DiceTest extends FlatSpec with Matchers {


  "A Dice" should "always return values in the range of 1 to 6" in {

    for(i <- 1 to 10) {
      val roll1 = Dice.rollDice()._1
      val roll2 = Dice.rollDice()._2

      val beInRange = be >= 1 and be <= 6

      roll1 should beInRange
      roll2 should beInRange

    }
  }

}
