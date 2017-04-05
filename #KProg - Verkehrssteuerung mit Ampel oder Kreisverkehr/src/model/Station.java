package model;

import java.util.ArrayList;
import java.util.Collection;

import view.StationView;

/**
 * Superclass for all Stations
 * 
 * @author Jaeger, Schmidt
 * @version 2016-02-11
 */
public abstract class Station extends Actor {
	

	/** the types of queues we have INQUEUE or OUTQUEUE */
	public enum QueueType {
		INQUEUE, OUTQUEUE
	};


	/** list of all stations */
	protected static ArrayList<Station> allStations = new ArrayList<Station>();

	/**
	 * Constructor for all stations
	 * 
	 * @param label
	 *            of the station
	 * @param xPos
	 *            x position of the station
	 * @param yPos
	 *            y position of the station
	 * @param image
	 *            image of the station
	 */
	protected Station(String label, int xPos, int yPos, String image) {
		super(label, xPos, yPos);

		// create the view
		//if this station is 
		if(this instanceof StartStation || this instanceof EndStation){
			super.theView = StationView.create(image, xPos, yPos, false);
		}
		else{
			super.theView = StationView.create(image, xPos, yPos, true);
		}
		

		// add this station to the all stations list
		allStations.add(this);
	}

	@Override
	protected boolean work() {

		// let the thread wait only if there are no objects in the incoming and
		// outgoing queues
		if (numberOfInQueueObjects() == 0 && numberOfOutQueueObjects() == 0)
			return false;

		// If there is an inqueue object found, handle it
		if (numberOfInQueueObjects() > 0)
			this.handleObject(this.getNextInQueueObject());

		// If there is an object in the out queue -> wake it up
		if (numberOfOutQueueObjects() > 0) {

			TheObject myObject = (TheObject) this.getNextOutQueueObject();// get
																			// the
																			// object

			// instruct the object to move to the next station
			myObject.wakeUp();

		}

		// maybe there is more work to do
		return true;

	}

	/**
	 * Create the stations queues
	 *
	 */
	protected abstract void createQueues();

	/**
	 * Handle the given object, this is the place where the given object is
	 * treated by the station
	 *
	 * @param theObject
	 *            the object that should be treated
	 */
	protected abstract void handleObject(TheObject theObject);

	/**
	 * Handle the given objects, this is the place where the given objects are
	 * treated by the station
	 *
	 * @param theObjects
	 *            the collection of objects that should be treated
	 */
	protected abstract void handleObjects(Collection<TheObject> theObjects);

	/**
	 * Get all Stations
	 * 
	 * @return the allStations
	 */
	public static ArrayList<Station> getAllStations() {
		return allStations;
	}

	/**
	 * Get the number of all waiting objects in the incoming queues
	 *
	 * @return the number
	 */
	protected abstract int numberOfInQueueObjects();

	/**
	 * Get the number of all waiting objects in the outgoing queues
	 *
	 * @return the number
	 */
	protected abstract int numberOfOutQueueObjects();

	/**
	 * Get the next (suited) object out of one of the stations incoming queues
	 *
	 * @return the next object or null if there's nothing found
	 */
	protected abstract TheObject getNextInQueueObject();

	/**
	 * Get a number of (suited) objects out of the stations incoming queues
	 *
	 * @return a collection of objects or null if there's nothing found
	 */
	protected abstract Collection<TheObject> getNextInQueueObjects();

	/**
	 * Get the next (suited) object out of one of the stations outgoing queues
	 *
	 * @return the next object or null if there's nothing found
	 */
	protected abstract TheObject getNextOutQueueObject();

	/**
	 * Get a number of (suited) objects out of the stations outgoing queues
	 *
	 * @return a collection of objects or null if there's nothing found
	 */
	protected abstract Collection<TheObject> getNextOutQueueObjects();

	/**
	 * Get all incoming queues
	 * @return ArrayList return all out queues
	 */
	public abstract ArrayList<SynchronizedQueue> getAllInQueues();

	/**
	 * Get all outgoing queues
	 * @return ArrayList returns all out queues
	 */
	public abstract ArrayList<SynchronizedQueue> getAllOutQueues();

}
