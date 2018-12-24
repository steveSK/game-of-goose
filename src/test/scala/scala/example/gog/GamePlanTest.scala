package scala.example.gog

import org.scalatest._

import scala.example.gog.game.Game.MovedType.MovedType
import scala.example.gog.game.Game.{Moved, MovedType, Player}
import scala.example.gog.game.GamePlan


class GamePlanTest extends FlatSpec with Matchers {

  val players = List(Player("test1"), Player("test2"))

  val createGame = () => new GamePlan(players)

  val moveShouldBeEqual = (move: Moved, player: Player, startPos: Int, endPos: Int, moveType: MovedType) => {
    move.startPos shouldEqual startPos
    move.player shouldEqual player
    move.endPos shouldEqual endPos
    move.moveType shouldEqual moveType
  }


  "A Game Plan" should "throw an exception when dice sum is not in the range <2,12>" in {
    an[IllegalArgumentException] should be thrownBy {
      createGame().move(players(0), 0)
    }

    an[IllegalArgumentException] should be thrownBy {
      createGame().move(players(0), 15)
    }
  }

  "A Game Plan" should "throw an exception when player is not in the game" in {
    an[IllegalArgumentException] should be thrownBy {
      createGame().move(Player("test3"), 6)
    }
  }

  "A Game Plan" should " return Normal move, when player moves by sum of dice and steps on normal (non-special) space" in {
    val moves = createGame().move(players(0), 3)
    moves.size shouldEqual 1

    val move = moves(0)
    moveShouldBeEqual(move, players(0), 0, 3, MovedType.NORMAL)
  }

  "A Game Plan" should " return Normal and Bridge move, when player moves by sum of dice and steps on the bridge space" in {
    val moves = createGame().move(players(0), 6)
    moves.size shouldEqual 2

    val normalMove = moves(0)
    moveShouldBeEqual(normalMove, players(0), 0, 6, MovedType.NORMAL)

    val bridgeMove = moves(1)
    moveShouldBeEqual(bridgeMove, players(0), 6, 12, MovedType.BRIDGE)
  }

  "A Game Plan" should " return Normal and Goose jump move, when player moves by sum of dice and steps on the goose space" in {
    val moves = createGame().move(players(0), 5)
    moves.size shouldEqual 2

    val normalMove = moves(0)
    moveShouldBeEqual(normalMove, players(0), 0, 5, MovedType.NORMAL)

    val gooseJump = moves(1)
    moveShouldBeEqual(gooseJump, players(0), 5, 10, MovedType.GOOSE)
  }

  "A Game Plan" should " return Normal and Goose double jump move, when player moves by sum of dice " +
    "and steps on the goose space and new position after the jump is again the goose space" in {
    val gamePlan = createGame()
    val firstMoves = gamePlan.move(players(0), 10)

    val doubleGooseJumpMoves = gamePlan.move(players(0), 4)

    doubleGooseJumpMoves.size shouldEqual 3

    val normalMove = doubleGooseJumpMoves(0)
    moveShouldBeEqual(normalMove, players(0), 10, 14, MovedType.NORMAL)

    val firstGooseJump = doubleGooseJumpMoves(1)
    moveShouldBeEqual(firstGooseJump, players(0), 14, 18, MovedType.GOOSE)

    val doubleGooseJump = doubleGooseJumpMoves(2)
    moveShouldBeEqual(doubleGooseJump, players(0), 18, 22, MovedType.GOOSE)


  }

  "A Game Plan" should " return Normal and MoveBack move, when player moves by sum of dice to a places occupied by other player" in {
    val game = createGame()

    game.move(players(0), 4)

    val playerMoveBack = game.move(players(1), 4)

    playerMoveBack.size shouldEqual 2


    val player2move = playerMoveBack(0)
    moveShouldBeEqual(player2move,players(1), 0, 4, MovedType.NORMAL)

    val player1moveBack = playerMoveBack(1)
    moveShouldBeEqual(player1moveBack,players(0), 4, 0, MovedType.MOVEBACK)
  }

  "A Game Plan" should " return Bounced move, when player moves by sum of dice to a place which exceeds number 63" in {
    val game = createGame()
    game.move(players(0),12) // player moves to 12
    game.move(players(0),12) // player moves to 24
    game.move(players(0),12) // player moves to 36
    game.move(players(0),12) // player moves to 48
    game.move(players(0),12) // player moves to 60

    val moves = game.move(players(0),5)

    moves.size shouldEqual 1
    moveShouldBeEqual(moves(0),players(0), 60, 61, MovedType.BOUNCED)

  }

  "A Game Plan" should " return Winning move, when player moves by sum of dice to a place which is equal number 63" in {
    val game = createGame()
    game.move(players(0),12) // player moves to 12
    game.move(players(0),12) // player moves to 24
    game.move(players(0),12) // player moves to 36
    game.move(players(0),12) // player moves to 48
    game.move(players(0),12) // player moves to 60

    val moves = game.move(players(0),3)

    moves.size shouldEqual 1
    moveShouldBeEqual(moves(0),players(0), 60, 63, MovedType.WINNING)

  }



}
