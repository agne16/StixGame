package edu.up.cs301.stix;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A StixMoveAction is a general action of a StixGame. 
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * 
 * @author Micah Alconcel
 * 
 * @version November 2013
 */
public abstract class StixMoveAction extends GameAction {

	// to satisfy the serializable interface
	private static final long serialVersionUID = -107694601639284154L;

	/**
	 * Constructor for the StixMoveAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public StixMoveAction(GamePlayer player) {
		super(player);
	}
}//class StixMoveAction
