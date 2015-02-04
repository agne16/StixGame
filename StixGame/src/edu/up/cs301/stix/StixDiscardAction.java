package edu.up.cs301.stix;

import edu.up.cs301.game.GamePlayer;

/**
 * A StixDiscardAction occurs whenever a player is to discard a card from their hand.
 * 
 * @author Micah Alconcel
 *
 */

public class StixDiscardAction extends StixMoveAction {

	// satisfy Serializable interface
	private static final long serialVersionUID = -8573657870240207264L;
	
	// instance variables
	private Card card;
	private int id;
	
	/**
	 * Constructor for a StixDiscardAction
	 * @param player - the player making the move
	 * @param card - the card that is being discarded
	 */
	StixDiscardAction(GamePlayer player, int cardID) {
		super(player);
		this.id = cardID;
	}

	/**
	 * Getter method for the card that is being discarded.
	 * @return the card that is being discarded
	 */
	public Card getCard()
	{
		return this.card;
	}

	/**
	 * Getter method for the card ID that is being discarded.
	 * @return the card that is being discarded
	 */
	public int getID()
	{
		return this.id;
	}

}//StixDiscardAction
