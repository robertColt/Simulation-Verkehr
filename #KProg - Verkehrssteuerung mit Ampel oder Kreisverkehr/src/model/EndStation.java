package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import controller.Main;
import controller.Simulation;
import io.JSONWriter;
import io.Statistics;

/**
 * Class for the end station. This is the last station where all objects are
 * collected
 * 
 * @author Jaeger, Schmidt, Verghelet, Colt
 * @version 2016-07-08
 */
public class EndStation extends SimpleStation {

	/**
	 * (private!) Constructor, creates a new end station
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
	private EndStation(String label, int xPos, int yPos, String image) {
		super(label, xPos, yPos, image);

	}

	/**
	 * creates a new end station
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
	public static void create(String label, int xPos, int yPos, String image) {

		new EndStation(label, xPos, yPos, image);

	}

	@Override
	protected boolean work() {

		// let the thread wait only if there are no objects in the incoming
		// queue
		if (numberOfInQueueObjects() == 0)
			return false;

		// If there is an inqueue object found, handle it
		if (numberOfInQueueObjects() > 0)
			this.handleObject(this.getNextInQueueObject());

		// maybe there is more work to do
		return true;

	}

	@Override
	protected void handleObject(TheObject theObject) {

		// the object chooses the outgoing queue and enter it
		theObject.enterOutQueue(this);

		// End the simulation if the condition is met
		endSimulation();

	}

	/**
	 * Ends the simulation and writes to file
	 */
	private void endSimulation() {

		// Are all objects in the stations outgoing queue, then we are finish
		if (TheObject.getAllObjects().size() == numberOfOutQueueObjects()) {

			Statistics.show("\n--- Simulation beendet ----");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
			LocalDateTime dateTimeNow = LocalDateTime.now();

			Statistics.writeToFile("\n-----------------------------------------------\n" + dateTimeNow.format(formatter)
					+ "\n" + Simulation.getScenario() + "\n-----------------------------------------------");
			// show some station statistics
			for (RoundaboutStation station : RoundaboutStation.getAllProcessStations()) {
				station.printStatistics();
				station.addStatisticsToFile();
			}
			for (TrafficLightStation station : TrafficLightStation.getAllProcessStations()) {
				station.printStatistics();
				station.addStatisticsToFile();
			}

			// total of all objects total waiting time
			long totalWaitingTime = 0;

			// show some objects statistics
			for (Object object : this.outGoingQueue) {
				((TheObject) object).printStatistics();
				((TheObject) object).addStatisticsToFile();
				totalWaitingTime += ((TheObject) object).measurement.myWaitingTime;
			}

			// the average total waiting time of all objects on their whole
			// route from start to finish
			double averageTotalWaitingTime = totalWaitingTime / outGoingQueue.size();

			Statistics.show(
					"Durchschnittliche GesamtWrtezeit eines Autos auf der ganzen Strecke : " + averageTotalWaitingTime);

			Statistics.writeToFile("\nDurchschnittliche Gesamtwartezeit eines Autos auf der ganzen Strecke : "
					+ averageTotalWaitingTime);

			writeToFile(averageTotalWaitingTime);

			//Simulation.getSimulationView().closeSimWindow(); // end simulation
			System.exit(0);
		}
	}
	/**
	 * Is called in endSimulation(); wirtes scenario stats to JSON file
	 * @param averageTotalWaitingTime
	 */
	private void writeToFile(double averageTotalWaitingTime) {
		Map<String, Double> mappedValues = new HashMap<>();
		mappedValues.put("avgWaitingTime", averageTotalWaitingTime);
		JSONWriter.writeToFile(mappedValues, Main.getAction());
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
