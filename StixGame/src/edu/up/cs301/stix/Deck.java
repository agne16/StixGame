package edu.up.cs301.stix;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that represents an instance of a deck of stix cards.
 * An instance will act as the main deck, hands, and discard piles. 
 * 
 * @author T.J. Agne
 * @author Matthew Farr
 * @author Bryce Matsuda
 * @author Micah Alconcel
 * 
 * @author Steven R. Vegdahl
 * @version November 2013
 */
public class Deck implements Serializable {

	//to satisfy serializable class
	private static final long serialVersionUID = -2409142908658583396L;

	// ArrayList to represent the deck
	private ArrayList<Card> cards;

	/**
	 * constructor, creating an empty deck
	 */
	public Deck() {
		cards = new ArrayList<Card>();
	}

	/** copy constructor, making an exact copy of a deck
	 * 
	 * @param orig
	 * 		the deck from which the copy should be made
	 */
	public Deck(Deck orig) {
		// synchronize to ensure that original is not being modified as we
		// iterate over it
		synchronized(orig.cards) {
			// create a new arrayList for our new deck; add each card in it
			cards = new ArrayList<Card>();
			for (Card c: orig.cards) {
				cards.add(c);
			}
		}
	}

	/**
	 * Shuffles the deck
	 * 
	 * @return
	 * 		returns a shuffled deck
	 */
	public Deck shuffle() {
		// synchronize so that we don't have someone trying to modify the
		// deck as we're modifying it
		synchronized (this.cards) {
			// go through a loop that randomly rearranges the cards
			for (int i = cards.size(); i > 1; i--) {
				int spot = (int)(i*Math.random());
				Card temp = cards.get(spot);
				cards.set(spot, cards.get(i-1));
				cards.set(i-1, temp);
			}
		}

		// return the deck
		return this;
	}

	/**
	 * get card from deck at specified position
	 * 
	 * @param slot
	 * 		idx to get card from deck
	 * 
	 * @return
	 * 		a card
	 */
	public Card getSlot(int slot)
	{
		return cards.get(slot);
	}

	/**
	 * get card by id
	 * 
	 * @param id
	 * 		The id of card to search for
	 * 
	 * @return
	 * 		The card with corresponding id
	 */
	public Card getCardById(int id)
	{
		for (Card c: cards)
		{
			if (c.getCardID() == id)
			{
				return c;
			}
		}

		return null;
	}

	/**
	 * Gets a card object from a partcular position in a deck
	 * 
	 * @param x 
	 * 		the position in deck to look at
	 * @return  
	 * 		a Card object
	 */
	public Card getCardAtSlot(int x) {
		return cards.get(x);
	}

	/**
	 * @return
	 * 		the number of cards in the deck
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * remove the top card from the deck
	 * 
	 * @return
	 * 		the top card in the deck, which is also removed,
	 * 		or null if the deck was empty
	 */
	public Card removeTopCard() {
		synchronized (this.cards) {
			if (cards.isEmpty()) return null;
			return cards.remove(cards.size()-1);
		}
	}

	/**
	 * get the top card of Deck
	 * 
	 * @return
	 * 		The top card of deck
	 */
	public Card getTopCard()
	{
		synchronized (this.cards) {
			if (cards.isEmpty()) return null;
			return cards.get(cards.size()-1);
		}
	}

	/**
	 * add a card to the top of a deck
	 * 
	 * @param c
	 * 		the card to add
	 */
	public void add(Card c) {
		// synchronize so that the underlying ArrayList is not accessed
		// inconsistently
		synchronized(this.cards) {
			cards.add(c);
		}
	}

	/**
	 * Removes specified card from deck
	 * 
	 * @param rCard
	 * 		The card to remove
	 */
	public void remove(Card rCard) {
		cards.remove(rCard);
	}

	/**
	 * Adds card to deck
	 * 
	 * @param c
	 * 		The card to add
	 * @param idx
	 * 		The position to add to inside arraylist
	 */
	public void add(Card c, int idx) {
		synchronized(this.cards) {
			cards.add(idx, c);
		}
	}

