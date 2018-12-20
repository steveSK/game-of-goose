# The Goose Game Kata
Goose game is a game where two or more players move pieces around a track by rolling a die. The aim of the game is to reach square number sixty-three before any of the other players and avoid obstacles. ([wikipedia](https://en.wikipedia.org/wiki/Game_of_the_Goose))

## Requirement ##
Scala 2.11 or higher, Gradle 4.10.x


## Instalation ##
Clone the repository to your local

# Project Build #
`gradle clean build`



## RUN ##
To run the game via CLI interface use `gradle run`

### in CLI interface ###

First You need to Add players:
TO add player use command =>  `add player 'player'` 

After you added all players you can start the game:
To start the game use command =>  `start game`

To stop the game use command =>  `stop game`
 
After the game is started, you can move players by rolling the dice or even specifying the dice values

To move player with rolling the dice use command => `move player 'player'`

To move player without rolling the dice (specifying the dice values) use command => `move player 'player' 'dice1_value' 'dice2_value'`
   
Player who first reach the posiiton 63, WINS :)

To exit the game use command => `exit`

Have a fun!


