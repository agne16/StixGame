package edu.up.cs301.stix;

import edu.up.cs301.game.GamePlayer;

/**
 * A StixDrawCardAction occurs whenever a player draws a card.
 * A player draws a card if, on their turn, they do not make a move before time runs out
 * or they do not have a card in their hand they can discard.
 * 
 * @author Micah Alconcel
 *
 */

public class StixDrawCardAction extends StixMoveAction {

	// satisfy Serializable interface
	private static final long serialVersionUID = -7030853678079010660L;

	/**
	 * Constructor for a StixDrawCardAction.
	 * @param player - the player making the move.
	 */
	public StixDrawCardAction(GamePlayer player) {
		super(player);
	}
	
}//StixDrawCardAction
