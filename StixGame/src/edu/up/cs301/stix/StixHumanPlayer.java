package edu.up.cs301.stix;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.game.util.GameTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;

/**
 * A GUI that allows a human player to play Stix. The game is fixed to play at an landscape
 * orientation; holding the device in portrait mode doesn't change the orientation.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * 
 * @author T.J. Agne
 * @author Micah Alconcel
 * @author Bryce Matsuda
 * 
 * @version December 5, 2013
 */
public class StixHumanPlayer extends GameHumanPlayer implements StixPlayer, OnClickListener, OnTouchListener, 
android.content.DialogInterface.OnClickListener {

	/* instance variables for GUI */
	private TextView turn;
	private TextView timer;
	private TextView yourCardAmt;
	private TextView opp1CardAmt;
	private TextView fieldStatus;
	private TextView cardsRemain;
	private StixHandCanvas handCanvas;
	private StixFieldCanvas fieldCanvas;
	private Button commit;
	private Button mirrorVert;
	private Button mirrorHoriz;
	private Button shiftLeft;
	private Button shiftRight;
	private Button rotate;
	private Button scootUp;
	private Button scootLeft;
	private Button scootRight;
	private Button scootDown;
	private ImageView deckPic;
	private GameTimer turnTime;

	boolean[][] origField = new boolean[StixFieldCanvas.FIELD_SIZE][StixFieldCanvas.FIELD_SIZE];

	// the most recent game state, as given to us by the StixLocalGame
	protected StixState state;

	// the android activity that we are running
	private Activity myActivity;
	
	//a boolean that tells whether or not the player is in the middle of their turn
	//so that the popup doesn't appear multiple times
	private boolean inMiddleOfTurn = false;

	/**
	 * constructor
	 * @param name
	 * 		the player's name
	 */
	public StixHumanPlayer(String name) {
		super(name);
		turnTime = this.getTimer();
		turnTime.setInterval(1000); //tells us that the timer ticks every second
	}

	/**
	 * Returns the GUI's top view object
	 * 
	 * @return
	 * 		the top object in the GUI's view hierarchy
	 */
	public View getTopView() {
		return myActivity.findViewById(R.id.stixBoard);
	}

	/**
	 * perform any initialization that needs to be done after the player
	 * knows what their game-position and opponents' names are.
	 */
	protected void initAfterReady() {
		myActivity.setTitle("Stix: "+allPlayerNames[0]+" vs. "+allPlayerNames[1]);
	}
	
	/**
	 * this method gets called when the user clicks any of the buttons.	
	 * 
	 * @param v the button that was clicked
	 */
	public void onClick(View v) {
		// if we are not yet connected to a game, ignore
		if (game == null) return;

		// Construct the action and send it to the game

		if (v == commit)
		{
			// send request to update field for all players
			game.sendAction(new StixMoveStixAction(this, fieldCanvas.getField()));
			fieldCanvas.invalidate();

		}
		else if (v == rotate)
		{	
			// rotates field 90 degrees clockwise
			fieldCanvas.setField(this.rotate(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
		else if (v == mirrorHoriz)
		{
			// mirror field horizontally
			fieldCanvas.setField(this.flipVert(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
		else if (v == mirrorVert)
		{
			// mirror field vertically
			fieldCanvas.setField(this.flipHoriz(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
		else if (v == shiftLeft)
		{
			handCanvas.shiftLeft();
			handCanvas.invalidate();
		}
		else if (v == shiftRight)
		{
			handCanvas.shiftRight();
			handCanvas.invalidate();
		}
		else if (v == scootUp)
		{
			fieldCanvas.setField(this.scootUp(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
		else if (v == scootLeft)
		{
			fieldCanvas.setField(this.scootLeft(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
		else if (v == scootRight)
		{
			fieldCanvas.setField(this.scootRight(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
		else if (v == scootDown)
		{
			fieldCanvas.setField(this.scootDown(fieldCanvas.getField()));
			fieldCanvas.invalidate();
		}
	}// onClick

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		
	}

	public boolean onTouch (View v, MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			if (v == handCanvas)
			{
				int x = (int) event.getX();
				int idToDiscard = 0;

				// determine which card was touched and get its respective ID.
				if (x > 0 && x < 170)
				{
					idToDiscard = handCanvas.getSlotID(0);
				}
				else if (x >= 170 && x < 350)
				{
					idToDiscard = handCanvas.getSlotID(1);
				}
				else if (x >= 350 && x < 530)
				{
					idToDiscard = handCanvas.getSlotID(2);
				}
				else if (x >= 530 && x < 700)
				{
					idToDiscard = handCanvas.getSlotID(3);
				}
				else if (x >= 700 && x < 890)
				{
					idToDiscard = handCanvas.getSlotID(4);
				}
				else if (x >= 890)
				{
					idToDiscard = handCanvas.getSlotID(5);
				}

				// send discard action to the game
				inMiddleOfTurn=false;
				game.sendAction(new StixDiscardAction(this, idToDiscard));
			}
			else if (v == fieldCanvas)
			{
				int x = (int) event.getX();
				int y = (int) event.getY();
				fieldCanvas.handleTouch(x , y);

				return true;
			}
			else if (v == deckPic)
			{
				// draw a card
				inMiddleOfTurn=false;
				game.sendAction(new StixDrawCardAction(this));
				return true;
			}
		}

		return true;
	}

	/**
	 * callback method when we get a message (e.g., from the game)
	 * 
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
		builder.setPositiveButton("Ok", this);
		AlertDialog dialog = builder.create();
		dialog.setMessage("Your turn!");
		dialog.setCanceledOnTouchOutside(false);
		
		// ignore the message if it's not a StixState message
		if (!(info instanceof StixState))
		{
			return;
		}
		else if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo)
		{
			this.flash(Color.RED, 50);
			return;
		}
		else
		{
			// update our state; then update the display and reset the timer
			this.state = (StixState) info;
			Log.i("human player", "receiving");
			updateDisplay();
			if(state.getWhoseMove()==this.playerNum && !inMiddleOfTurn)
			{
				inMiddleOfTurn=true;
				turnTime.start();
				dialog.show();
			}
			else
			{
				turnTime.reset();
			}

		}
	}

	/** 
	 * updateDisplay - updates the player's GUI
	 * */
	public void updateDisplay()
	{
		if (this.state == null) return;

		// sets the field canvas to that of the state's
		this.fieldCanvas.setField(this.state.getGameField());
		this.fieldCanvas.invalidate();

		// sets new card amounts for each player and deck
		this.yourCardAmt.setText("Your card amount: " + String.valueOf(this.state.getHand(this.playerNum).size()));
		this.opp1CardAmt.setText("Your opponent's amount: " + String.valueOf(this.state.getHand(Math.abs(this.playerNum - 1)).size()));
		this.cardsRemain.setText("Cards remaining in deck: " + this.state.getDeckSize());
		
		// change text displaying if it is your turn to move or not.
		if (state.getWhoseMove() != this.playerNum)
		{
			turn.setText("Your opponent is making a move...");
		}
		else
		{
			turn.setText("Your turn to move!");
		}

		if (state.getFieldStatus() == true)
		{
			fieldStatus.setText("Field Status: Updated!");
		}
		else if (state.getFieldStatus() == false)
		{
			fieldStatus.setText("Field Status: Not updated.");
		}


		// wipe out old card hand array (to ensure only the most recent state info is used)
		this.handCanvas.clear(); 
		for (int x = 0; x < this.state.getHand(this.playerNum).size(); x++)
		{
			Card d = this.state.getHand(this.playerNum).getSlot(x);
			// re-add cards, including new ones
			if (d != null) this.handCanvas.addCard(d, x);
		}

		this.handCanvas.invalidate(); // re-draw canvas
	}

	/**
	 * callback method--our game has been chosen/rechosen to be the GUI,
	 * called from the GUI thread
	 * 
	 * @param activity
	 * 		the activity under which we are running
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		myActivity = activity;

		// Load the layout resource for our GUI
		activity.setContentView(R.layout.stix_human_player);
		turn = (TextView) activity.findViewById(R.id.isPlayersTurn);
		timer = (TextView) activity.findViewById(R.id.timer);
		yourCardAmt = (TextView) activity.findViewById(R.id.testOpp1);
		opp1CardAmt = (TextView) activity.findViewById(R.id.testOpp2);
		fieldStatus = (TextView) activity.findViewById(R.id.fieldUpdated);
		cardsRemain = (TextView) activity.findViewById(R.id.cardsRemaining);
		commit = (Button) activity.findViewById(R.id.commit);
		mirrorVert = (Button) activity.findViewById(R.id.mirrorVert);
		mirrorHoriz = (Button) activity.findViewById(R.id.mirrorHoriz);
		handCanvas = (StixHandCanvas) activity.findViewById(R.id.stixHandCanvas1);
		fieldCanvas = (StixFieldCanvas) activity.findViewById(R.id.stixFieldCanvas1);
		shiftLeft = (Button) activity.findViewById(R.id.shiftLeft);
		shiftRight = (Button) activity.findViewById(R.id.shiftRight);
		rotate = (Button) activity.findViewById(R.id.rotateButton);
		deckPic = (ImageView) activity.findViewById(R.id.imageView1);
		scootUp = (Button) activity.findViewById(R.id.shiftUp);
		scootLeft = (Button) activity.findViewById(R.id.scootLeft);
		scootRight = (Button) activity.findViewById(R.id.scootRight);
		scootDown = (Button) activity.findViewById(R.id.scootDown);

		// initialize card images
		Card.initImages(myActivity);

		// initialize listeners
		commit.setOnClickListener(this);
		mirrorVert.setOnClickListener(this);
		mirrorHoriz.setOnClickListener(this);
		shiftLeft.setOnClickListener(this);
		shiftRight.setOnClickListener(this);
		rotate.setOnClickListener(this);
		handCanvas.setOnTouchListener(this);
		fieldCanvas.setOnTouchListener(this);
		deckPic.setOnTouchListener(this);
		scootUp.setOnClickListener(this);
		scootLeft.setOnClickListener(this);
		scootRight.setOnClickListener(this);
		scootDown.setOnClickListener(this);

		// set timer
		timer.setText("Time Remaining: 60");


		// if we have a game state, "simulate" that we have just received
		// the state from the game so that the GUI values are updated
		if (state != null) {
			receiveInfo(state);
		}
	}

	/**
	 * This method takes an existing 13x13 boolean array and 
	 * rotates it counterclockwise
	 * 
	 * @param arr
	 * 		The existing 13x13 array to be rotated
	 * 
	 * @return 
	 * 		The rotated 13x13 array passed in
	 */
	public boolean[][] rotate(boolean[][] arr)
	{
		//creates a new temporary array to do rotation
		boolean[][] rotTemp = new boolean[13][13];

		//iterates over entire original array and copies
		//it to the new array
		for(int i=0; i<13; ++i)
		{
			for(int j=0; j<13; ++j)
			{
				//copies boolean from original to new
				rotTemp[j][12-i] = arr[i][j];

			}
		}

		return rotTemp;
	}

	/**
	 * This method takes an existing 13x13 array and flips it horizontally
	 * 
	 * @param arr
	 * 		The array to flip horizontally
	 * 
	 * @return
	 * 		The flipped array that was passed in
	 */
	public boolean[][] flipHoriz(boolean[][] arr)
	{
		//creates new temporary array
		boolean[][] flipHorizTemp = new boolean[13][13];

		//copies the boolean from original to new array
		for(int i=0; i < 13; ++i)
		{
			for(int j=0; j < 13; ++j)
			{
				flipHorizTemp[12-i][j] = arr[i][j];
			}
		}

		return flipHorizTemp;
	}

	/**
	 * This method takes an existing 13x13 array and flips it vertically
	 * 
	 * @param arr
	 * 		The array to flip
	 * 
	 * @return
	 * 		The flipped array that was passed in
	 */
	public boolean[][] flipVert(boolean[][] arr)
	{
		//creates new temporary array
		boolean[][] flipVertTemp = new boolean[13][13];

		//copies boolean from original to new array
		for(int i=0; i < 13; ++i)
		{
			for(int j=0; j < 13; ++j)
			{
				flipVertTemp[i][12-j] = arr[i][j];
			}
		}

		return flipVertTemp;
	}

	/**
	 * the timer was ticked. In this case, it happens every second after the timer has been started.
	 */
	@Override
	protected void timerTicked()
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
		AlertDialog dialog = builder.create();
		dialog.setMessage("Hurry up! It's still your move!");
		int time = 61-turnTime.getTicks();
		timer.setText("Time Remaining: "+ time);
		//if a player takes more than 60 seconds, remind the idiot that it is his turn.
		if(turnTime.getTicks()==60)
		{
			dialog.show();
			turnTime.reset();
		}
	}

	/**
	 * Shifts the elements of array up
	 * Used in GUI to prevent stix from moving off field
	 * 
	 * @param arr
	 * 		The array to be shifted
	 * @return
	 * 		The shifted array
	 */
	public boolean[][] scootUp(boolean[][] arr)
	{
		//makes a copy of the original
		boolean[][] shiftTemp = new boolean[13][13];
		for(int i=0;i<13;++i)
		{
			for(int j=0;j<13;++j)
			{
				shiftTemp[i][j] = arr[i][j];
			}
		}		

		//assumes that the array can be shifted up
		boolean canShiftUp = true;

		//looks through top two rows
		for(int i=0; i<2; ++i)
		{
			for(int j=0; j<13; ++j)
			{
				//if top two rows are not completely blank
				if(shiftTemp[i][j] == true)
				{
					canShiftUp = false;//set false
				}
			}
		}

		if(canShiftUp)
		{
			//iterates through rows 2-12
			for(int i=2; i < 13; ++i)
			{
				for(int j=0; j < 13; ++j)
				{
					//copies boolean from original to new array
					shiftTemp[i-2][j] = shiftTemp[i][j];
				}
			}

			//sets the bottom two rows of array to false (i.e. clears them)
			for(int i=11; i<13; ++i)
			{
				for(int j=0; j<13; ++j)
				{
					shiftTemp[i][j] = false;
				}
			}
		}	

		return shiftTemp;
	}

	/**
	 * Shifts the elements of array left
	 * Used in GUI to prevent stix from moving off field
	 * 
	 * @param arr
	 * 		The array to be shifted
	 * @return
	 * 		The shifted array
	 */
	public boolean[][] scootLeft(boolean[][] arr)
	{
		//makes a copy of the original
		boolean[][] shiftTemp = new boolean[13][13];
		for(int i=0;i<13;++i)
		{
			for(int j=0;j<13;++j)
			{
				shiftTemp[i][j] = arr[i][j];
			}
		}		

		//assumes that the array can be shifted left
		boolean canShiftLeft = true;

		//looks through left two rows
		for(int i=0; i<13; ++i)
		{
			for(int j=0; j<2; ++j)
			{
				//if left two rows are not completely blank
				if(shiftTemp[i][j] == true)
				{
					canShiftLeft = false;//set false
				}
			}
		}

		if(canShiftLeft)
		{
			//iterates through columns 2-12
			for(int i=0; i < 13; ++i)
			{
				for(int j=2; j < 13; ++j)
				{
					//copies boolean from original array to new array
					shiftTemp[i][j-2] = shiftTemp[i][j];
				}
			}

			//sets first two columns to false (i.e. clears them)
			for(int i=0; i<13; ++i)
			{
				for(int j=11; j<13; ++j)
				{
					shiftTemp[i][j] = false;
				}
			}
		}

		return shiftTemp;
	}

	/**
	 * Shifts the elements of array right
	 * Used in GUI to prevent stix from moving off field
	 * 
	 * @param arr
	 * 		The array to be shifted
	 * @return
	 * 		The shifted array
	 */
	public boolean[][] scootRight(boolean[][] arr)
	{
		//makes a copy of the original
		boolean[][] shiftTemp = new boolean[13][13];
		for(int i=0;i<13;++i)
		{
			for(int j=0;j<13;++j)
			{
				shiftTemp[i][j] = arr[i][j];
			}
		}		

		//assumes that the array can be shifted right
		boolean canShiftRight = true;

		//looks through last two rows
		for(int i=0; i<13; ++i)
		{
			for(int j=11; j<13; ++j)
			{
				//if last two rows are not completely blank
				if(shiftTemp[i][j] == true)
				{
					canShiftRight = false;//set false
				}
			}
		}

		if(canShiftRight)
		{
			//iterates through columns 12-2
			for(int i=0; i < 13; ++i)
			{
				for(int j=12; j > 1; --j)
				{
					//copies boolean from original array to new array
					shiftTemp[i][j] = shiftTemp[i][j-2];
				}
			}

			//sets first two columns to false (i.e. clears them)
			for(int i=0; i<13; ++i)
			{
				for(int j=0; j<2; ++j)
				{
					shiftTemp[i][j] = false;
				}
			}
		}

		return shiftTemp;
	}

	/**
	 * Shifts the elements of array down
	 * Used in GUI to prevent field from moving off field
	 * 
	 * @param arr
	 * 		The array to be shifted
	 * @return
	 * 		The shifted array
	 */
	public boolean[][] scootDown(boolean[][] arr)
	{
		//makes a copy of the original
		boolean[][] shiftTemp = new boolean[13][13];
		for(int i=0;i<13;++i)
		{
			for(int j=0;j<13;++j)
			{
				shiftTemp[i][j] = arr[i][j];
			}
		}		

		//assumes that the array can be shifted down
		boolean canShiftDown = true;

		//looks through bottom two rows
		for(int i=11; i<13; ++i)
		{
			for(int j=0; j<13; ++j)
			{
				//if bottom two rows are not completely blank
				if(shiftTemp[i][j] == true)
				{
					canShiftDown = false;//set false
				}
			}
		}

		if(canShiftDown)
		{
			//iterates through rows 12-2
			for(int i=12; i > 1; --i)
			{
				for(int j=0; j < 13; ++j)
				{
					//copies boolean from original to new array
					shiftTemp[i][j] = shiftTemp[i-2][j];
				}
			}
			
			//sets bottom two rows to false (i.e. clears them)
			for(int i=0; i<2; ++i)
			{
				for(int j=0; j<13; ++j)
				{
					shiftTemp[i][j] = false;
				}
			}
		}	

		return shiftTemp;
	}

}//StixHumanPlayer

