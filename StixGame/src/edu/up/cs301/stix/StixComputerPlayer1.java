package edu.up.cs301.stix;

import java.util.ArrayList;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.GameTimer;
import edu.up.cs301.game.util.Tickable;

/**
 * The "dumb" computer player. This one makes a random legal move, then checks its hand to see if any cards match
 * the configuration it has made.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * 
 * @author Micah Alconcel
 * @version December 2013
 */
public class StixComputerPlayer1 extends GameComputerPlayer implements StixPlayer, Tickable {

	// instance variables
	protected StixState currGameState;
	protected boolean myMove = false;
	protected GameTimer stall;
	protected StixFieldCanvas sfc;
	private boolean madeMove = false;
	private boolean didRemove = false;
	private boolean gate = true;
	Integer guard = Integer.valueOf(0);
	private boolean[][] savedGameBoard;
	private boolean[][] currGameBoard;

	/**
	 * Constructor for objects of class StixComputerPlayer1
	 * 
	 * @param name
	 * 		the player's name
	 */
	public StixComputerPlayer1(String name) {
		// invoke superclass constructor
		super(name);

		//timer ticks once per second
		stall = this.getTimer();
		stall.setInterval(1000);
		stall.start();
	}

	/**
	 * callback method--game's state has changed
	 * 
	 * @param info
	 * 		the information (presumably containing the game's state)
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		stall.reset();
		if(!(info instanceof StixState))
		{
			return;
		}
		stall.reset();
		this.currGameState = (StixState) info;
		if(currGameState.getWhoseMove() == this.playerNum && gate==true)
		{
			gate = false;
			moveStick();
		}
	}


	/**
	 * callback method: the timer ticked

	 */
	protected void timerTicked() {
	}

	/**
	 * Since this is the "dumb" AI, this method will move the stick to a random legal location.
	 */
	protected void moveStick()
	{
		myMove = false;
		currGameBoard = currGameState.getGameField();
		savedGameBoard = currGameBoard;
		int savedMove = 0;
		int savedMove2 = 0;
		synchronized(guard)
		{
			//remove the first stick possible
			for(int i = 0;i<13;i++)
			{
				for(int j = 0;j<13;j++)
				{
					if(currGameBoard[i][j] == true)
					{
						if(isOkayToRemove(currGameBoard,i,j)&&didRemove == false)
						{
							currGameBoard[i][j]=false;
							savedMove = i;
							savedMove2 = j;
							didRemove = true;
						}
					}
				}
				if(didRemove)
				{
					break;
				}
			}
		}

		synchronized(guard)
		{
			int count2 = 0;
			//randomly generate a number.
			//this number is which stick will be the one that the new stick gets appended to.
			int whereToPlace = (int) (Math.random()*4)+1;
			
			//only place a stick if you removed one. otherwise, you'd have six sticks.
			if(didRemove)
			{
				for(int i = 0;i<13;i++)
				{
					for(int j = 0;j<13;j++)
					{
						if(currGameBoard[i][j] == true)
						{
							count2++;
							//count2 is the number of the stick. the number of the stick is determined by when this iterator
							//reaches it. "whereToPlace" was a randomly generated # from 1-5.
							//so we're placing a random stick 
							if(count2==whereToPlace)
							{
								if(i<12 && j<12 && currGameBoard[i+1][j+1]==false && madeMove == false)
								{
									currGameBoard[i+1][j+1]=true;
									madeMove = true;
								}
								//top right
								else if(i<12 && j>0 && currGameBoard[i+1][j-1]==false && madeMove == false)
								{
									currGameBoard[i+1][j-1]=true;
									madeMove = true;
								}
								//bottom left
								else if(i>0 && j<12 && currGameBoard[i-1][j+1]==false&& madeMove == false)
								{
									currGameBoard[i-1][j+1]=true;
									madeMove = true;
								}
								//top left
								else if(i>0 && j>0 && currGameBoard[i-1][j-1]==false&& madeMove == false)
								{
									currGameBoard[i-1][j-1]=true;
									madeMove = true;
								}
							}

						}
					}
				}
			}
		}

		savedGameBoard = currGameState.getGameField();
		int badCount = 0;
		//checks how many differences there are between the original board and the board it's about to submit
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
		//if there are more than two differences, that means the AI moved more than one stick and it should revert the board back to whatever it was
		//originally
		if(badCount>2)
		{
			currGameBoard = savedGameBoard;
		}
		//if you removed a stick but were not able to place a stick, then put the stick back
		if(didRemove && !madeMove)
		{
			currGameBoard[savedMove][savedMove2]=true;
		}
		game.sendAction(new StixMoveStixAction(this, currGameBoard));
		checkHand();
	}


	/**
	 * Checks the hand to see if any of its cards match the configuration on the board, or any of its rotations.
	 */
	protected void checkHand()
	{
		//get player's hand
		Deck myHand = currGameState.getHand(this.playerNum);

		//create an arraylist of those cards
		ArrayList<Card> cards = new ArrayList<Card>();		
		for(int i=0;i<myHand.size();++i)
		{
			cards.add(myHand.getCardAtSlot(i));
		}
		//iterate the check for ALL the cards
		for(Card currCard: cards)
		{
			game.sendAction(new StixDiscardAction(this, currCard.getCardID()));
		}

		game.sendAction(new StixDrawCardAction(this));
		madeMove = false;
		myMove = false;
		didRemove = false;
		gate = true;
		stall.reset();
	}

	/**
	 * Checks if it is okay to remove the stick at the given position.
	 * @param toCheck - the array that the stick is in
	 * @param i - the x position of the stick being checked
	 * @param j - the y position of the stick being checked
	 * @return true if it is okay to remove, false otherwise.
	 */
	protected boolean isOkayToRemove(boolean[][] toCheck, int i, int j)
	{
		int adjacentCount = 0;
		//bottom right position
		if(i<12 && j<12 && toCheck[i+1][j+1]==true)
		{
			adjacentCount++;
		}
		//top right
		if(i<12 && j>0 && toCheck[i+1][j-1]==true)
		{
			adjacentCount++;
		}
		//bottom left
		if(i>0 && j<12 && toCheck[i-1][j+1]==true)
		{
			adjacentCount++;
		}
		//top left
		if(i>0 && j>0 && toCheck[i-1][j-1]==true)
		{
			adjacentCount++;
		}
		//if it's a vertical stick, then the one below
		if(j%2==0 && j<11 && toCheck[i][j+2]==true)
		{
			adjacentCount++;
		}
		//if it's a vertical stick, then the one above
		if(j%2==0 && j>1 && toCheck[i][j-2]==true)
		{
			adjacentCount++;
		}
		//if it's a horizontal stick, then the one to the right
		if(j%2==1 && i<11 && toCheck[i+2][j]==true)
		{
			adjacentCount++;
		}
		//if it's a horizontal stick, then the one to the left
		if(j%2==1 && i>1 && toCheck[i-2][j]==true)
		{
			adjacentCount++;
		}
		//if the position was bordered by more than two sticks, it isn't a good idea to remove it.
		if(adjacentCount>1)
		{
			return false;
		}
		return true;
	}
}//StixComputerPlayer1 Class

