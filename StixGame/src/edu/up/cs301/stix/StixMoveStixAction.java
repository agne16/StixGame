package edu.up.cs301.stix;

import edu.up.cs301.game.GamePlayer;

/**
 * A StixMoveStixAction  occurs whenever a player has moved a stick to a new position on the field
 * (which may or may not be legal)
 * 
 * @author Bryce Matsuda
 *
 */
public class StixMoveStixAction extends StixMoveAction {

	// satisfy Serializable interface
	private static final long serialVersionUID = 5605049290391945879L;

	// board config to update
	boolean[][] field = new boolean[StixFieldCanvas.FIELD_SIZE][StixFieldCanvas.FIELD_SIZE];
	
	/**
	 * Constructor for a StixMoveStixAction
	 * @param player - the player making the move
	 * @param stixField - the field config to send to all players
	 */
	public StixMoveStixAction(GamePlayer player, boolean[][] stixField) {
		super(player);
		this.field = stixField;
	}
	
	/**
	 * Getter method for the field being sent to the game state
	 * @return the field being sent
	 */
	public boolean[][] getField()
	{
		return field;
	}

}//StixMoveStixAction
