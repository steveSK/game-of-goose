package scala.example.gog

import scala.example.gog.cli.GameCLI.CommandLineParser

object Main {


  def main(args: Array[String]): Unit = {
    val exitCommand = "exit"

    println("Welcome in the GAME OF GOOSE")
    println("----------------------------")
    println("add player 'player' => to add player to the game")
    println("start game => to start the game")
    println("stop game => to stop the game")
    println("move player 'player' => to role a dice and move player on the board")
    println("exit => exit the game")

    Iterator.continually(io.StdIn.readLine)
      .takeWhile(_ != exitCommand)
      .foreach(x => println(CommandLineParser.readLine(x)))

    println("Game is exiting.... Bye!")
  }

}
