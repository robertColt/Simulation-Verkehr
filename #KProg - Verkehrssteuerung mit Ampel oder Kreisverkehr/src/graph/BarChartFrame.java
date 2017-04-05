package graph;

import javax.swing.JFrame;

/**
 * @author Verghelet
 * @version 04-12-2016
 *	This class is used to show a new window with a {@link}BarChart
 */
@SuppressWarnings("serial")
public class BarChartFrame extends JFrame {
	/** frame width*/
	private static final int WIDTH = 670;
	/** frame height*/
	private static final int HEIGHT = 500;
	/**
	 * private constructor, is called only in openGrafik()
	 */
	private BarChartFrame() {
		super();
	}
	/**
	 * A new BarChartFrame is created and shown on screen.
	 */
	public static void openGrafik() {
		BarChartFrame barFrame = new BarChartFrame();

		barFrame.setSize(WIDTH, HEIGHT);
		barFrame.setTitle("Durchschnittliche Gesamtwartezeit eines Autos / Szenario");
		barFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		BarChartComponent component = new BarChartComponent();
		barFrame.add(component);

		barFrame.setVisible(true);
	}
}




