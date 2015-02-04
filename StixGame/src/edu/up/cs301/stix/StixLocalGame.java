package edu.up.cs301.stix;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a Stix game. It sends updates to the players whenever the gamestate has been updated.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * 
 * @author Bryce Matsuda
 * @author Micah Alconcel
 * @author T.J. Agne
 * 
 * @version December 6, 2013
 */
public class StixLocalGame extends LocalGame implements StixGame {

	// target value - 0 cards in hand or 0 cards in the deck
	public static final int TARGET_SCORE = 0; 

	// the game's state
	private StixState state;

	/**
	 * Tell whether the given player is allowed to make a move at the
	 * present point in the game. 
	 * 
	 * @param playerIdx
	 * 		the player's player-number (ID)
	 * @return
	 * 		true iff the player is allowed to move
	 */
	@Override
	public boolean canMove(int playerIdx) {
		if (state.getWhoseMove() == playerIdx)
		{
			return true;
		}
		return false;
	}

	/**
	 * This ctor should be called when a new stix game is started
	 */
	public StixLocalGame() {
		// initialize the game state
		super();
		this.state = new StixState();

	}

	/**
	 * makeMove - Handles the various actions (DrawCard, MoveStix, Discard) 
	 * @param action - the action being performed
	 * @return true if the action was successful, false otherwise.

	 */
	@Override
	public boolean makeMove(GameAction action) {
		Log.i("action", action.getClass().toString());

		if (action instanceof StixMoveAction) {

			// cast so that the game knows it's StixMoveAction
			StixMoveAction sma = (StixMoveAction) action;
			int thisPlayerIdx = getPlayerIdx(sma.getPlayer());

			if (thisPlayerIdx != state.getWhoseMove())
			{
				return false;
			}
			else if (sma instanceof StixDrawCardAction && state.getFieldStatus()) // and time is up
			{
				state.dealCard(thisPlayerIdx);
				state.setFieldStatus(false);
				state.setWhoseMove(Math.abs(1 - state.getWhoseMove()));
				return true;
			}
			else if (sma instanceof StixMoveStixAction)
			{
				StixMoveStixAction smsa = (StixMoveStixAction) sma;
				state.setGameField(smsa.getField());
				state.setFieldStatus(true);
				this.sendAllUpdatedState();
				return true;
			}
			else if (sma instanceof StixDiscardAction && state.getFieldStatus())
			{
				StixDiscardAction currAction = (StixDiscardAction) sma;
				int playedCardId = currAction.getID(); // card ID being submitted for discard

				Deck masterDeck = state.getMasterDeck();//gets deck that all the cards in order
				Card playedCard = masterDeck.getCardById(playedCardId);//gets the card from masterdeck based on ID

				boolean[][] playedConfig = playedCard.getConfig();//the configuration of the card

				boolean[][] currBoard = state.getGameField();//the configuration on the board

				//trims both arrays for comparison
				currBoard = trim(currBoard);
				playedConfig = trim(playedConfig);

				//checks all four rotations of the config on the board to see if it matches the card in the hand
				for(int flip = 0;flip<2;++flip)
				{
					for(int rotations = 0;rotations<4;rotations++)
					{
						if(isMatch(currBoard,playedConfig))
						{
							state.removeCard(playedCard, thisPlayerIdx); // remove the card
							state.setWhoseMove(Math.abs(1 - state.getWhoseMove())); // change whose turn it is to move
							state.setFieldStatus(false);
							Log.i("action", "Player " + thisPlayerIdx + " has made a move.");
							Log.i("msg", "It is now " + state.getWhoseMove() + "'s turn.");
							return true;
						}
						else//if there was not match fount
						{
							//rotate the card configuration
							playedConfig = rotate(playedConfig);
							playedConfig = trim(playedConfig);
						}
					}
					//flip and trim after comparing the first four rotations
					playedConfig = flipVert(playedConfig);
					playedConfig = trim(playedConfig);
				}
				return false;
			}
		}
		else
		{
			return false;
		}

		return true;
	}//makeMove


	/**
	 * send the updated state to a given player
	 * 
	 * @param p - the player to send the state to.
	 * 
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		p.sendInfo(new StixState(state));
		
	}//sendUpdatedState

	/**
	 * Check if the game is over. The game ends when either a player discards all of their cards,
	 * or the deck runs out of cards.
	 * Currently doesn't handle ties.
	 * 
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	@Override
	public String checkIfGameOver() {

		int[] handSizes = new int[getNumPlayers()];
		int lowestHandSize = state.getHand(0).size();
		int winningPlayer = 0;
		for(int i = 0;i < handSizes.length; i++)
		{
			//if a player runs out of cards, then that player wins

			if(state.getHand(i).size() == 0)
			{
				return "Player " + (i + 1) + " has won by discarding all cards in his/her hand.";
			}

			//gets the player with the lowest number of cards, just in case the deck runs out

			else if(state.getHand(i).size() <lowestHandSize)
			{
				lowestHandSize = state.getHand(i).size();
				winningPlayer = i;
			}
		}

		int deckSize = state.getDeckSize();

		//if the deck runs out of cards, the game is over, and the player with the fewest cards wins.
		if (deckSize==0)
		{
			return "All cards in the deck have been drawn. " +
					"Player " + (winningPlayer + 1) + " wins with " + lowestHandSize + " cards in his/her hand.";
		}
		else {
			//No player has achieved the win condition yet
			return null;
		}
	}		

	/**
	 * Getter method for how many players are playing in the game.
	 * @return - the number of players currently playing
	 */
	public int getNumPlayers()
	{
		return super.players.length;
	}

