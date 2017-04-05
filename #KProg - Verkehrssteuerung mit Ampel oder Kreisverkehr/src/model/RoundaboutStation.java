package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import controller.Simulation;
import io.JSONWriter;
import io.Statistics;
import view.StationView;

/**
 * Class for a roundabout station
 * 
 * @author Jaeger, Schmidt, Colt, Verghelet
 * @version 2016-07-08
 */
public class RoundaboutStation extends Station {

	/** a list for all incoming queues */
	private ArrayList<SynchronizedQueue> inComingQueues = new ArrayList<SynchronizedQueue>();

	/** a list for all outgoing queues */
	private ArrayList<SynchronizedQueue> outGoingQueues = new ArrayList<SynchronizedQueue>();

	/**
	 * the number of incoming queues, also represent maximal number of cars
	 * inside the roundabout
	 */
	private int numberOfInQueues;

	/** the number of outgoing queues, later this comes also from XML */
	private int numberOfOutQueues;

	/** a parameter that affects the speed of the treatment for an object */
	private double troughPut;

	/** the instance of our static inner Measurement class */
	Measurement measurement = new Measurement();

	/**
	 * (private!) Constructor, creates a new RoundaboutStation
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
	private RoundaboutStation(String label, int numberOfInQueues, int numberOfOutQueues, double troughPut, int xPos,
			int yPos, String image) {

		super(label, xPos, yPos, image);
		this.numberOfInQueues = numberOfInQueues;
		this.numberOfOutQueues = numberOfOutQueues;
		this.troughPut = troughPut;

		// add observer view(stationView) to measurement class(Observable)
		measurement.addObserver((StationView) super.theView);

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

		new RoundaboutStation(label, numberOfInQueues, numberOfOutQueues, troughPut, xPos, yPos, image);

	}

	/**
	 * The inqueues and outqueues for the station are created here(4 each)
	 */
	@Override
	protected void createQueues() {

		// create the queues for the incoming objects
		if (numberOfInQueues == Coordinates.values().length) {
			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.WEST, QueueType.INQUEUE, this));
			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.SOUTH, QueueType.INQUEUE, this));
			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.EAST, QueueType.INQUEUE, this));
			inComingQueues.add(SynchronizedQueue.createQueue(Coordinates.NORTH, QueueType.INQUEUE, this));
		} else {
			Statistics.showErr("Number of inqueues must be equal to number of cardinal points");
		}
		// create the queue(s) for outgoing objects
		if (numberOfOutQueues == Coordinates.values().length) {
			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.WEST, QueueType.OUTQUEUE, this));
			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.SOUTH, QueueType.OUTQUEUE, this));
			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.EAST, QueueType.OUTQUEUE, this));
			outGoingQueues.add(SynchronizedQueue.createQueue(Coordinates.NORTH, QueueType.OUTQUEUE, this));
		} else {
			Statistics.showErr("Number of outqueues must be equal to number of cardinal points");
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
	protected boolean work() {

		// let the thread wait only if there are no objects in the incoming and
		// outgoing queues
		if (numberOfInQueueObjects() == 0 && numberOfOutQueueObjects() == 0)
			return false;

		// handle all inQueue objects
		if (numberOfInQueueObjects() > 0) {
			this.handleObjects(this.getNextInQueueObjects());
		}

		// If there are objects in the out queue -> wake them up
		if (numberOfOutQueueObjects() > 0) {
			this.wakeUpObjects(this.getNextOutQueueObjects());
		}

		// maybe there is more work to do
		return true;
	}

	/**
	 * wake up all objects
	 * 
	 */
	private void wakeUpObjects(Collection<TheObject> theObjects) {
		// wake up all objects in the outQueues by starting new Thread for each
		// wakeup
		for (TheObject theObject : theObjects) {
			new Thread() {
				@Override
				public void run() {
					theObject.wakeUp();
				}
			}.start(); // start new anonymous thread

		}
	}

	@Override
	protected void handleObjects(Collection<TheObject> theObjects) {

		if (theObjects.isEmpty())
			return;

		// List of threads that are going to be started
		List<Thread> startedThreads = new ArrayList<>();

		// handle each object individually by starting a thread
		for (TheObject object : theObjects) {
			Thread thread = new ObjectHandler(object);
			startedThreads.add(thread);
			thread.start();
		}

		// wait for all started threads to terminate before going back to work
		// method
		for (Thread thread : startedThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Thread subclass that handles a single object in a different Thread from
	 * this stations Thread
	 *
	 */
	private class ObjectHandler extends Thread {

		/** the object to be handled */
		private TheObject theObject;

		/**
		 * constructor for this class
		 * 
		 * @param theObject
		 *            object to be handled
		 */
		public ObjectHandler(TheObject theObject) {
			this.theObject = theObject;
		}

		@Override
		public void run() {

			// count all the visiting objects
			measurement.numbOfVisitedObjects++;

			Statistics.show(theObject.getLabel() + " geht durch " + RoundaboutStation.this.getLabel());

			// the processing time of the object
			// int processTime = theObject.getProcessTime();

			// the time to handle the object is same for each object
			int theObjectsTreatingTime = (int) troughPut;

			// get the starting time of the treatment
			long startTime = Simulation.getGlobalTime();

			// the elapsed time of the treatment
			int elapsedTime = 0;

			// while treating time is not reached
			while (!(theObjectsTreatingTime <= elapsedTime)) {

				// the elapsed time since the start of the treatment
				elapsedTime = (int) (Simulation.getGlobalTime() - startTime);

				// let the thread sleep for the adjusted clock beat
				// This is just needed to notice the different treatment
				// duration in the view
				try {
					Thread.sleep(Simulation.CLOCKBEAT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			// increase the stations in use time
			measurement.inUseTime = measurement.inUseTime + elapsedTime;

			// the waiting time of this object
			long waitingTime = theObject.timer.getWaitingTime();

			// increase the time the object has waited
			// theObject.measurement.myWaitingTime =
			// theObject.measurement.myWaitingTime + waitingTime;

			// recalculate the average waiting time/object :
			// measurement.computeAvgWaitingTime according to this objects
			// waiting time
			// update statistics label of this Station
			// ((StationView)theView).updateStatistics(((Double)measurement.computeAvgWaitingTime(waitingTime)).toString());
			measurement.computeAvgWaitingTime(waitingTime);

			// the treatment is over, now the object chooses an outgoing queue
			// and enter it
			theObject.enterOutQueue(RoundaboutStation.this);

			// just to see the view of the outgoing queue works
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Method that returns all TheObject objects that are currently waiting(are
	 * first in the inComingQueues)
	 * 
	 * @return an ArrayList of Objects that are first in the inComingQueues
	 */
	@Override
	protected List<TheObject> getNextInQueueObjects() {

		// list of all objects that are first in all outQueue
		List<TheObject> inComingObjects = new ArrayList<TheObject>();

		// filling list inObjects with TheObjects
		for (SynchronizedQueue inQueue : inComingQueues) {
			if (inQueue.isEmpty()) {
				continue;
			} else {
				inComingObjects.add((TheObject) inQueue.poll());
			}
		}

		// if there are no objects in the outQueue return null
		if (inComingObjects.isEmpty()) {
			return null;
		} else {
			return inComingObjects;
		}

	}

	/**
	 * Method that returns all TheObject objects that are currently waiting(are
	 * first in the outGoingQueues)
	 * 
	 * @return an ArrayList of Objects that are first in the outQueues
	 */
	@Override
	protected List<TheObject> getNextOutQueueObjects() {

		// list of all objects that are first in the outqueues
		List<TheObject> outGoingObjects = new ArrayList<TheObject>();

		// filling List outObjects with TheObjects
		for (SynchronizedQueue outQueue : outGoingQueues) {
			while (!outQueue.isEmpty()) {
				// put all objects in the outGoingObjects list
				outGoingObjects.add((TheObject) outQueue.poll());
			}
		}

		// if there are no objects in the outQueues return null
		if (outGoingObjects.isEmpty()) {
			return null;
		} else {
			return outGoingObjects;
		}
	}

	/**
	 * A (static) inner class for measurement jobs. The class records specific
	 * values of the station during the simulation. These values can be used for
	 * statistic evaluation.
	 */
	private static class Measurement extends Observable {

		/** the total time the station is in use */
		private volatile int inUseTime = 0;

		/** the number of all objects that visited this station */
		private volatile int numbOfVisitedObjects = 0;

		/** the total time all objects waited on this station */
		private double totalWaitingTime = 0.0;

		/** average waiting time/object */
		private double avgWaitingTime = 0.0;

		/**
		 * the maximal number of objects that waited in all inQueues longest
		 * Queue formed
		 */
		private int maxNrWaitingObjects = 0;

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

		/**
		 * recalculates the totalWaitingTime/object by making the average
		 * between the instance totalWaitingTime and the new value given in the
		 * parameter list
		 * 
		 * @param objectWaitingTime
		 *            the waiting time for the object
		 */
		private synchronized double computeAvgWaitingTime(long objectWaitingTime) {
			// take current avg waiting time and make new average of it and the
			// value in the parameters

			// add this objects waiting time to he total
			totalWaitingTime = totalWaitingTime + objectWaitingTime;

			avgWaitingTime = totalWaitingTime / numbOfVisitedObjects;
			// notify all observers that the avg waiting time has changed
			super.setChanged();
			super.notifyObservers(this.avgWaitingTime);

			return avgWaitingTime;
		}

		/**
		 * Get the average time an object waits
		 * 
		 * @return the average time for waiting
		 */
		private double avgWaitingTime() {
			return avgWaitingTime;
		}

	}

	/** 
	 * update maximal nr of objects waiting at the inqueues of the given station
	 * @param station the station on which the update will be done
	 * */
	public synchronized void updateMaxObjects(Station station) {
		for (SynchronizedQueue inQueue : station.getAllInQueues()) {
			int nrObjects = inQueue.size();

			if (nrObjects > measurement.maxNrWaitingObjects) {
				measurement.maxNrWaitingObjects = nrObjects;
			}
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
		theString = theString + "\nDurchnittliche Wartezeit/Auto: " + measurement.avgWaitingTime();
		theString = theString + "\nLängste Warteschlange an dieser Station : " + measurement.maxNrWaitingObjects;

		Statistics.show(theString);

		Statistics.writeToFile(theString);
	}

	/**
	 * Packs some values(obtained with Measurement class) in a Map to be written
	 * in a json file,
	 */
	public void addStatisticsToFile() {
		Map<String, Double> map = new HashMap<>();
		map.put("numberOfVisitedObjects", (double) measurement.numbOfVisitedObjects);
		map.put("inUseTime", (double) measurement.inUseTime);
		map.put("avgTreatmentTime", (double) measurement.avgTreatmentTime());
		map.put("avgWaitingTime", measurement.avgWaitingTime());
		map.put("maxNrWaitingObjecst", (double) measurement.maxNrWaitingObjects);
		JSONWriter.writeToFile(map, this.label);
	}

	/**
	 * Get all process stations
	 * 
	 * @return the allProcessStations
	 */
	public static ArrayList<RoundaboutStation> getAllProcessStations() {

		// all the process station objects
		ArrayList<RoundaboutStation> allProcessStations = new ArrayList<RoundaboutStation>();

		// filter the process stations out of the station list
		for (Station station : Station.getAllStations()) {

			if (station instanceof RoundaboutStation)
				allProcessStations.add((RoundaboutStation) station);

		}

		return allProcessStations;
	}

	/**
	 * Get all incoming queues
	 * 
	 * @return ArrayList return all out queues
	 */
	@Override
	public ArrayList<SynchronizedQueue> getAllInQueues() {
		return inComingQueues;
	}

	/**
	 * Get all outgoing queues
	 * 
	 * @return ArrayList returns all out queues
	 */
	@Override
	public ArrayList<SynchronizedQueue> getAllOutQueues() {
		return outGoingQueues;
	}

	/**
	 * Handle the given object, this is the place where the given object is
	 * treated by the station
	 *
	 * @param theObject
	 *            the object that should be treated
	 */
	@Override
	protected void handleObject(TheObject theObject) {

	}

}
