package model;

import java.util.Collection;

import controller.Simulation;

/**
 * Class for the beginning station, this is where all objects start
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-07
 */
public class StartStation extends SimpleStation {
						
	/** instance of the start station */
	private static StartStation theStartStation;
	
	/** (private!) Constructor, creates a new start station
	 * 
	 * @param label of the station 
	 * @param xPos x position of the station 
	 * @param yPos y position of the station 
	 * @param image image of the station 
	 */
	private StartStation(String label, int xPos, int yPos, String image){
		
		super(label, xPos, yPos, image);
		
	}
	
	/** creates a new start station
	 *
	 * @param label of the station 
	 * @param xPos x position of the station 
	 * @param yPos y position of the station 
	 * @param image image of the station 
	 */
	public static void create(String label, int xPos, int yPos, String image){
	
		theStartStation = new StartStation(label, xPos, yPos, image);
		
	}
	
			
	@Override
	protected void handleObject(TheObject theObject){
				
		//the object chooses an outgoing queue and enter it
		theObject.enterOutQueue(this);
		
		//let the next objects start with a little delay one by one
				try {
					Thread.sleep(Simulation.CLOCKBEAT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		
	}
	
		
	/**Get the start station
	 * 
	 * @return theStartStation
	 */
	public static StartStation getStartStation() {
		return theStartStation;
	}

	@Override
	protected void handleObjects(Collection<TheObject> theObjects) {
				
	}
	
	@Override
	protected Collection<TheObject> getNextInQueueObjects() {
		return null;
	}

	@Override
	protected Collection<TheObject> getNextOutQueueObjects() {
		return null;
	}

	
	
}
