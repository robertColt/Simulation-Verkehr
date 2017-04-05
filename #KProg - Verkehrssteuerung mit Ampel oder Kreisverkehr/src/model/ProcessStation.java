package model;

import java.util.ArrayList;
import java.util.Collection;

import controller.Simulation;
import io.Statistics;

/**
 * !!! CURRENTLY NOT USED
 * Class for a processing station
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-08
 */
public class ProcessStation extends Station {

	/** a list for all incoming queues */
	private ArrayList<SynchronizedQueue> inComingQueues = new ArrayList<SynchronizedQueue>();

	/** a list for all outgoing queues */
	private ArrayList<SynchronizedQueue> outGoingQueues = new ArrayList<SynchronizedQueue>();

	/** the number of incoming queues */
	private int numberOfInQueues;

	/** the number of outgoing queues, later this comes also from XML */
	private int numberOfOutQueues;

	/** a parameter that affects the speed of the treatment for an object */
	private double troughPut;

	/** the instance of our static inner Measurement class */
	Measurement measurement = new Measurement();

	/**
	 * (private!) Constructor, creates a new process station
	 * 
	 * @param label
	 *            of the station
	 * @param numberOfInQueues
	 *            the number of incoming queues
	 * @param numberOfOutQueues
	 *            the number of outgoing queues
	 * @param troughPut
	 *            a stations parameter that affects treatment of an object
	 * @param xPos
	 *            x position of the station
	 * @param yPos
	 *            y position of the station
	 * @param image
	 *            image of the station
	 */
	private ProcessStation(String label, int numberOfInQueues, int numberOfOutQueues, double troughPut, int xPos,
			int yPos, String image) {

		super(label, xPos, yPos, image);
		this.numberOfInQueues = numberOfInQueues;
		this.numberOfOutQueues = numberOfOutQueues;
		this.troughPut = troughPut;

		// create the queues
		createQueues();

	}

	/**
	 * create a new process station and add it to the station list
	 *
	 * @param label
	 *            of the station
	 * @param numberOfInQueues
	 *            the number of incoming queues
	 * @param numberOfOutQueues
	 *            the number of outgoing queues
	 * @param troughPut
	 *            a stations parameter that affects treatment of an object
	 * @param xPos
	 *            x position of the station
	 * @param yPos
	 *            y position of the station
	 * @param image
	 *            image of the station
	 */
	public static void create(String label, int numberOfInQueues, int numberOfOutQueues, double troughPut, int xPos,
			int yPos, String image) {

		new ProcessStation(label, numberOfInQueues, numberOfOutQueues, troughPut, xPos, yPos, image);

	}

