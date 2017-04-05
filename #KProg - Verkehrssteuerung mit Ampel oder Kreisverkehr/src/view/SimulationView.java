package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Class for our main window
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-08
 */

@SuppressWarnings("serial")
public class SimulationView extends JFrame {

	/** main window width */
	private final static int WIDTH = 900;

	/** main window height */
	private final static int HEIGHTH = 600;

	/** main window title */
	private final static String TITLE = "Prototyp: allgemeine Objekt/Queue/Station Simulation";

	/** all actor views of our simulation */
	private static ArrayList<ActorView> allViews = new ArrayList<ActorView>();

	/**
	 * Creates a JFrame main window for our simulation
	 * 
	 */
	public SimulationView() {
		this.init();

	}

	/**
	 * initialize the main window
	 * 
	 */
	private void init() {

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHTH);
		this.setTitle(TITLE);

		// create a new SimulationPanel where our actor views can run
		SimulationPanel simulationPanel = new SimulationPanel();

		// create a start button
		StartButton startButton = new StartButton();

		// put the simulation panel into our JFrame
		this.getContentPane().add(simulationPanel, BorderLayout.CENTER);
		// put the start button into our JFrame
		this.getContentPane().add(startButton, BorderLayout.PAGE_END);

		this.setVisible(true);

	}

	/**
	 * Method used to close the simulation window when it ends
	 */
	/*public void closeSimWindow() {
		if (this != null) {
			this.setVisible(false);
			this.dispose();
		}
	}*/

	/**
	 * Add an actor view to the simulation view
	 * 
	 * @param theView
	 *            the actor view
	 */
	public static void addActorView(ActorView theView) {
		allViews.add(theView);

	}

	/**
	 * Inner JPanel class where the simulation runs
	 * 
	 */
	private class SimulationPanel extends JPanel {

		/**
		 * Constructor initializes the panel
		 * 
		 */
		public SimulationPanel() {

			this.setBackground(Color.WHITE);

			// this switch gives us a layout which is controllable by
			// coordinates,
			// according to our models
			this.setLayout(null);

			// put all actor views in the panel
			for (ActorView view : allViews) {
				this.add(view);
			}

		}

	}

}
