package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import controller.Simulation;
import io.JSONWriter;
import io.Statistics;
import view.ObjectView;

/**
 * Class for the objects
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-08
 */

public class TheObject extends Actor {
	
	/** the process time of the object */
	private int processTime;

	/** the speed of the object, the higher the lower */
	private int mySpeed;
	
	/**
	 * all the station (with in and out index) where the object still have to go
	 * to
	 */
	private Queue<MappedStation> stationsToGo = new LinkedList<MappedStation>();
	
	/** a copy of the stationsToGo queue where MappedStations are not polled */
	private Queue<MappedStation> stationsToGoCopy = new LinkedList<MappedStation>();

	/** list of all objects */
	private static ArrayList<TheObject> allObjects = new ArrayList<TheObject>();

	/** the instance of our static inner Measurement class */
	Measurement measurement = new Measurement();

	/** the instance of our timee inner class */
	Timer timer = new Timer();

	/**
	 * (private!) Constructor, creates a new object model and send it to the
	 * start station
	 * 
	 * @param label
	 *            of the object
	 * @param stationsToGo
	 *            the stations to go
	 * @param processtime
	 *            the processing time of the object, affects treatment by a
	 *            station
	 * @param speed
	 *            the moving speed of the object
	 * @param xPos
	 *            x position of the object
	 * @param yPos
	 *            y position of the object
	 * @param image
	 *            image of the object
	 */
	private TheObject(String label, Queue<MappedStation> stationsToGo, int processtime, int speed, int xPos, int yPos,
			String image) {
		super(label, xPos, yPos);

		// create the view
		super.theView = ObjectView.create(image, xPos, yPos);

		TheObject.allObjects.add(this); // add object to the static list

		this.stationsToGo = stationsToGo;
		this.processTime = processtime;
		this.mySpeed = speed;

		// the first station to go to is the start station
		Station station = this.getNextStation();

		// enter the in queue of the start station
		this.enterInQueue(station);

	}

	/**
	 * Method that compares a Station with all MappedStation of the current
	 * object
	 * 
	 * @param station
	 * @return int entrance index
	 */
	private int getInQueue(Station station) {
		//
		for (MappedStation st : this.stationsToGoCopy) {
			if (st.getStation().getLabel().equals(station.getLabel()))
				return st.getEntranceNr();
		}
		return 0;
	}

	/**
	 * Method that compares a Station with all MappedStation of the current
	 * object
	 * 
	 * @param station
	 * @return int exit index
	 */
	private int getOutQueue(Station station) {
		for (MappedStation st : this.stationsToGoCopy) {
			if (st.getStationLabel().equals(station.getLabel()))
				return st.getExitNr();
		}
		return 0;
	}

	/**
	 * Create a new object model
	 *
	 * @param label
	 *            of the object
	 * @param stationsToGo
	 *            the stations to go
	 * @param processtime
	 *            the processing time of the object, affects treatment by a
	 *            station
	 * @param speed
	 *            the moving speed of the object
	 * @param xPos
	 *            x position of the object
	 * @param yPos
	 *            y position of the object
	 * @param image
	 *            image of the object
	 */
	public static void create(String label, Queue<MappedStation> stationsToGo, int processtime, int speed, int xPos,
			int yPos, String image) {

		new TheObject(label, stationsToGo, processtime, speed, xPos, yPos, image);

	}

	/**
	 * Chose the next station to go to
	 * 
	 * @return the next station or null if no station was found
	 */
	private Station getNextStation() {

		// get the next station from the queue (list)

		if (this.stationsToGo.isEmpty())
			return null; // the "label" stations list is empty
		this.stationsToGoCopy.add(this.stationsToGo.peek());
		Station nextStation = this.stationsToGo.poll().getStation();

		// looking for the matching station and return it

		return nextStation; // no station was found
	}

	/**
	 * Chooses a suited incoming queue of the given station and enter it
	 * 
	 * @param station
	 *            the station from where the queue should be chosen
	 * 
	 */
	private void enterInQueue(Station station) {
		// get the stations incoming queues
		ArrayList<SynchronizedQueue> inQueues = station.getAllInQueues();
		if (inQueues.size() == 1) {
			inQueues.get(0).offer(this);
		} else {
			inQueues.get(this.getInQueue(station)).offer(this);
		}

		// object has entered queue -> start timer for this object
		this.timer.startTimer();

		if (station instanceof RoundaboutStation) {
			((RoundaboutStation) station).updateMaxObjects(station);
		} else if (station instanceof TrafficLightStation) {
			((TrafficLightStation) station).updateMaxObjects(station);
		}

		if (!(station instanceof StartStation || station instanceof EndStation)) {
			measurement.nrVisitedStations++;
		}
	}

