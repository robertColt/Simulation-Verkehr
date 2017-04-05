package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import controller.Simulation;
import io.JSONWriter;
import io.Statistics;
import view.SemaphoreView;
import view.StationView;

/**
 * Class for a processing station
 * 
 * @author Jaeger, Schmidt, Ilie, Dates, Verghelet, Colt
 * @version 2016-07-08
 */
public class TrafficLightStation extends Station {

	/**
	 * time to wait before switching the green light between perpendicular
	 * directions
	 */
	private final long SWITCH_TIME = 3000;

	/**
	 * true if the lights from West to East are green
	 * 
	 * @default true
	 */
	private volatile boolean isGreenHorizontal = true;

	/**
	 * true if the lights from North to South are green
	 * 
	 * @default false
	 */
	private volatile boolean isGreenVertical = false;

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
	 * the view that shows the "green" directions aka. graphical representation
	 * of isGreenVertica and isGreenHorizontal
	 */
	private SemaphoreView view = null;

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
	private TrafficLightStation(String label, int numberOfInQueues, int numberOfOutQueues, double troughPut, int xPos,
			int yPos, String image) {

		super(label, xPos, yPos, image);
		this.numberOfInQueues = numberOfInQueues;
		this.numberOfOutQueues = numberOfOutQueues;
		this.troughPut = troughPut;

		// create the queues
		createQueues();
		view = SemaphoreView.createSemaphore(this, isGreenHorizontal);

		// add observerview to measurement class(Observable)
		measurement.addObserver((StationView) super.theView);

		// start new Thread that switches the lights
		new Thread(new LightSwitcher(view)).start();

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

		new TrafficLightStation(label, numberOfInQueues, numberOfOutQueues, troughPut, xPos, yPos, image);
	}

	/**
	 * Inner class that switches colors of perpendicularly placed
	 * TrafficLights, also extends Observable and implements Runnable 
	 * 
	 * @see isGreenVertical
	 * @see isGreenHorizontal
	 */

	private class LightSwitcher extends Observable implements Runnable {

		// constructor for this class for adding the observer
		public LightSwitcher(Observer view) {
			TrafficLightStation.this.view = (SemaphoreView) view;
			super.addObserver(view);
		}

