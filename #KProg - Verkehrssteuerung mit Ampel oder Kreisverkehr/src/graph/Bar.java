package graph;

import java.awt.Color;
/**
 * Class that contains the values that are represented by a bar in the barchart
 * @author Verghelet
 *
 */
public class Bar {
	/**Double value, represents the height of the bar*/
	private double value;
	/**Represents the label of the bar(scenario label)*/
	private String label;
	/**Represents the color of the bar*/
	private Color color;
	/**
	 * Constructs a bar object, that has a specific height(value), label(label) and color(color)
	 * @param value the height 
	 * @param label the label
	 * @param color the color
	 */
	public Bar(double value, String label, Color color) {
		this.value = value;
		this.label = label;
		this.color = color;
	}
	/**
	 * Getter for the value attribute
	 * @return value the height of the bar(double)
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Getter for the label attribute(the label of the scenario)
	 * @return label the label of the bar(String)
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * Getter for the color of a the bar
	 * @return color the color of the bar(java.awt.Color)
	 */
	public Color getColor() {
		return color;
	}

}
