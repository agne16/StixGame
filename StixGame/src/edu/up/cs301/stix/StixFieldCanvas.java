package edu.up.cs301.stix;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * StixFieldCanvas
 * 
 * This class handles the display of the current arrangement of sticks on the field
 * to the human player
 * 
 * @author Matthew Farr
 *
 */
public class StixFieldCanvas extends SurfaceView {

	//instance variables
	public static final int FIELD_SIZE = 13;
	private boolean[][] field = new boolean[FIELD_SIZE][FIELD_SIZE];
	private ArrayList<Rect> stixPositions;
	private int width;
	private int height;
	private int stixWidth;
	private int stixLength;
	private int sideLength;
	private Paint paint;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public StixFieldCanvas(Context context) {
		super(context);
	}

	/**
	 * Second Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public StixFieldCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * onDraw method
	 * Determines behavior for drawing on sticks on the canvas
	 * 
	 * @param canvas
	 */
	public void onDraw(Canvas canvas)
	{
		/*
		 * This section of the code gets the height and width of the canvas, then sets
		 * whichever is smaller to be the length and width of the square playing area
		 * on which the sticks are displayed
		 */
		width = canvas.getWidth();
		height = canvas.getHeight();
		if(width > height){
			sideLength = height;
		}
		else{
			sideLength = width;
		}

		setUpStixPositions();

		paint = new Paint();
		paint.setColor(Color.BLACK);

		Paint faintgray = new Paint();
		faintgray.setColor(0xffe8e8e8);


		/*
		 * Iterates over all the positions in the field array, and if the boolean stored at
		 * the position is true, then it draws the corresponding stick in the rectangle ArrayList.
		 */
		int i = 0;
		for(int y=0 ; y < field.length ; y++){
			for(int x=0 ; x<field[y].length ; x++){
				if( ((y%2 == 0) && (x%2 != 0)) || ((y%2 != 0) && (x%2 == 0)) ){
					if(field[y][x]){						
						canvas.drawRect(stixPositions.get(i) , paint);
					}
					else{
						canvas.drawRect(stixPositions.get(i) , faintgray);
					}
					i++;
				}

			}
		}

	}

	/**
	 * This method sets up a series of rectangles which represent the sticks.
	 */
	private void setUpStixPositions(){

		stixLength = sideLength / 8;
		stixWidth = stixLength / 6;

		stixPositions = new ArrayList<Rect>();

		int topEdge = 0;
		int leftEdge;

		/*
		 * This loop iterates 13 times for each of the 13 rows of sticks
		 * For even numbered rows, it sets up six vertical rectangles
		 * For odd numbered rows, it sets up seven horizontal rectangles
		 */
		for (int i=0 ; i<FIELD_SIZE ; i++){

			if(i%2 == 0){
				leftEdge = stixLength;
				for(int j=0 ; j<6 ; j++){
					stixPositions.add(new Rect(leftEdge , topEdge , leftEdge + stixWidth , topEdge + stixLength));
					leftEdge = leftEdge + stixWidth + stixLength;
				}
				topEdge = topEdge + stixLength;
			}

			if(i%2 != 0){
				leftEdge = 0;
				for(int k=0 ; k<7 ; k++){
					stixPositions.add(new Rect(leftEdge , topEdge , leftEdge + stixLength, topEdge + stixWidth));
					leftEdge = leftEdge + stixWidth + stixLength;
				}
				topEdge = topEdge + stixWidth;
			}
		}

	}

	/**
	 * Takes an x and y position of a touch and if it's close to one of the
	 * sticks on the field, toggles the position of that boolean to update
	 * the field and either add or remove that stick
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void handleTouch(int xPos , int yPos){
		for(int i=0 ; i<stixPositions.size(); i++){
			
			/*
			 * For each of the stick positions, creates a rectangle around
			 * the center of that stick and checks if that rectangle contains
			 * the point
			 */
			Rect touchRect;
			int centerX = stixPositions.get(i).centerX();
			int centerY = stixPositions.get(i).centerY();
			touchRect = new Rect(centerX - stixLength/4 , centerY - stixLength/4 ,
					centerX + stixLength/4 , centerY + stixLength/4 );
			
			/*
			 * if the rectangle contains the point, then this section of the 
			 * code maps the rectangle to the a stick position
			 */
			if(touchRect.contains(xPos , yPos)){
				int stixY = 0;
				int stixX = 0;
				if(i>=0 && i<6){
					stixY = 0;
					stixX = 2*i+1;
				}
				if(i>5 && i<13){
					stixY = 1;
					stixX = (i-6)*2;
				}
				if(i>12 && i<19){
					stixY = 2;
					stixX = (i-13)*2+1;
				}
				if(i>18 && i<26){
					stixY = 3;
					stixX = (i-19)*2;
				}
				if(i>25 && i<32){
					stixY = 4;
					stixX = (i-26)*2+1;
				}
				if(i>31 && i<39){
					stixY = 5;
					stixX = (i-32)*2;
				}
				if(i>38 && i<45){
					stixY = 6;
					stixX = (i-39)*2+1;
				}
				if(i>44 && i<52){
					stixY = 7;
					stixX = (i-45)*2;
				}
				if(i>51 && i<58){
					stixY = 8;
					stixX = (i-52)*2+1;
				}
				if(i>57 && i<65){
					stixY = 9;
					stixX = (i-58)*2;
				}
				if(i>64 && i<71){
					stixY = 10;
					stixX = (i-65)*2+1;
				}
				if(i>70 && i<78){
					stixY = 11;
					stixX = (i-71)*2;
				}
				if(i>77 && i<84){
					stixY = 12;
					stixX = (i-78)*2+1;
				}
				//toggles the selected boolean
				field[stixY][stixX] = !field[stixY][stixX];
				invalidate();return;
			}
		}

	}

	/**
	 * Sets the field to match the given field config
	 * 
	 * @param field the config to set
	 */
	public void setField(boolean[][] field)
	{
		this.field = field;
	}

	/**
	 * Returns the field config
	 *  	 
	 * @return field the current field config of the canvas
	 */
	public boolean[][] getField()
	{
		return field;
	}

}