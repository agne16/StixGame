package edu.up.cs301.stix;

import java.util.ArrayList;

import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * The "smart" computer player. 
 * This player will check its hand to see if it can, within one move, match one of its cards
 * to the configuration on the board.
 *
 * @author Micah Alconcel
 * @version December 6, 2013
 *
 */
public class StixComputerPlayer2 extends StixComputerPlayer1
{
	private boolean[][] currGameBoard;
	private boolean didRemove = false;
	private int whichToRemove = 0;
	private Deck cards = new Deck();
	private ArrayList<Card> hand = new ArrayList<Card>();
	private ArrayList<boolean[][]> handConfigs = new ArrayList<boolean[][]>();
	private boolean madeMove = false;
	private boolean[][] savedGameBoard;
	private int savedMove1 = 0, savedMove2 = 0;
	boolean timeOut = false;

	/**
	 * Constructor for objects of class StixComputerPlayer1
	 * 
	 * @param name
	 * 		the player's name
	 */
	public StixComputerPlayer2(String name)
	{
		super(name);
	}

	/**
	 * callback method--game's state has changed
	 * 
	 * @param info
	 * 		the information (presumably containing the game's state)
	 */
	@Override
	public void receiveInfo(GameInfo info)
	{
		if(!(info instanceof StixState))
		{
			return;
		}
		this.currGameState = (StixState) info;
		if(currGameState.getWhoseMove()==this.playerNum)
		{
			currGameBoard = currGameState.getGameField();
			moveStick();
		}

	}

