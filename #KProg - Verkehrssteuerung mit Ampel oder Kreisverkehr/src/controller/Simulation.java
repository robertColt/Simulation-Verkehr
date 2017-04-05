package controller;

import java.util.concurrent.atomic.AtomicLong;

import io.Factory;
import io.Statistics;
import model.Actor;
import view.SimulationView;

/**
 * Controls the flow of the simulation Singleton pattern: there can be only one
 * simulation running at the same time
 *
 * @author Jaeger, Schmidt, Verghelet
 * @version 2016-07-07
 */
public final class Simulation {

	/** is the simulation running */
	public static boolean isRunning = false;

	/**
	 * a speed factor for the clock to vary the speed of the clock in a simple
	 * way
	 */
	public static int SPEEDFACTOR = 1;

	/**
	 * the beat or speed of the clock, e.g. 300 means one beat every 300 milli
	 * seconds
	 */
	public static final int CLOCKBEAT = 300 * SPEEDFACTOR;

	/** the global clock */
	// the clock must be thread safe -> AtomicLong. The primitive type long
	// isn't, even if synchronized
	private static AtomicLong clock = new AtomicLong(0);

	/**
	 * The scenario this simulation is running
	 */
	private static String scenario;

	/** only instance of this class */
	private static Simulation simulation = null;

	/**
	 * private constructor of this class -> Singleton
	 */
	private Simulation(){}

	

	/**
	 * static method for getting this class' instance if the class has been
	 * instantiated the already available instance will be returned
	 * 
	 * @return single instance of this class
	 */
	public static Simulation createInstance() {
		if (simulation == null)
			simulation = new Simulation();
		return simulation;

	}

	/**
	 * initialize the simulation
	 * 
	 * @param paths
	 *            container for xml-paths that are received from Main
	 */
	protected void init(String paths[]) {

		// create all stations and objects for the starting scenario out of XML
		Factory.createStartScenario(paths);

		// the view of our simulation
		new SimulationView();

		// set up the the heartbeat (clock) of the simulation
		new HeartBeat().start();

		Statistics.show("---- Simulation gestartet ---\n");

		// start all the actor threads
		for (Actor actor : Actor.getAllActors()) {
			actor.start();

		}

	}

	/**
	 * The heartbeat (the pulse) of the simulation, controls the clock.
	 * 
	 */
	private class HeartBeat extends Thread {

		@Override
		public void run() {

			while (true) {

				try {

					Thread.sleep(CLOCKBEAT);

					// Increase the global clock
					clock.incrementAndGet();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * Get the global time
	 * 
	 * @return the global time
	 */
	public static long getGlobalTime() {
		return clock.get();
	}

	/**
	 * sets the name of the scenario this simulation is going to depict
	 * @param scenarioName the name of the scenario can be found on the buttons at the beginning of the programm
	 * @see scenario static class variable
	 */
	public static void setScenario(String scenarioName) {
		scenario = scenarioName;
	}

	/** returns the name of the scenario this simulation is running
	 * @return scenario the scenario of the simulation
	 * @see scenario
	 *  */
	public static String getScenario() {
		return scenario;
	}

}
