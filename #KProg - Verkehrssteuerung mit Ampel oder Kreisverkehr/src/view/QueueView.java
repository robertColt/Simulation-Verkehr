package view;

import model.Station;
import model.Station.QueueType;

/**
 * Class for the view of queues
 * 
 * @author Jaeger, Schmidt, Verghelet
 * @version 2016-07-15
 */
@SuppressWarnings("serial")
public class QueueView extends TextView {
	
	/** INQUEUE: the x offset between the view and the stations x coordinate*/
	private static final int X_INQUEUEOFFSET = -20; 
	
	/** INQUEUE: the y offset between the view and the stations y coordinate*/
	private static final int Y_INQUEUEOFFSET = -40; 
	
	/** OUTQUEUE: the x offset between the view and the stations x coordinate*/
	private static final int X_OUTQUEUEOFFSET = 20; 
	
	/** OUTQUEUE: the y offset between the view and the stations y coordinate*/
	private static final int Y_OUTQUEUEOFFSET = 80; 
		
	/** the x offset between two views*/
	private static final int X_OFFSET = 40; 
	
	/** Creates a new view with text (JLabel)
	 * 
	 * @param text the text content of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 */
	private QueueView(String text, int xPos, int yPos){
		
		super(text, xPos, yPos);
		
	}
			
	/** Creates a new text view of the queue
	 * 
	 * @param type the type of queue we have
	 * @param text the text content of the view
	 * @param station the station the queue belongs to
	 * @param xfactor affects the x position of the view
	 *
	 * @return the QueueView
	 */
	public static QueueView create(QueueType type, String text, Station station, int xfactor){
		
		// the views x-, y-position
		int xPos = 0;
		int yPos = 0;
		
		// do we have an incoming queue or an outgoing queue
		switch (type){
		
			case INQUEUE:
				
				xPos = station.getXPos() + X_INQUEUEOFFSET + xfactor * X_OFFSET;
				yPos = station.getYPos() + Y_INQUEUEOFFSET;
				
				break;
				
			case OUTQUEUE:
				
				xPos = station.getXPos() + X_OUTQUEUEOFFSET + xfactor * X_OFFSET;
				yPos = station.getYPos() + Y_OUTQUEUEOFFSET;
				
				break;
		
		}
		
		return new QueueView(text, xPos, yPos);
		
	}

}