	/**
	 * The smarter computer player.
	 * This one will attempt all legal configurations (or almost all of them) until it either finds a match, or runs out of options.
	 * The first time it finds a match, it will discard that card.
	 * Otherwise, it'll draw a card if it doesn't find a match after moving five sticks.
	 */
	@Override
	protected void moveStick()
	{
		//gets the cards that it's going to compare the boardConfig to.
		//First we get the hand.
		currGameBoard = currGameState.getGameField();
		savedGameBoard = currGameState.getGameField();
		//first we get the hand
		cards = currGameState.getHand(this.playerNum);
		//Then we get the cards.
		for(int i = 0;i<cards.size();i++)
		{
			hand.add(cards.getCardAtSlot(i));
		}
		int count = 0;
		//Then we get the configurations of the cards.
		for(Card c: hand)
		{
			handConfigs.add(c.getConfig());
			handConfigs.set(count, trim(handConfigs.get(count)));
			count++;
		}
		//first check if any of the cards in your hand match what is already on the board
		for(int i = 0;i<handConfigs.size();i++)
		{
			if(isMatch(currGameBoard, handConfigs.get(i)))
			{
				checkHand();
				return;
			}
		}
		/*
		 * Start by removing the first stick.
		 * Because iterating this method for all five would take too long and would be boring for the human player.
		 * And boring games don't sell.
		 */

		//We start off removing the first stick.
		//Every time this method is run, this increments, until we get to the fifth stick at which point
		//if the AI still hasn't made a move, it will draw a card.
		whichToRemove++;
		int savedMove1 = 0;
		int savedMove2 = 0;
		int stickCount = 0;
		for(int i = 0;i<13;i++)
		{
			for(int j = 0;j<13;j++)
			{
				if(currGameBoard[i][j] == true)
				{
					stickCount++;
					if(stickCount == whichToRemove && didRemove == false)
					{
						currGameBoard[i][j]=false;
						//in case this turns out to be an illegal move, save the two positions
						savedMove1 = i;
						savedMove2 = j;
						didRemove = true;
					}
				}
			}
			//if you already removed a stick, break out of this method to save some time.
			if(didRemove)
			{
				break;
			}
		}

		/*
		 * And now we add a stick back, but only if you were able to remove one.
		 * Moves the sticks in every possible way until it finds a configuration that matches the one in the hand.
		 * And now we add a stick back.
		 */
		if(didRemove)
		{
			for(int i = 0;i<13;i++)
			{
				for(int j = 0;j<13;j++)
				{
					//if there is a stick there, then try to place a stick somewhere around it.
					//because those are the only legal places to put it.
					if(currGameBoard[i][j] == true)
					{
						//bottom right
						if(i<12 && j<12 && currGameBoard[i+1][j+1]==false && madeMove == false)
						{
							currGameBoard[i+1][j+1]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i+1][j+1]=false;
							}
						}
						//top right
						if(i<12 && j>0 && currGameBoard[i+1][j-1]==false && madeMove == false)
						{
							currGameBoard[i+1][j-1]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i+1][j-1]=false;
							}
						}
						//bottom left
						if(i>0 && j<12 && currGameBoard[i-1][j+1]==false&& madeMove == false)
						{
							currGameBoard[i-1][j+1]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i-1][j+1]=false;
							}
						}
						//top left
						if(i>0 && j>0 && currGameBoard[i-1][j-1]==false&& madeMove == false)
						{
							currGameBoard[i-1][j-1]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));								
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i-1][j-1]=false;
							}
						}
						//if it's a vertical stick, then the one on top
						if(j%2==1 && i>1&&currGameBoard[i-2][j]==false && madeMove == false)
						{
							currGameBoard[i-2][j]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i-2][j]=false;
							}
						}
						//if it's a vertical stick, then the one on the bottom
						if(j%2==1 && i<11&&currGameBoard[i+2][j]==false && madeMove == false)
						{
							currGameBoard[i+2][j]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i+2][j]=false;
							}
						}
						//if it's a horizontal stick, then the one to the left
						if(j%2==0 && j>1&&currGameBoard[i][j-2]==false && madeMove == false)
						{
							currGameBoard[i][j-2]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i][j-2]=false;
							}
						}
						//if it's a horizontal stick, then the one to the right
						if(j%2==1 && j<11&&currGameBoard[i][j+2]==false && madeMove == false)
						{
							currGameBoard[i][j+2]=true;
							if(checkIfAnyMatch(handConfigs,currGameBoard))
							{
								madeMove = true;
								game.sendAction(new StixMoveStixAction(this,currGameBoard));
								whichToRemove = 0;
								checkHand();
								return;
							}
							else
							{
								currGameBoard[i][j+2]=false;
							}
						}

					}
				}
			}//iterator for placing a stick
		}
		//if you removed a stick but were not able to place a stick, then put the stick back
		if(didRemove && !madeMove)
		{
			currGameBoard[savedMove1][savedMove2]=true;
		}
		//if you couldn't make a move, just move a random stick, like the dumb computer does.
		//But make sure to reset your variables.
		didRemove = false;
		madeMove = false;
		super.moveStick();
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
	 */
	public boolean[][] trim(boolean[][] arr)
	{
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
			//looks through top row
			for(int i=0; i<2; ++i)
			{
				for(int j=0; j<13; ++j)
				{
					//if top row is not completely blank
					if(trimTemp[i][j] == true)
					{
						canShiftUp = false;//set false
					}
				}
			}

			//looks through left column
			//looks through top row
			for(int i=0; i<13; ++i)
			{
				for(int j=0; j<2; ++j)
				{
					//if top row is not completely blank
					if(trimTemp[i][j] == true)
					{
						canShiftLeft = false;//set false
					}
				}
			}
			//runs if array can shift up
			if(canShiftUp)
			{
				//iterates through the 2nd and 6th rows
				for(int i=2; i < 13; ++i)
				{
					for(int j=0; j < 13; ++j)
					{
						//copies char from original to new array
						trimTemp[i-2][j] = trimTemp[i][j];
					}
				}

				for(int i=11; i<13; ++i)
				{
					for(int j=0; j<13; ++j)
					{
						trimTemp[i][j] = false;
					}
				}
			}	

			//runs if array can shift left
			if(canShiftLeft)
			{
				//iterates through 2nd and 6th columns of array
				for(int i=0; i < 13; ++i)
				{
					for(int j=2; j < 13; ++j)
					{
						//copies char from original array to new array
						trimTemp[i][j-2] = trimTemp[i][j];
					}
				}

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

	/**
	 * checks if there are any matches in the computer player's hand, any at all.
	 * @param handy - the hand being checked
	 * @param board - the board that each card in the hand will be compared to
	 * @return true if there is a match, false otherwise
	 */
	public boolean checkIfAnyMatch(ArrayList<boolean[][]> handy, boolean[][] board)
	{
		int howManyToCheck = 0;
		boolean[][] handCard = handy.get(0);
		board = trim(board);
		if(handy.size()>13)
		{
		howManyToCheck = 13;
		}
		else
		{
			howManyToCheck = handy.size();
		}
		howManyToCheck = handy.size();
		for(int i = 0;i<howManyToCheck;i++)
		{
			handCard = trim(handy.get(i));
			for(int flips = 0;flips<2;flips++)
			{
				for(int rotations = 0;rotations<4;rotations++)
				{
					if(isMatch(handCard,board))
					{
						return true;
					}
					handCard = rotate(handCard);
				}
				handCard = flipVert(handCard);
			}
		}
		return false;
	}

	/**
	 * Checks if the movestixaction was legal. If more than one stick has been moved, then it is not legal.
	 * Or if a stick has no sticks surrounding it, it is not legal.
	 * @param currentBoard - the board with the moved stick
	 * @param origBoard - the board before the stick was moved
	 * @return true if it was a legal move, false otherwise.
	 */
	public boolean checkIfLegal(boolean[][] currentBoard, boolean[][] origBoard)
	{
		//if there are more than two differences, that means the AI moved more than one stick and it should revert the board back to whatever it was
		//originally
		int badCount = 0;
		for(int k = 0; k<13;k++)
		{
			for(int i = 0;i<13;i++)
			{
				if(currGameBoard[k][i] != savedGameBoard[k][i])
				{
					badCount++;
				}
			}
		}
		if(badCount>2)
		{
			currGameBoard = savedGameBoard;
			return false;
		}
		//if you removed a stick but were not able to place a stick, then put the stick back
		return true;
	}

}//StixComputerPlayer2 class