package scala.example.gog.game

import scala.util.Random

object Dice {

  def rollDice(): (Int, Int) = {
    (Random.nextInt(6) + 1, Random.nextInt(6) + 1)
  }
}
