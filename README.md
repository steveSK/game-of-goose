# The Game of Goose
Goose game is a game where two or more players move pieces around a track by rolling a dice. The aim of the game is to reach square number sixty-three before any of the other players and avoid obstacles. ([wikipedia](https://en.wikipedia.org/wiki/Game_of_the_Goose))

## Requirement ##
Scala 2.11 or higher, Gradle 4.10.x


## Instalation ##
Clone the repository to your local

## Project Build ##
To build using gradle use `gradle clean build`



## Run ##
To run the game via CLI interface use `gradle run`

### in CLI interface ###

To start to play, First you need to add players (minimum is 2):
TO add player use command =>  `add player 'player'` 

After you added all players you can start the game:
To start the game use command =>  `start game`

To stop the game use command =>  `stop game`
 
After the game is started, you can move players by rolling the dice or even specifying the dice values:

To move player with rolling the dice use command => `move player 'player'`

To move player without rolling the dice (specifying the dice values) use command => `move player 'player' 'dice1_value' 'dice2_value'`
   
Player who first reach the posiiton 63, WINS :)

To exit the game use command => `exit`

Have a fun!

# Licence #
This application is under license GNU GPL v3 [http://www.gnu.org/licenses/gpl.html]

