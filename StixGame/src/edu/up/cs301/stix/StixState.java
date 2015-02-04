package edu.up.cs301.stix;

import edu.up.cs301.game.infoMsg.GameState;

/**
 * This contains the state for the Stix game. 
 * 
 * @author Steven R. Vegdahl
 * 
 * @author Bryce Matsuda
 * @author T.J. Agne
 * 
 * @version December 4, 2013
 */
public class StixState extends GameState {

	//to satisfy serializable interface
	private static final long serialVersionUID = 1244960591472643474L;

	// instance variables
	private boolean[][] gameField; // stix field represented as an array
	private Deck[] hands; // player's hands
	private Deck deck; // stix deck used in game
	private Deck masterDeck; // stix deck that stays full to verify card info

	// makes sure that a stix has been moved first
	// before allowing players to discard/draw a card
	private boolean fieldHasBeenUpdated = false;
	
	// an int that tells whose move it is
	private int playerToMove;

	/**
	 * constructor for a new stix state
	 * 
	 */
	public StixState() {
		// create new Stix deck, initialize cards, and shuffle deck
		deck = new Deck();
		deck.init();
		deck.shuffle();

		// create new master deck
		masterDeck = new Deck();
		masterDeck.init();

		// randomly pick player to move first
		playerToMove = (int)(2*Math.random());

		hands = new Deck[2]; // create players' hands and deal 5 cards
		for (int z = 0; z < 2; z++)
		{
			hands[z] = new Deck();
			this.deal5Cards(z);
		}

		// create initial Stix field configuration based on next top card
		gameField = new boolean[StixFieldCanvas.FIELD_SIZE][StixFieldCanvas.FIELD_SIZE];

		deck.removeTopCard();

		Card topCard = deck.removeTopCard();
		boolean[][] topConfig = topCard.getConfig();
		gameField = topConfig;
	}

	/**
	 * copy constructor; makes a copy of the original object
	 * 
	 * @param orig
	 * 		the object from which the copy should be made
	 */
	public StixState(StixState orig) {
		deck = orig.deck;
		masterDeck = orig.masterDeck;
		gameField = new boolean[StixFieldCanvas.FIELD_SIZE][StixFieldCanvas.FIELD_SIZE];

		for (int x = 0; x < gameField.length; x++)
		{
			for (int y = 0; y < gameField.length; y++)
			{
				gameField[x][y] = orig.gameField[x][y];
			}
		}

		playerToMove = orig.playerToMove;
		fieldHasBeenUpdated = orig.fieldHasBeenUpdated;
		hands = new Deck[2];
		
		for (int z = 0; z < 2; z++)
		{
			hands[z] = new Deck(orig.hands[z]);
		}
	}

	/**
	 * Tells whose move it is.
	 * 
	 * @return the index (0 to 1) of the player whose move it is.
	 */
	public int getWhoseMove() {
		return playerToMove;
	}

	/**
	 * set whose move it is
	 * @param id
	 * 		the player we want to set as to whose move it is
	 */
	public void setWhoseMove(int id) {
		playerToMove = id;
	}

	/**
	 * gives current amount of cards in deck
	 * 
	 * @return amount of cards in deck
	 */
	public int getDeckSize()
	{
		return deck.size();
	}

	/**
	 * Removes a card from a deck
	 * 
	 * @param rCard 
	 * 				the card to remove
	 * @param id 
	 * 				the ID of the player's deck we want to remove the card from
	 * 
	 */
	public void removeCard(Card rCard, int id)
	{
		for (int x = 0; x < hands[id].size(); x++) //look at each card in hand
		{
			Card cCard = hands[id].getCardAtSlot(x);
			if (rCard.getCardID() == cCard.getCardID())
			{
				hands[id].remove(cCard);
			}
		}
	}

	/**
	 * Deals one card to a specified player
	 * 
	 * @param id the ID of the player we want to deal the card to
	 */
	public void dealCard(int id)
	{
		Card handSlot = deck.getTopCard();
		hands[id].add(handSlot);
		deck.removeTopCard();
	}

	/**
	 * Deals five cards to a specified player
	 * 
	 * @param id the ID of the player we want to deal the cards to
	 */
	public void deal5Cards(int id)
	{
		for (int x = 0; x < 5; x++)
		{
			Card handSlot = deck.getTopCard();
			hands[id].add(handSlot, x);
			deck.removeTopCard();
		}
	}

	/**
	 * getHand - Gives the given deck.
	 * 
	 * @return  the deck for the given player
	 */
	public Deck getHand(int id)
	{
		return hands[id];
	}

	/**
	 * gives current stix field
	 * 
	 * @return  the current stix field
	 */
	public boolean[][] getGameField()
	{
		return this.gameField;
	}

	/**
	 * sets stix field to new configuration (given by StixMoveStixAction)
	 * 
	 * @param field the config we want to set the new field to
	 * 
	 */
	public void setGameField(boolean[][] field)
	{
		gameField = field;
	}

	/**
	 * gives master deck
	 * 
	 * @return the master deck
	 */
	public Deck getMasterDeck()
	{
		return masterDeck;
	}

	/**
	 * Returns whether the field has been changed by a player or not
	 * 
	 * @return true if field has been changed, false if it hasn't
	 */
	public boolean getFieldStatus() {
		return fieldHasBeenUpdated;
	}
	
	/**
	 * Sets field status to true or false, depending on if the field has been updated
	 * 
	 * @param newStatus the status of the field
	 */
	public void setFieldStatus(boolean newStatus)
	{
		fieldHasBeenUpdated = newStatus;
	}
}//StixState