		@Override
		public void run() {
			/** switch color of traffic lights after the SWITCH_TIME elapsed */
			while (true) {
				try {
					Thread.sleep(SWITCH_TIME * Simulation.SPEEDFACTOR);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// change color of lights -> initialize variables with their
				// negative value(isGreen(=true) becomes false)
				isGreenVertical = !isGreenVertical;
				isGreenHorizontal = !isGreenHorizontal;

				super.setChanged();
				super.notifyObservers(isGreenHorizontal);
				// view.setIcon(isGreenHorizontal);

				/*
				 * System.out.println("WE : " +isGreenHorizontal);
				 * System.out.println("NS : " +isGreenVertical + "\n");
				 */
			}
		}
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
	protected void handleObject(TheObject theObject) {

	}

	/**
	 * Thread Subclass that handles one object in a different Thread gets
	 * Started in the @see handleObjects() method
	 **/
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

			Statistics.show(theObject.getLabel() + " geht durch " + TrafficLightStation.this.getLabel());

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
			// ((StationView)theView).updateStatistics(((Double)measurement.).toString());
			measurement.computeAvgWaitingTime(waitingTime);

			// the treatment is over, now the object chooses an outgoing queue
			// and enter it
			theObject.enterOutQueue(TrafficLightStation.this);

			// just to see the view of the outgoing queue works
			try {
				Thread.sleep(500);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected List<TheObject> getNextInQueueObjects() {

		// list of first objects waiting on the green lights
		List<TheObject> inComingObjects = new ArrayList<>();

		// check on which direction the light is green : horizontally or
		// vetically?
		// see instance variables of this station isGreenHorizontal and
		// isGreenVertical
		if (isGreenHorizontal) {
			// take first object from the left(inqueue with index 0)
			if (!(inComingQueues.get(0).isEmpty()))
				inComingObjects.add((TheObject) inComingQueues.get(0).poll());

			// take first object from the right(inqueue with index 2)
			if (!(inComingQueues.get(2).isEmpty()))
				inComingObjects.add((TheObject) inComingQueues.get(2).poll());

		} else if (isGreenVertical) {
			// take first object from down(inqueue with index 1)
			if (!(inComingQueues.get(1).isEmpty()))
				inComingObjects.add((TheObject) inComingQueues.get(1).poll());

			// take first object from up(inqueue with index 3)
			if (!(inComingQueues.get(3).isEmpty()))
				inComingObjects.add((TheObject) inComingQueues.get(3).poll());
		}

		return inComingObjects;
	}

	@Override
	protected Collection<TheObject> getNextOutQueueObjects() {

		// list of all objects that are in all outqueues
		ArrayList<TheObject> outGoingObjects = new ArrayList<TheObject>();

		// filling List outObjects with TheObjects
		for (SynchronizedQueue outQueue : outGoingQueues) {
			// check if currently iterated outQueue is not empty and get all
			// objects out of it
			while (!outQueue.isEmpty()) {
				// put all objects in the outGoingObjects list
				outGoingObjects.add((TheObject) outQueue.poll());
			}
		}

		// if there are no objects in the outQueue return null
		if (outGoingObjects.isEmpty()) {
			return null;
		} else {
			return outGoingObjects;
		}
	}

	@Override
	protected void handleObjects(Collection<TheObject> theObjects) {

		// if received collection is empty
		if (theObjects.isEmpty())
			return;

		// List of threads that are going to be started
		List<Thread> startedThreads = new ArrayList<>();

		// handle each object individually and simultaneosly (start new Thread
		// for each one)
		for (TheObject object : theObjects) {
			Thread thread = new ObjectHandler(object);
			startedThreads.add(thread); // put thread in the list
			thread.start();
		}

		// wait for all above started threads to terminate
		for (Thread thread : startedThreads) {
			try {
				thread.join(); // wait for Thread to finish
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected boolean work() {

		// let the thread wait only if there are no objects in the incoming and
		// outgoing queues
		if (numberOfInQueueObjects() == 0 && numberOfOutQueueObjects() == 0)
			return false;

		// If there are inqueue objects found, handle them
		if (numberOfInQueueObjects() > 0)
			this.handleObjects(this.getNextInQueueObjects());

		// If there are objects in the out queue -> wake them up
		if (numberOfOutQueueObjects() > 0) {
			// wake up objects in the outQueues
			this.wakeUpObjects(this.getNextOutQueueObjects());

		}
		return true;
	}

	/**
	 * wakes up the objects given by the Collection in the parameters
	 * 
	 * @param outGoingObjects
	 *            objects to be waken up
	 */
	private void wakeUpObjects(Collection<TheObject> outGoingObjects) {
		// wake up every object in the outQueues by starting new Thread for each
		// one
		for (TheObject object : outGoingObjects) {
			new Thread() {
				@Override
				public void run() {
					object.wakeUp();
				}
			}.start();

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
	 * update maximal nr of objects waiting at the inqueues at the given station
	 * @param station the station on which the update has to be made
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
		theString = theString + "\nDurchnittliche Behandlungsdauer/auto: " + measurement.avgTreatmentTime();
		theString = theString + "\nDurchnittliche Wartezeit/Auto : " + measurement.avgWaitingTime();
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
	 * @return allProcessStations
	 */
	public static ArrayList<TrafficLightStation> getAllProcessStations() {

		// all the process station objects
		ArrayList<TrafficLightStation> allProcessStations = new ArrayList<TrafficLightStation>();

		// filter the process stations out of the station list
		for (Station station : Station.getAllStations()) {

			if (station instanceof TrafficLightStation)
				allProcessStations.add((TrafficLightStation) station);

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
	 * Get all incoming queues
	 * 
	 * @return ArrayList return all out queues
	 */
	@Override
	public ArrayList<SynchronizedQueue> getAllOutQueues() {
		return outGoingQueues;
	}

}