	/**
	 * This method takes an existing 6x6 array of char and 
	 * rotates it counterclockwise
	 * 
	 * @param arr
	 * 		The existing 6x6 array to be rotated
	 */
	public boolean[][] rotate(boolean[][] arr)
	{
		//creates a new temporary array to do rotation
		boolean[][] rotTemp = new boolean[13][13];

		//iterates over entire original array and copies
		//it to the new array
		//it swaps the state of a char (if any)
		for(int i=0; i<13; ++i)
		{
			for(int j=0; j<13; ++j)
			{
				//copies char from original to new
				rotTemp[j][12-i] = arr[i][j];

			}
		}

		//sends array out to trim
		//		trim(rotTemp);
		//		rotTemp = trim(rotTemp);
		return rotTemp;
	}

	/**
	 * This method takes an existing 6x6 array and flips it vertically
	 * 
	 * @param arr
	 * 		The array to flip
	 */
	public boolean[][] flipVert(boolean[][] arr)
	{
		//creates new temporary array
		boolean[][] flipVertTemp = new boolean[13][13];

		//copies char from original to new array
		for(int i=0; i < 13; ++i)
		{
			for(int j=0; j < 13; ++j)
			{
				flipVertTemp[i][12-j] = arr[i][j];
			}
		}

		//sends array out to trim
		//flipVertTemp = trim(flipVertTemp);
		return flipVertTemp;
	}

	/**
	 * This method trims blank first columns or
	 * blank first rows of array such that the
	 * configuration is in the top left corner or array.
	 * 
	 * @param arr
	 * 		The array to be trimmed
	 * 		(Passed in from rotate() or a flip method)
	 * 
	 * @return
	 * 		The trimmed array
	 */
	public boolean[][] trim(boolean[][] arr)
	{
		//creates a new array and copies contents from old to new
		boolean[][] trimTemp = new boolean[13][13];
		for(int i=0;i<13;++i)
		{
			for(int j=0;j<13;++j)
			{
				trimTemp[i][j] = arr[i][j];
			}
		}		

		//assumes that the array can be shifted up
		boolean canShiftUp = true;

		//assumes the array can be shifted left
		boolean canShiftLeft = true;

		while (canShiftUp || canShiftLeft)
		{
			//looks through two rows
			for(int i=0; i<2; ++i)
			{
				for(int j=0; j<13; ++j)
				{
					//if top two rows not completely blank
					if(trimTemp[i][j] == true)
					{
						canShiftUp = false;//set false
					}
				}
			}

			//looks to first two columns
			for(int i=0; i<13; ++i)
			{
				for(int j=0; j<2; ++j)
				{
					//if first two columns are not completely blank
					if(trimTemp[i][j] == true)
					{
						canShiftLeft = false;//set false
					}
				}
			}
			
			if(canShiftUp)
			{
				//iterates through rows 2-12
				for(int i=2; i < 13; ++i)
				{
					for(int j=0; j < 13; ++j)
					{
						//copies boolean from original to new array
						trimTemp[i-2][j] = trimTemp[i][j];
					}
				}

				//sets last two rows to false (i.e. clears them)
				for(int i=11; i<13; ++i)
				{
					for(int j=0; j<13; ++j)
					{
						trimTemp[i][j] = false;
					}
				}
			}	

			if(canShiftLeft)
			{
				//iterates through columns 2-12
				for(int i=0; i < 13; ++i)
				{
					for(int j=2; j < 13; ++j)
					{
						//copies boolean from original array to new array
						trimTemp[i][j-2] = trimTemp[i][j];
					}
				}

				//sets last two columns to false (i.e. clears them)
				for(int i=0; i<13; ++i)
				{
					for(int j=11; j<13; ++j)
					{
						trimTemp[i][j] = false;
					}
				}
			}

		}

		return trimTemp;
	}

	/**
	 * Checks to see if two booleans match
	 * 
	 * @param board
	 * 		The array that represents the stix field
	 * @param card
	 * 		The array that represents a stix configuration
	 * @return
	 */
	public boolean isMatch(boolean[][] board, boolean[][] card)
	{
		//iterates to compare each corresponding element of arrays
		for(int i=0;i<13;++i)
		{
			for(int j=0;j<13;++j)
			{
				if(board[i][j] != card[i][j])
				{
					//return false if there's a discrepancy
					return false;
				}
			}
		}		

		//otherwise, return that arrays match
		return true;
	}

}// class StixLocalGame
