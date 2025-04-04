# Table of Contents
1. [Summary](#Summary)
2. [Installation](#Installation)
3. [Quick Start](#Quick-Start)
4. [Components](#Components)
5. [Source Organization](#Organization)


## Summary:
This project Pawns Board functions as a playable version of a card game.  This is a
two-player game played on a board in which each play takes turns placing a card which influences the
grid, and continues until both players do not or can not place a card consecutively.

## Installation:
Clone the repository 'git clone https://github.com/selcarmesan/Pawns-Board/' \
Navigate to the directory and ensure dependencies are install (OpenJDK 20.0.1)

## Quick-Start:
To run this project, run the .jar file, while specifying four terms after it.  The first two are
the config file locations of the red and blue decks, respectively.  Specific information on the
formatting of pawns board config files can be found in the javadoc for PawnsCardReader.
The second two words represent what AI is controlling each player red and blue, respectively.
The available options are:
- 'human' for GUI controlled moves
- 'strategy1' for a machine player which plays the first possible move
- 'strategy2' for a machine player which tries to win over rows

Keyboard input to confirm is the enter key and the input to pass is the space bar while playing.

## Components:
This game consists of two major portions, the model, controller, and view.  The model acts as the
design for the game itself, as well as the components of it, being the players available,
red & blue, the cards used for making moves as a player, and the cells that the board consists of.
The controller contains a card reader class, which takes from a specified File to format it into
a list of cards for a particular player.

The card class has a name, cost, value, and influence grid.  It acts as a move to make
within the game, requiring the space it is played in to have the proper number of pawns belonging
to the player, after which its value (score) comes into play for the sake of tallying.

Each cell has a current number of pawns, an owner, and a card.  An empty cell starts with no pawns,
no owner, and no card, and the owner changes to the last player to influence it.  Once a card is
played, a card can no longer be played, and the cell essentially acts as a storage of the score.

The game class itself constructs a board of cells of the specified size, with the leftmost column
starting with 1 red pawn in each, and the rightmost column starting with one blue pawn in each.
The game allows each player to take a move in rotating order, either placing one of their cards
if possible, or skipping their turn.  The game continues until both players skip their turns
sequentially, in which case the game ends, and the winner can be determined.  Scoring is done
per row, and the player with the higher score cancels out all the opposing player's points
in that row.

Strategies are single-method classes which take in an active game of Pawns Board, and a player, and
returns the most optimal move for that player according to the strategy, or if there are none, 
throws an exception.

A view takes in a game of pawns board, and while active, is able to represent the board and hands
in some form, whether through a GUI or a simple textual view.

The controller needs a model of the game, a view for the model, and a player that this controller 
controls the actions of.  If the player is a machine, it will take the actions for them every turn
based on that machine players' strategy.  If the player is a human, the controller will listen for
events through interaction with the GUI and make the according plays.

The main class allows you to play a simple game after specifying in command-line the parameters
for both of the players.

## Organization:
All objects related to the model of the game and observers can be found within the model package. \
The controller for the game is found within the controller package, as well as the class that 
reads from a file and converts it into a list of cards.\
The view package contains the available views for the model, a textual representation and a GUI.\
The main class is placed in pawns board package outside of model and view.\
The docs folder contains the default supplied deck config files.

\
\
Specified Invariant:
If the game is started, then the current turn is either RED or BLUE, never null
is a logical if then statement.  Can be checked at any time.  Is ensured by the constructor,
as the game is not yet started, and the startGame method changes the player to RED before
starting the game.  From there on the only method that changes currentTurn is swapTurn, which
either changes it to RED or BLUE, neither null again.

Homework 6 Changelog:

- Separated placeCard and isMoveValid into two distinct methods rather than only containing 
placeCard.  This is so the view can check validity other than only when attempting to place a card.
- Changed the PawnsCard implementation and Card interface to no longer have a specified color.
This is due to it being a redundant field.
- Changed the Cell and Board to no longer be generic.  There is no need for generics when there is 
only a singular planned implementation.
- Changed the overload for getScore to be two separate methods, getTotalScore and getRowScore.
This is because they had slightly different purposes, so it was poor design to overload the same 
function to achieve this.
- Changed PawnsCardReader to only allow for reading from a specified file, rather than a default.  
This is to fix a misread on the instructions within the previous assignment.

Homework 7 Changelog:
- Changed the model and view to implement the proper update/observer interfaces, and edited the
methods in them to correctly call them during the proper events.
- Changed the view to only care about a single player that is specified upon construction, only 
showing their hand, and only allowing them to interact when it is their turn.