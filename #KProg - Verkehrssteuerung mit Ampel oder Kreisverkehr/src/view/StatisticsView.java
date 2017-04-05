package view;

/** class for displaying the statistics for the station that holds an instance of it
 * @author Colt
 * */
public class StatisticsView extends TextView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** offset for horizontal alignment of the StatisticsLabel*/
	private final int X_OFFSET = - 60;
	
	/** offset for vertical alignment of the StatisticsLabel*/
	private final int Y_OFFSET = -20;
	
	/** the width of this label */
	private final int WIDTH = 30;
	
	/** the length of this label*/
	private final int LENGTH = 70;
	
	/** the description of this StatisticsView --> in order to know what displayed is */
	private final String DESCRIPTION = "Durchschnittliche Wartezeit/Auto";
	
	/** constructor for this class 
	 * creates additionally a StatisticsLabel on the view with the text "Durchschnittliche Wartezeit/Auto"
	 * */
	private StatisticsView(String text, int xPos, int yPos) {
		super(text, xPos, yPos);
		this.setSize(LENGTH, WIDTH);
		new StatisticsLabel(this.DESCRIPTION, xPos +this.X_OFFSET , yPos + this.Y_OFFSET);
	}
	
	/**private inner class that inherits from ActorView */
	private class StatisticsLabel extends ActorView{
		
		/**
		 * SerialVersionID
		 */
		private static final long serialVersionUID = 1L;

		/** Constructor for this class -> label with the String given by parameters will
		 * be displayed on the SimulationView
		 * @param text the text that this StatisticsLabel has
		 * @param xPos x coordinate of this label
		 * @param yPos y coordinate of this label
		 * */
		protected StatisticsLabel(String text, int xPos, int yPos) {
			super(xPos, yPos);
			this.setText(text);
			this.setSize(200, 10);
		}
		
	}
	
	/**creates and returns a new Statisticsview object with the parameters given
	 * @param text the text content of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 * 
	 * @return a new StatisticsView Object
	 *  */
	public static StatisticsView create(String text, int xPos, int yPos){
		return new StatisticsView(text, xPos, yPos);
	}

	

}