	/**
	 *  init method called in StixState
	 *  it establishes all 55 card configuration
	 *  (Card 0 is left blank since an empty slot in the hand canvas int array is 0)
	 *  55 Card objects are created and added the the Deck ArrayList
	 */
	public void init()
	{
		
		/*
		 * Creates 55 card configurations and an empty card.
		 * 
		 * 13x13 array is initialize as all false
		 * a true signifies whether a stick exists where:
		 * 
		 * vert  stick is represented by an idx of [even][odd]
		 * horiz stick is represented by an idx of [odd][even]
		 * 
		 * Indices [even][even] and [odd][odd] are unused
		 * 
		 */
		boolean[][] baseCase0 = new boolean[13][13];
		
		boolean[][] baseCase1 = new boolean[13][13];
		baseCase1[2][3] = true;
		baseCase1[4][3] = true;
		baseCase1[6][3] = true;
		baseCase1[8][3] = true;
		baseCase1[10][3] = true;
		
		boolean[][] baseCase2 = new boolean[13][13];
		baseCase2[2][3] = true;
		baseCase2[3][2] = true;
		baseCase2[4][3] = true;
		baseCase2[6][3] = true;
		baseCase2[8][3] = true;
		
		boolean[][] baseCase3 = new boolean[13][13];
		baseCase3[3][2] = true;
		baseCase3[4][3] = true;
		baseCase3[6][3] = true;
		baseCase3[8][3] = true;
		baseCase3[10][3] = true;
		
		boolean[][] baseCase4 = new boolean[13][13];
		baseCase4[2][3] = true;
		baseCase4[4][3] = true;
		baseCase4[5][4] = true;
		baseCase4[6][3] = true;
		baseCase4[8][3] = true;
		
		boolean[][] baseCase5 = new boolean[13][13];
		baseCase5[2][3] = true;
		baseCase5[4][3] = true;
		baseCase5[5][2] = true;
		baseCase5[6][3] = true;
		baseCase5[7][4] = true;
		
		boolean[][] baseCase6 = new boolean[13][13];
		baseCase6[3][4] = true;
		baseCase6[4][3] = true;
		baseCase6[4][5] = true;
		baseCase6[6][3] = true;
		baseCase6[8][3] = true;
		
		boolean[][] baseCase7 = new boolean[13][13];
		baseCase7[2][3] = true;
		baseCase7[4][3] = true;
		baseCase7[6][3] = true;
		baseCase7[7][4] = true;
		baseCase7[7][6] = true;
		
		boolean[][] baseCase8 = new boolean[13][13];
		baseCase8[2][3] = true;
		baseCase8[4][3] = true;
		baseCase8[6][3] = true;
		baseCase8[7][4] = true;
		baseCase8[8][5] = true;
		
		boolean[][] baseCase9 = new boolean[13][13];
		baseCase9[3][2] = true;
		baseCase9[4][3] = true;
		baseCase9[6][3] = true;
		baseCase9[7][4] = true;
		baseCase9[8][3] = true;
		
		boolean[][] baseCase10 = new boolean[13][13];
		baseCase10[2][3] = true;
		baseCase10[4][3] = true;
		baseCase10[6][3] = true;
		baseCase10[7][2] = true;
		baseCase10[7][4] = true;
		
		boolean[][] baseCase11 = new boolean[13][13];
		baseCase11[3][4] = true;
		baseCase11[4][3] = true;
		baseCase11[6][3] = true;
		baseCase11[8][3] = true;
		baseCase11[9][4] = true;
		
		boolean[][] baseCase12 = new boolean[13][13];
		baseCase12[3][2] = true;
		baseCase12[4][3] = true;
		baseCase12[6][3] = true;
		baseCase12[8][3] = true;
		baseCase12[9][4] = true;
		
		boolean[][] baseCase13 = new boolean[13][13];
		baseCase13[2][3] = true;
		baseCase13[3][2] = true;
		baseCase13[4][3] = true;
		baseCase13[6][3] = true;
		baseCase13[7][2] = true;
		
		boolean[][] baseCase14 = new boolean[13][13];
		baseCase14[2][3] = true;
		baseCase14[2][5] = true;
		baseCase14[4][3] = true;
		baseCase14[4][5] = true;
		baseCase14[5][4] = true;
		
		boolean[][] baseCase15 = new boolean[13][13];
		baseCase15[3][4] = true;
		baseCase15[4][3] = true;
		baseCase15[4][5] = true;
		baseCase15[6][3] = true;
		baseCase15[7][2] = true;
		
		boolean[][] baseCase16 = new boolean[13][13];
		baseCase16[3][4] = true;
		baseCase16[4][3] = true;
		baseCase16[4][5] = true;
		baseCase16[5][2] = true;
		baseCase16[6][3] = true;
		
		boolean[][] baseCase17 = new boolean[13][13];
		baseCase17[3][4] = true;
		baseCase17[4][3] = true;
		baseCase17[4][5] = true;
		baseCase17[5][6] = true;
		baseCase17[6][3] = true;
		
		boolean[][] baseCase18 = new boolean[13][13];
		baseCase18[3][4] = true;
		baseCase18[4][5] = true;
		baseCase18[6][3] = true;
		baseCase18[6][5] = true;
		baseCase18[7][4] = true;
		
		boolean[][] baseCase19 = new boolean[13][13];
		baseCase19[2][3] = true;
		baseCase19[3][4] = true;
		baseCase19[4][3] = true;
		baseCase19[4][5] = true;
		baseCase19[5][4] = true;
		
		boolean[][] baseCase20 = new boolean[13][13];
		baseCase20[2][3] = true;
		baseCase20[3][4] = true;
		baseCase20[4][5] = true;
		baseCase20[5][4] = true;
		baseCase20[6][3] = true;
		
		boolean[][] baseCase21 = new boolean[13][13];
		baseCase21[3][4] = true;
		baseCase21[4][3] = true;
		baseCase21[5][4] = true;
		baseCase21[6][5] = true;
		baseCase21[7][4] = true;
		
		boolean[][] baseCase22 = new boolean[13][13];
		baseCase22[3][2] = true;
		baseCase22[4][3] = true;
		baseCase22[5][4] = true;
		baseCase22[6][3] = true;
		baseCase22[7][4] = true;
		
		boolean[][] baseCase23 = new boolean[13][13];
		baseCase23[3][4] = true;
		baseCase23[3][6] = true;
		baseCase23[4][3] = true;
		baseCase23[4][5] = true;
		baseCase23[6][3] = true;
		
		boolean[][] baseCase24 = new boolean[13][13];
		baseCase24[3][4] = true;
		baseCase24[3][6] = true;
		baseCase24[4][3] = true;
		baseCase24[4][7] = true;
		baseCase24[6][7] = true;
		
		boolean[][] baseCase25 = new boolean[13][13];
		baseCase25[3][2] = true;
		baseCase25[4][3] = true;
		baseCase25[5][2] = true;
		baseCase25[6][3] = true;
		baseCase25[7][2] = true;
		
		boolean[][] baseCase26 = new boolean[13][13];
		baseCase26[2][3] = true;
		baseCase26[3][4] = true;
		baseCase26[3][6] = true;
		baseCase26[4][7] = true;
		baseCase26[6][7] = true;
		
		boolean[][] baseCase27 = new boolean[13][13];
		baseCase27[3][2] = true;
		baseCase27[3][4] = true;
		baseCase27[4][5] = true;
		baseCase27[5][6] = true;
		baseCase27[6][5] = true;
		
		boolean[][] baseCase28 = new boolean[13][13];
		baseCase28[2][3] = true;
		baseCase28[3][2] = true;
		baseCase28[4][3] = true;
		baseCase28[5][2] = true;
		baseCase28[5][4] = true;
		
		boolean[][] baseCase29 = new boolean[13][13];
		baseCase29[3][2] = true;
		baseCase29[3][4] = true;
		baseCase29[4][3] = true;
		baseCase29[6][3] = true;
		baseCase29[7][4] = true;
		
		boolean[][] baseCase30 = new boolean[13][13];
		baseCase30[3][4] = true;
		baseCase30[4][3] = true;
		baseCase30[6][3] = true;
		baseCase30[7][4] = true;
		baseCase30[8][5] = true;
		
		boolean[][] baseCase31 = new boolean[13][13];
		baseCase31[2][3] = true;
		baseCase31[3][4] = true;
		baseCase31[4][3] = true;
		baseCase31[5][4] = true;
		baseCase31[6][5] = true;
		
		boolean[][] baseCase32 = new boolean[13][13];
		baseCase32[2][3] = true;
		baseCase32[2][7] = true;
		baseCase32[3][4] = true;
		baseCase32[3][6] = true;
		baseCase32[4][5] = true;
		
		boolean[][] baseCase33 = new boolean[13][13];
		baseCase33[3][4] = true;
		baseCase33[4][3] = true;
		baseCase33[4][5] = true;
		baseCase33[5][6] = true;
		baseCase33[6][7] = true;
		
		boolean[][] baseCase34 = new boolean[13][13];
		baseCase34[3][4] = true;
		baseCase34[4][3] = true;
		baseCase34[5][4] = true;
		baseCase34[6][5] = true;
		baseCase34[8][5] = true;
		
		boolean[][] baseCase35 = new boolean[13][13];
		baseCase35[2][3] = true;
		baseCase35[4][3] = true;
		baseCase35[5][4] = true;
		baseCase35[6][5] = true;
		baseCase35[8][5] = true;
		
		boolean[][] baseCase36 = new boolean[13][13];
		baseCase36[2][3] = true;
		baseCase36[4][3] = true;
		baseCase36[5][4] = true;
		baseCase36[6][5] = true;
		baseCase36[7][6] = true;
		
		boolean[][] baseCase37 = new boolean[13][13];
		baseCase37[2][3] = true;
		baseCase37[3][4] = true;
		baseCase37[4][5] = true;
		baseCase37[5][6] = true;
		baseCase37[6][7] = true;
		
		boolean[][] baseCase38 = new boolean[13][13];
		baseCase38[2][3] = true;
		baseCase38[3][4] = true;
		baseCase38[4][5] = true;
		baseCase38[5][6] = true;
		baseCase38[6][5] = true;
		
		boolean[][] baseCase39 = new boolean[13][13];
		baseCase39[2][3] = true;
		baseCase39[3][4] = true;
		baseCase39[4][5] = true;
		baseCase39[6][5] = true;
		baseCase39[7][6] = true;
		
		boolean[][] baseCase40 = new boolean[13][13];
		baseCase40[2][3] = true;
		baseCase40[3][2] = true;
		baseCase40[3][4] = true;
		baseCase40[4][3] = true;
		baseCase40[6][3] = true;
		
		boolean[][] baseCase41 = new boolean[13][13];
		baseCase41[2][5] = true;
		baseCase41[4][3] = true;
		baseCase41[4][5] = true;
		baseCase41[5][4] = true;
		baseCase41[5][6] = true;
		
		boolean[][] baseCase42 = new boolean[13][13];
		baseCase42[2][5] = true;
		baseCase42[3][2] = true;
		baseCase42[3][4] = true;
		baseCase42[4][5] = true;
		baseCase42[6][5] = true;
		
		boolean[][] baseCase43 = new boolean[13][13];
		baseCase43[2][3] = true;
		baseCase43[3][2] = true;
		baseCase43[4][3] = true;
		baseCase43[5][4] = true;
		baseCase43[6][3] = true;
		
		boolean[][] baseCase44 = new boolean[13][13];
		baseCase44[2][3] = true;
		baseCase44[3][4] = true;
		baseCase44[4][3] = true;
		baseCase44[5][4] = true;
		baseCase44[6][3] = true;
		
		boolean[][] baseCase45 = new boolean[13][13];
		baseCase45[3][4] = true;
		baseCase45[4][3] = true;
		baseCase45[5][4] = true;
		baseCase45[6][3] = true;
		baseCase45[8][3] = true;
		
		boolean[][] baseCase46 = new boolean[13][13];
		baseCase46[2][5] = true;
		baseCase46[4][5] = true;
		baseCase46[5][4] = true;
		baseCase46[5][6] = true;
		baseCase46[6][3] = true;
		
		boolean[][] baseCase47 = new boolean[13][13];
		baseCase47[2][3] = true;
		baseCase47[4][3] = true;
		baseCase47[4][5] = true;
		baseCase47[5][4] = true;
		baseCase47[6][3] = true;
		
		boolean[][] baseCase48 = new boolean[13][13];
		baseCase48[2][3] = true;
		baseCase48[2][5] = true;
		baseCase48[3][4] = true;
		baseCase48[4][3] = true;
		baseCase48[4][5] = true;
		
		boolean[][] baseCase49 = new boolean[13][13];
		baseCase49[2][3] = true;
		baseCase49[3][4] = true;
		baseCase49[4][5] = true;
		baseCase49[5][4] = true;
		baseCase49[5][6] = true;
		
		boolean[][] baseCase50 = new boolean[13][13];
		baseCase50[3][4] = true;
		baseCase50[4][5] = true;
		baseCase50[5][4] = true;
		baseCase50[6][3] = true;
		baseCase50[6][5] = true;
		
		boolean[][] baseCase51 = new boolean[13][13];
		baseCase51[2][3] = true;
		baseCase51[2][5] = true;
		baseCase51[3][4] = true;
		baseCase51[4][5] = true;
		baseCase51[6][5] = true;
		
		boolean[][] baseCase52 = new boolean[13][13];
		baseCase52[2][3] = true;
		baseCase52[2][5] = true;
		baseCase52[3][4] = true;
		baseCase52[4][5] = true;
		baseCase52[5][6] = true;
		
		boolean[][] baseCase53 = new boolean[13][13];
		baseCase53[3][4] = true;
		baseCase53[4][3] = true;
		baseCase53[5][2] = true;
		baseCase53[5][4] = true;
		baseCase53[6][5] = true;
		
		boolean[][] baseCase54 = new boolean[13][13];
		baseCase54[2][5] = true;
		baseCase54[3][4] = true;
		baseCase54[4][3] = true;
		baseCase54[4][5] = true;
		baseCase54[6][3] = true;
		
		boolean[][] baseCase55 = new boolean[13][13];
		baseCase55[3][2] = true;
		baseCase55[4][3] = true;
		baseCase55[5][2] = true;
		baseCase55[5][4] = true;
		baseCase55[6][3] = true;
		
		/*
		 * 56 Card objects are created by providing an ID and config
		 */
		Card card0 = new Card(0, baseCase0);
		Card card1 = new Card(1, baseCase1);
		Card card2 = new Card(2, baseCase2);
		Card card3 = new Card(3, baseCase3);
		Card card4 = new Card(4, baseCase4);
		Card card5 = new Card(5, baseCase5);
		Card card6 = new Card(6, baseCase6);
		Card card7 = new Card(7, baseCase7);
		Card card8 = new Card(8, baseCase8);
		Card card9 = new Card(9, baseCase9);
		Card card10 = new Card(10, baseCase10);
		Card card11 = new Card(11, baseCase11);
		Card card12 = new Card(12, baseCase12);
		Card card13 = new Card(13, baseCase13);
		Card card14 = new Card(14, baseCase14);
		Card card15 = new Card(15, baseCase15);
		Card card16 = new Card(16, baseCase16);
		Card card17 = new Card(17, baseCase17);
		Card card18 = new Card(18, baseCase18);
		Card card19 = new Card(19, baseCase19);
		Card card20 = new Card(20, baseCase20);
		Card card21 = new Card(21, baseCase21);
		Card card22 = new Card(22, baseCase22);
		Card card23 = new Card(23, baseCase23);
		Card card24 = new Card(24, baseCase24);
		Card card25 = new Card(25, baseCase25);
		Card card26 = new Card(26, baseCase26);
		Card card27 = new Card(27, baseCase27);
		Card card28 = new Card(28, baseCase28);
		Card card29 = new Card(29, baseCase29);
		Card card30 = new Card(30, baseCase30);
		Card card31 = new Card(31, baseCase31);
		Card card32 = new Card(32, baseCase32);
		Card card33 = new Card(33, baseCase33);
		Card card34 = new Card(34, baseCase34);
		Card card35 = new Card(35, baseCase35);
		Card card36 = new Card(36, baseCase36);
		Card card37 = new Card(37, baseCase37);
		Card card38 = new Card(38, baseCase38);
		Card card39 = new Card(39, baseCase39);
		Card card40 = new Card(40, baseCase40);
		Card card41 = new Card(41, baseCase41);
		Card card42 = new Card(42, baseCase42);
		Card card43 = new Card(43, baseCase43);
		Card card44 = new Card(44, baseCase44);
		Card card45 = new Card(45, baseCase45);
		Card card46 = new Card(46, baseCase46);
		Card card47 = new Card(47, baseCase47);
		Card card48 = new Card(48, baseCase48);
		Card card49 = new Card(49, baseCase49);
		Card card50 = new Card(50, baseCase50);
		Card card51 = new Card(51, baseCase51);
		Card card52 = new Card(52, baseCase52);
		Card card53 = new Card(53, baseCase53);
		Card card54 = new Card(54, baseCase54);
		Card card55 = new Card(55, baseCase55);
		
		/*
		 * all 55 cards (card 0 is excluded) is added to deck
		 */
		add(card1);
		add(card2);
		add(card3);
		add(card4);
		add(card5);
		add(card6);
		add(card7);
		add(card8);
		add(card9);
		add(card10);
		add(card11);
		add(card12);
		add(card13);
		add(card14);
		add(card15);
		add(card16);
		add(card17);
		add(card18);
		add(card19);
		add(card20);
		add(card21);
		add(card22);
		add(card23);
		add(card24);
		add(card25);
		add(card26);
		add(card27);
		add(card28);
		add(card29);
		add(card30);
		add(card31);
		add(card32);
		add(card33);
		add(card34);
		add(card35);
		add(card36);
		add(card37);
		add(card38);
		add(card39);
		add(card40);
		add(card41);
		add(card42);
		add(card43);
		add(card44);
		add(card45);
		add(card46);
		add(card47);
		add(card48);
		add(card49);
		add(card50);
		add(card51);
		add(card52);
		add(card53);
		add(card54);
		add(card55);		
	}//init()

}//Deck
