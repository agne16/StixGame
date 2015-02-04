package edu.up.cs301.stix;

import java.io.Serializable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.up.cs301.game.R;

/**
 * A class that represents an instance of a Stix card object. 
 * 
 * @author T.J. Agne
 * @author Matthew Farr
 * @author Bryce Matsuda
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * 
 * @version December 6, 2013
 */
public class Card implements Serializable {

	//to satisfy serializable class
	private static final long serialVersionUID = 836572498713812104L;

	//instance variables
	private int cardID;
	private boolean[][] config;

	/**
	 * This is the constructor class for a card object
	 * 
	 * @param id
	 * 		An integer from 1...55 to identify this card
	 * 
	 * @param baseCase
	 * 		13x13 boolean array where a stix configuration is represented
	 * 
	 * @return Card
	 * 		returns a card object
	 */
	public Card(int id, boolean[][] baseCase)
	{
		this.cardID = id;
		this.config = baseCase;
	}


	//array of resource index of the card images
	private static int[] resIdx = {

		R.drawable.card_00, R.drawable.card_01, R.drawable.card_02, R.drawable.card_03,
		R.drawable.card_04, R.drawable.card_05, R.drawable.card_06, R.drawable.card_07, 
		R.drawable.card_08, R.drawable.card_09, R.drawable.card_10, R.drawable.card_11, 
		R.drawable.card_12, R.drawable.card_13, R.drawable.card_14, R.drawable.card_15, 
		R.drawable.card_16, R.drawable.card_17, R.drawable.card_18, R.drawable.card_19, 
		R.drawable.card_20, R.drawable.card_21, R.drawable.card_22, R.drawable.card_23, 
		R.drawable.card_24, R.drawable.card_25, R.drawable.card_26, R.drawable.card_27, 
		R.drawable.card_28, R.drawable.card_29, R.drawable.card_30, R.drawable.card_31, 
		R.drawable.card_32, R.drawable.card_33, R.drawable.card_34, R.drawable.card_35, 
		R.drawable.card_36, R.drawable.card_37, R.drawable.card_38, R.drawable.card_39, 
		R.drawable.card_40, R.drawable.card_41, R.drawable.card_42, R.drawable.card_43, 
		R.drawable.card_44, R.drawable.card_45, R.drawable.card_46, R.drawable.card_47, 
		R.drawable.card_48, R.drawable.card_49, R.drawable.card_50, R.drawable.card_51, 
		R.drawable.card_52, R.drawable.card_53, R.drawable.card_54, R.drawable.card_55
	};

	// the array of card images
	private static Bitmap[] cardImages = null;

	/**
	 * initializes the card images
	 * 
	 * @param activity
	 * 		the current activity
	 */
	public static void initImages(Activity activity) {
		// if it's already initialized, then ignore
		if (cardImages != null) return;

		// create the outer array
		cardImages = new Bitmap[resIdx.length];

		// loop through the resource-index array, creating a
		// "parallel" array with the images themselves
		for (int i = 0; i < resIdx.length; i++) {

			// create the bitmap from the corresponding image
			// resource, and set the corresponding array element
			cardImages[i] = BitmapFactory.decodeResource(
					activity.getResources(),
					resIdx[i]);
		}
	}

	/**
	 * Gets the ID of the current card
	 * 
	 * @return
	 * 		returns the ID of the current card
	 */
	public int getCardID() {

		return cardID;
	}

	/**
	 * Gets the array configuration of the card
	 * 
	 * @return
	 * 		returns a char[][] of the card configuration
	 */
	public boolean[][] getConfig(){

		return config;
	}

	/**
	 * Gets all card images
	 * 
	 * @return Bitmap[] containing all card images
	 */
	public static Bitmap[] getCardImages() {
		return cardImages;
	}
}//Card
