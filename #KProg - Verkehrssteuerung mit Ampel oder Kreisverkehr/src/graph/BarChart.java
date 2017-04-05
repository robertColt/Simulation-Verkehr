package graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * Class in which the bar chart calculations are made, where the chart is drawn.
 * 
 * @author Verghelet
 * @version 04-12-2016
 */
public class BarChart {
	/**The width of the barchart*/
	private int width;
	/**The height of the barchart*/
	private int height;
	/**Array with the bars which are contained in this barchart*/
	private ArrayList<Bar> bars;
	/**The space between bars(in pixel)*/
	private static final int BAR_SPACE=5;

	/**
	 * Creates a new BarChart(that contains all the bars)
	 * 
	 * @param width width of barchart
	 * @param height of barchart
	 */
	public BarChart(int width, int height) {
		this.width = width;
		this.height = height;
		bars = new ArrayList<>();
	}

	/**
	 * Adds a bar to the graphic
	 * 
	 * @param bar
	 *            contains a label, x dimension(double) and a color.
	 */
	public void add(Bar bar) {
		bars.add(bar);
	}

	/**
	 * The method that draws the the bars and the labels.
	 * 
	 * @param graph2D
	 *            object which is used to draw the BarChart(chart=only 2d)
	 */
	public void draw(Graphics2D graph2D) {

		int i = 0;
		double max = 0;
		// the font which is used for all the bar-labels
		Font customFont = new Font("TimesRoman", Font.BOLD, 14);
		//FontMetrics=used to get the dimensions of the font which is used for all the labels
		FontMetrics fontMet = graph2D.getFontMetrics(customFont);
		for (Bar bar : bars)
			if (max < bar.getValue())
				max = bar.getValue();
		// the width of the whole BarChart - 1
		int xWidth = width - 1;
		// the height of the whole BarChart - -1
		int yHeight = height - 1;
		// The x coordinate of the BarChart
		int xLeft = 0;
		
		graph2D.setFont(customFont);
		//FontMetrics=used to get the dimensions of the font which is used for all the labels
		
		for (i = 0; i < bars.size(); i++) {
			//the width of a bar
			int barWidth = xWidth / bars.size();
			//the x coordinate of the next bar
			int xRight=xLeft+barWidth;
			//set the bar height to be direct proportional to the bar value
			int barHeight = (int) Math.round(yHeight * bars.get(i).getValue() / max);
			//represents a bar
			Rectangle rect = new Rectangle(xLeft, yHeight - barHeight, barWidth, barHeight);
			//each rectangle gets filled with a different color
			graph2D.setPaint(bars.get(i).getColor());
			graph2D.fill(rect);
			//rectangle outline and labels are always black
			graph2D.setPaint(Color.BLACK);
			//label is drawn in each rectangle
			graph2D.drawString(bars.get(i).getLabel(), rect.x, rect.y + fontMet.getHeight());
			graph2D.draw(rect);
			//the X Coordinate of the next bar is the xRight coordinate of this one + the space between
			xLeft = xRight + BAR_SPACE;

		}
	}

}