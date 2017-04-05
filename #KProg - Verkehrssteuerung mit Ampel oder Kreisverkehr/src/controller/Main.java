package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import graph.BarChartFrame;

/**
 * @author Ilie, Dates
 * @version 04-12-2016 The class from where the scenarios can be chosen Main
 *          method can be found here(start application class)
 *
 */
public class Main {
	/** String where the label of the button which has been pressed is saved */
	private static String action;

	/**
	 * Method that builds the JFrame with JButtons for each scenario
	 */
	private static void chooseScenario() {

		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());
		frame.setSize(400, 400);
		JPanel panel = new JPanel();
		GridLayout gridLayout = new GridLayout(2, 2);
		panel.setLayout(gridLayout);
		ActionListener scenarioListener = new ChooseScenarioListener();
		JButton firstScenario = new JButton("Szenario 1: Ampel-Ampel");
		firstScenario.addActionListener(scenarioListener);
		panel.add(firstScenario);
		JButton secondScenario = new JButton("Szenario 2:  Kreisv.-Kreisv.");
		secondScenario.addActionListener(scenarioListener);
		panel.add(secondScenario);
		JButton thirdScenario = new JButton("Szenario 3: Kreisv.-Ampel");
		thirdScenario.addActionListener(scenarioListener);
		panel.add(thirdScenario);
		JButton fourthScenario = new JButton("Szenario 4: Ampel-Kreisv.");
		fourthScenario.addActionListener(scenarioListener);
		JButton openGrafik = new JButton("Szenarien Grafik");
		openGrafik.addActionListener(scenarioListener);
		pane.add(openGrafik, BorderLayout.SOUTH);

		panel.add(fourthScenario);
		pane.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	/**
	 * Getter that returns a string(label of one of the buttons)
	 * 
	 * @return String contains the label of the button which has been pressed
	 */
	public static String getAction() {
		return action;
	}

	/**
	 * Array of strings, where paths of the images are saved to be sent to
	 * factory
	 */
	private static String[] paths = new String[4];
	/** Variable that becomes true when a button is pressed */
	private static boolean scenarioChosen = false;

	/**
	 * 
	 * ActionListener nested class that handles the press button action.
	 *
	 */
	private static class ChooseScenarioListener implements ActionListener {

		/**
		 * Method that chooses the scenario according to the button pressed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Szenarien Grafik"))
				BarChartFrame.openGrafik();
			else {
				if (e.getActionCommand().equals("Szenario 1: Ampel-Ampel")) {
					// saving the button label
					action = e.getActionCommand();

					paths[0] = "xml/Szenario 1/object.xml";

					paths[1] = "xml/Szenario 1/station.xml";

					paths[2] = "xml/Szenario 1/startstation.xml";

					paths[3] = "xml/Szenario 1/endstation.xml";

				} else if (e.getActionCommand().equals("Szenario 2:  Kreisv.-Kreisv.")) {
					// saving the button label
					action = e.getActionCommand();
					paths[0] = "xml/Szenario 2/object.xml";

					paths[1] = "xml/Szenario 2/station.xml";

					paths[2] = "xml/Szenario 2/startstation.xml";

					paths[3] = "xml/Szenario 2/endstation.xml";

				} else if (e.getActionCommand().equals("Szenario 3: Kreisv.-Ampel")) {

					// saving the button label
					action = e.getActionCommand();
					paths[0] = "xml/Szenario 3/object.xml";

					paths[1] = "xml/Szenario 3/station.xml";

					paths[2] = "xml/Szenario 3/startstation.xml";

					paths[3] = "xml/Szenario 3/endstation.xml";

				} else if (e.getActionCommand().equals("Szenario 4: Ampel-Kreisv.")) {
					// saving the button label
					action = e.getActionCommand();
					paths[0] = "xml/Szenario 4/object.xml";

					paths[1] = "xml/Szenario 4/station.xml";

					paths[2] = "xml/Szenario 4/startstation.xml";

					paths[3] = "xml/Szenario 4/endstation.xml";

				}

				((JButton) e.getSource()).setBackground(Color.GREEN);
				Simulation.setScenario(e.getActionCommand());
				scenarioChosen = true;
				
				//wait for the scenario to be chosen
				while(!scenarioChosen){
					
				}
				
				//initialize simulation
				Simulation.createInstance().init(paths);
			}
		}

	}

	/**
	 * The main method where everything starts.
	 * 
	 * @param args
	 */
	public static void main(String... args) {

		// choose the desired scenario
		chooseScenario();

		
		// start simulation with the paths coresponding to the chosen scenario
		//new Simulation().init(paths);
		

	}
}
