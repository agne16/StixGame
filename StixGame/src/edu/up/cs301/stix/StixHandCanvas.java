package edu.up.cs301.stix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * StixHandCanvas - canvas that displays the current cards in a player's hand.
 * 
 * @author Bryce Matsuda
 *
 */
public class StixHandCanvas extends SurfaceView{

	// initialize variables
	private int[] handOfIDs = new int[56];	// player's hand represented as an array of card IDs
	private Bitmap[] cardImages; // array of all card images
	private boolean init = true;
	private int amtCards = 0; // amount of cards in the players hand

	/**
	 * constructors
	 */
	public StixHandCanvas(Context context) {
		super(context);
	}

	public StixHandCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	/**
	 * 
	 * draws card images on canvas
	 *  
	 *  @param g the canvas to draw on
	 *  
	 *  */
	public void onDraw (Canvas g)
	{
		cardImages = Card.getCardImages();
		countCards(); 
		for (int x = 0; x < 6; x++)
		{
			if (init == true) // don't draw on first initialization, only draw once we get state info
			{
				init = false;
				return;
			}
			if (cardImages[handOfIDs[x]] != null && handOfIDs[x] > 0)
			{
				g.drawBitmap((cardImages[handOfIDs[x]]), x*cardImages[handOfIDs[x]].getWidth(),
						0, new Paint());
			}
		}
	}

	/** 
	 * addCard - adds one card to array
	 * 
	 * @param d the card to add
	 * @param x the slot to put the card into
	 * 
	 * */
	public void addCard(Card d, int x) {

		if (handOfIDs != null)
		{
			handOfIDs[x] = d.getCardID();
		}
	}
	
	/** 
	 * wipes hand array clean
	 * (to ensure no old values are left when updating new info from state)
	 * 
	 * */
	public void clear()
	{
		for (int c = 0; c < handOfIDs.length; c++)
		{
			handOfIDs[c] = 0;
		}
	}

	/**
	 * 
	 * allows player to move cards to the left to view all cards in hand
	 * (in case a player has 6 or more cards in his hand) 
	 * 
	 *  */
	public void shiftLeft() {
		countCards();
		if (amtCards >= 6)
		{
			for (int x = 0; x < amtCards - 1; x++)
			{
				int temp = handOfIDs[x];
				handOfIDs[x] = handOfIDs[x + 1];
				handOfIDs[x + 1] = temp;
			}
		}
	}
	
	/** 
	 * allows player to move cards to the right to view all cards in hand
	 * (in case a player has 6 or more cards in his hand) 
	 **/
	public void shiftRight() {
		if (amtCards >= 6)
		{
			int temp = handOfIDs[amtCards-1];
			for(int i = amtCards-1; i > 0; --i)
			{
				handOfIDs[i] = handOfIDs[i-1];
			}
			handOfIDs[0] = temp;
		}
	}

	/**
	 * 
	 * returns the card id at the current slot
	 * 
	 * @param slot to get ID from
	 * @return card ID in slot
	 *  
	 *  */
	public int getSlotID(int idx)
	{
		return handOfIDs[idx];
	}

	/**
	 * 
	 * counts the number of cards in player's hand
	 * @ return total amount of cards in player's hand
	 *  
	 */
	private int countCards()
	{
		amtCards = 0;

		for (int x = 0; x < handOfIDs.length; x++)
		{
			if (handOfIDs[x] > 0)
			{
				amtCards++;
			}
			else
			{
				return amtCards;
			}
		}

		return amtCards;
	}
}//StixHandCanvas
