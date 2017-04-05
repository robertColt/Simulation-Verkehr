package graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JComponent;

import io.JSONScenarioParser;

/**
 * Class that instantiates a new BarChart and makes it a JComponent to be later
 * added to a JFrame
 * 
 * @author Verghelet
 * @version 05-12-2016
 *
 */
@SuppressWarnings("serial")
public class BarChartComponent extends JComponent {
	/** Array that contains the colors that are used for the bars */
	private static final Color[] colors = { new Color(175, 255, 47), new Color(255, 215, 0), new Color(255, 193, 37),
			new Color(255, 69, 0) };

	/**
	 * The contents of the JSON scenario file are extracted in this method and used to draw a BarChart
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		BarChart barChart = new BarChart(getWidth(), getHeight());
		HashMap<String, Double> scenarios = new HashMap<>(JSONScenarioParser.getMappedScenarios());
		int i = 0;
		for (String key : scenarios.keySet()) {
			barChart.add(new Bar(scenarios.get(key), key, colors[i]));
			i++;
		}
		barChart.draw(g2);
	}
}