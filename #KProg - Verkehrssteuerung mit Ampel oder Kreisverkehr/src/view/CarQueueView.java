package view;

import java.awt.Color;

import javax.swing.BorderFactory;

import model.Coordinates;
import model.Station;
import model.Station.QueueType;
/**
 * @author Colt, Verghelet
 * Class of the view of the queues.
 *
 */
@SuppressWarnings("serial")
public class CarQueueView extends TextView {
	/**The size of the station-image in pixel*/
	private static final int STATION_IMAGE_SIZE = 72;
	
	/**The distance from the view to the image*/
	private static final int DISTANCE = 30;
	
	/**The offset of the OutQueues, that stand next to the inQueues*/
	private static final int OFFSET = 30;
	
	/** The type of this queue */
	private final QueueType QUEUE_TYPE; 
	
	/** amount of cars for the queue to be considered crowded */
	private final int QUEUE_CROWDED = 8;

	
	/**
	 * @see create() method
	 * @param text
	 * @param type
	 * @param xPos
	 * @param yPos
	 */
	private CarQueueView(String text,QueueType type, int xPos, int yPos) {
		
		super(text, xPos, yPos);
		QUEUE_TYPE = type;
		if(QUEUE_TYPE == QueueType.INQUEUE)
			this.setBorder(BorderFactory.createLineBorder(new Color(65,105,225),5));
	}
	
	/**
	 * Creates a queue view with a label, which is placed on one of the 4 sides of the station.
	 * (WEST,SOUTH,EAST,NORTH)
	 * @param coord the position of the CarQueView (EAST, WEST, SOUTH, NORTH)
	 * @param type Type of Queue (in or out)
	 * @param text text to be displayed
	 * @param station the station to which it belongs
	 * @return CarQueueView
	 */
	public static CarQueueView create(Coordinates coord, QueueType type, String text, Station station) {

		// the views x-, y-position
		int xPos = 0;
		int yPos = 0;

		// do we have an incoming queue or an outgoing queue
		switch (type) {
		case INQUEUE:
			xPos = station.getXPos() + getXByDirection(coord);
			yPos = station.getYPos() + getYByDirection(coord);
			break;
		case OUTQUEUE:
			xPos = station.getXPos() + getXByDirection(coord) + getX_OffSet(coord) * OFFSET;
			yPos = station.getYPos() + getYByDirection(coord) + getY_OffSet(coord) * OFFSET;
			break;

		}

		return new CarQueueView(text,type, xPos, yPos);

	}
	/**
	 * get the X coordinate taking into consideration the side on which the queue is placed
	 * relative to the station
	 * @param coord
	 * @return int X coordinate of the queue
	 */
	private static int getXByDirection(Coordinates coord) {
		switch (coord) {
		case WEST:
			return -DISTANCE;
		case NORTH:
			return 0;
		case EAST:
			return STATION_IMAGE_SIZE;
		case SOUTH:
			return DISTANCE;
		}
		return 0;
	}
	/**
	 * get the Y coordinate taking into consideration the side on which the queue is placed
	 * relative to the station
	 * @param coord
	 * @return int Y coordinate of the queue
	 */
	private static int getYByDirection(Coordinates coord) {
		switch (coord) {
		case WEST:
			return DISTANCE;
		case NORTH:
			return -DISTANCE;
		case EAST:
			return 0;
		case SOUTH:
			return STATION_IMAGE_SIZE;
		}
		return 0;
	}
	/**
	 * Get the X offset of the outqueue relative the the inqueue, taking into consideration the side
	 * @param coord
	 * @return int the x offset for the outqueue
	 */
	private static int getX_OffSet(Coordinates coord) {
		switch (coord) {
		case WEST:
			return 0;
		case NORTH:
			return 1;
		case EAST:
			return 0;
		case SOUTH:
			return -1;
		}
		return 0;
	}
	/**
	 * Get the Y offset of the outqueue relative the the inqueue, taking into consideration the side
	 * @param coord
	 * @return int the y offset for the outqueue
	 */
	private static int getY_OffSet(Coordinates coord) {
		switch (coord) {
		case WEST:
			return -1;
		case NORTH:
			return 0;
		case EAST:
			return 1;
		case SOUTH:
			return 0;

		}
		return 0;
	}
	
	
	/** overriden method in order to set backround according to the length of the queue*/
	@Override
	public void updateText(String newValue){
		super.updateText(newValue);
		
		if(QUEUE_TYPE == QueueType.INQUEUE){
			//make this component opaque so that the bgcolor is visible
			this.setOpaque(true);
			
			//check what color suits the actual length of this Queue
			switch(Integer.parseInt(newValue)){
			case 0 : super.setBackground(Color.WHITE);break;
			case 1 :
			case 2 : super.setBackground(new Color(175,255,47));break; //green color
			case 3 :
			case 4 : super.setBackground(new Color(255,215,0));break; //gold color
			case 5 :
			case 6 : super.setBackground(new Color(255,193,37));break; //orange color
			case 7 :
			case 8 : super.setBackground(new Color(255,69,0));break; //red color
			}
		}
		
	}
	
	
}