	/**
	 * Chooses a suited outgoing queue of the given station and enter it
	 * 
	 * @param station
	 *            the station from where the queue should be chosen
	 */

	void enterOutQueue(Station station) {
		// get the stations incoming queues
		ArrayList<SynchronizedQueue> outQueues = station.getAllOutQueues();
		if (outQueues.size() == 1) {
			outQueues.get(0).offer(this);
		} else {
			outQueues.get(this.getOutQueue(station)).offer(this);
		}

	}

	@Override
	protected boolean work() {

		// choose the next station to go to
		Station nextStation = this.getNextStation();

		// only move if there is a next station found
		if (nextStation == null)
			return false;

		// let the object move to the chosen station

		Statistics.show(this.getLabel() + " geht zur " + nextStation.getLabel());

		// while target is not achieved
		while (!(nextStation.getXPos() == this.xPos && nextStation.getYPos() == this.yPos)) {

			// move to the station
			if (nextStation.getXPos() > this.xPos)
				this.xPos++;
			if (nextStation.getYPos() > this.yPos)
				this.yPos++;

			if (nextStation.getXPos() < this.xPos)
				this.xPos--;
			if (nextStation.getYPos() < this.yPos)
				this.yPos--;

			// set our view to the new position
			theView.setLocation(this.xPos, this.yPos);

			// let the thread sleep for the sequence time
			try {
				Thread.sleep(Simulation.SPEEDFACTOR * mySpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		Statistics.show(this.getLabel() + " erreicht " + nextStation.getLabel());

		// the object has reached the station, now the object chooses an
		// incoming queue and enter it
		this.enterInQueue(nextStation);

		// wake up the station
		nextStation.wakeUp();

		// work is done
		return false;

	}

	/**
	 * Class for recording the time a car waits in a station
	 * 
	 */
	public class Timer {

		/** the global time this object enters the in Queue */
		private long entranceTime;

		/**
		 * starts the timer for this object initializes variable entranceTime
		 * with current global time
		 */
		private void startTimer() {
			entranceTime = Simulation.getGlobalTime();
		}

		/**
		 * computes how long this object waited from entrance in the station to exit
		 * @return waitingTime the waiting time
		 */
		public long getWaitingTime() {
			long waitingTime = Simulation.getGlobalTime() - entranceTime;
			measurement.computeAvgWaitingTime(waitingTime);
			// get current global time and subtract the entranceTime from it
			return waitingTime;
		}

	}

	/**
	 * A (static) inner class for measurement jobs. The class records specific
	 * values of the object during the simulation. These values can be used for
	 * statistic evaluation.
	 */
	static class Measurement {

		/** the waiting time by all processing stations, in seconds */
		double myWaitingTime = 0;

		/** number of visited stations */
		int nrVisitedStations = 0;

		/**
		 * the average waiting time on all stations from start- to end station
		 */
		double avgWaitingTime = 0;

		/**
		 * recalculates the avgWaitingTime
		 * 
		 */
		public void computeAvgWaitingTime(long waitingTime) {
			// make the average between actual value of avgWaitingTime and param
			// waitingTime
			myWaitingTime += waitingTime;
			avgWaitingTime = myWaitingTime / nrVisitedStations;
		}

	}

	/**
	 * Print some statistics
	 * 
	 */
	public void printStatistics() {

		String theString = "\nObjekt: " + this.label;
		theString = theString + "\nGesamte Wartezeit des Objekts: " + measurement.myWaitingTime
				+ "\nDurchschnittliche Wartezeit an der Stationen auf der gesamten Strecke : "
				+ measurement.avgWaitingTime;

		Statistics.show(theString);
		Statistics.writeToFile(theString);

	}

	/**
	 * Adds statistics to JSON file
	 */
	public void addStatisticsToFile() {
		Map<String, Double> map = new HashMap<>();
		map.put("waitingTime", measurement.myWaitingTime);
		map.put("avgWatingTime", measurement.avgWaitingTime);
		JSONWriter.writeToFile(map, this.label);
	}

	/**
	 * Get all objects
	 * 
	 * @return a list of all objects
	 */
	public static ArrayList<TheObject> getAllObjects() {
		return allObjects;
	}

	/**
	 * Get the objects processing time
	 * 
	 * @return the processing time
	 */
	public int getProcessTime() {
		return processTime;
	}

	/**
	 * Get the list of all stations still to go to
	 * 
	 * @return the stationsToGo
	 */
	public Queue<MappedStation> getStationsToGo() {
		return stationsToGo;
	}

}