	@Override
	protected void createQueues() {

		// create the queues for the incoming objects
		if (numberOfInQueues == Coordinates.values().length) {

			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.WEST, QueueType.INQUEUE, this));

			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.NORTH, QueueType.INQUEUE, this));

			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.EAST, QueueType.INQUEUE, this));
			
			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.SOUTH, QueueType.INQUEUE, this));
		} else {
			Statistics.showErr("InQueues number can't be changed");
		}

		// create the queue(s) for outgoing objects

		if (numberOfOutQueues == Coordinates.values().length) {

			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.WEST, QueueType.OUTQUEUE, this));

			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.NORTH, QueueType.OUTQUEUE, this));

			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.EAST, QueueType.OUTQUEUE, this));
			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.SOUTH, QueueType.OUTQUEUE, this));
		} else {
			Statistics.showErr("OutQueues number can't be changed");
		}
		

	}

	@Override
	protected int numberOfInQueueObjects() {

		int theNumber = 0;

		// We have more than one incoming queue -> get all incoming queues
		for (SynchronizedQueue inQueue : this.inComingQueues) {

			theNumber = theNumber + inQueue.size();
		}

		return theNumber;

	}

	@Override
	protected int numberOfOutQueueObjects() {

		int theNumber = 0;

		// maybe we have more than one outgoing queue -> get all outgoing queues
		for (SynchronizedQueue outQueue : this.outGoingQueues) {

			theNumber = theNumber + outQueue.size();
		}

		return theNumber;

	}

	@Override
	protected TheObject getNextInQueueObject() {

		// maybe we have more than one incoming queue -> get all incoming queues
		for (SynchronizedQueue inQueue : this.inComingQueues) {

			// We have to made a decision which queue we choose -> your turn
			// I'll take the first possible I get
			if (inQueue.size() > 0) {
				return (TheObject) inQueue.poll();
			}
		}

		// nothing is found
		return null;
	}

	@Override
	protected TheObject getNextOutQueueObject() {

		// maybe we have more than one outgoing queue -> get all outgoing queues
		for (SynchronizedQueue outQueue : this.outGoingQueues) {

			// We have to made a decision which queue we choose -> your turn
			// I'll take the first possible I get
			if (outQueue.size() > 0) {
				return (TheObject) outQueue.poll();
			}
		}

		// nothing is found
		return null;

	}

	@Override
	protected void handleObject(TheObject theObject) {

		// count all the visiting objects
		measurement.numbOfVisitedObjects++;

		Statistics.show(this.getLabel() + " behandelt: " + theObject.getLabel());

		// the processing time of the object
		int processTime = theObject.getProcessTime();

		// the time to handle the object
		int theObjectsTreatingTime = (int) (processTime / this.troughPut);

		// get the starting time of the treatment
		long startTime = Simulation.getGlobalTime();

		// the elapsed time of the treatment
		int elapsedTime = 0;

		// while treating time is not reached
		while (!(theObjectsTreatingTime <= elapsedTime)) {

			// the elapsed time since the start of the treatment
			elapsedTime = (int) (Simulation.getGlobalTime() - startTime);

			// let the thread sleep for the adjusted clock beat
			// This is just needed to notice the different treatment duration in
			// the view
			try {
				Thread.sleep(Simulation.CLOCKBEAT);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		// increase the time the object was treated
		//TODO theObject.measurement.myTreatmentTime = theObject.measurement.myTreatmentTime + elapsedTime;

		// increase the stations in use time
		measurement.inUseTime = measurement.inUseTime + elapsedTime;

		// the treatment is over, now the object chooses an outgoing queue and
		// enter it
		theObject.enterOutQueue(this);

		// just to see the view of the outgoing queue works
		try {
			Thread.sleep(500);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * A (static) inner class for measurement jobs. The class records specific
	 * values of the station during the simulation. These values can be used for
	 * statistic evaluation.
	 */
	private static class Measurement {

		/** the total time the station is in use */
		private int inUseTime = 0;

		/** the number of all objects that visited this station */
		private int numbOfVisitedObjects = 0;

		/**
		 * Get the average time for treatment
		 * 
		 * @return the average time for treatment
		 */
		private int avgTreatmentTime() {

			if (numbOfVisitedObjects == 0)
				return 0; // in case that a station wasn't visited
			else
				return inUseTime / numbOfVisitedObjects;

		}

	}

	/**
	 * get and print some statistics out of the Measurement class
	 * 
	 */
	public void printStatistics() {

		String theString = "\nStation Typ: " + this.label;
		theString = theString + "\nAnzahl der behandelten Objekte: " + measurement.numbOfVisitedObjects;
		theString = theString + "\nZeit zum Behandeln aller Objekte: " + measurement.inUseTime;
		theString = theString + "\nDurchnittliche Behandlungsdauer: " + measurement.avgTreatmentTime();

		Statistics.show(theString);

	}

	/**
	 * Get all process stations
	 * 
	 * @return the allProcessStations
	 */
	public static ArrayList<ProcessStation> getAllProcessStations() {

		// all the process station objects
		ArrayList<ProcessStation> allProcessStations = new ArrayList<ProcessStation>();

		// filter the process stations out of the station list
		for (Station station : Station.getAllStations()) {

			if (station instanceof ProcessStation)
				allProcessStations.add((ProcessStation) station);

		}

		return allProcessStations;
	}

	@Override
	public ArrayList<SynchronizedQueue> getAllInQueues() {
		return inComingQueues;
	}

	@Override
	public ArrayList<SynchronizedQueue> getAllOutQueues() {
		return outGoingQueues;
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
