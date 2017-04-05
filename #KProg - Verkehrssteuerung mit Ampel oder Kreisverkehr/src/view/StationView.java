package view;

import java.util.Observable;
import java.util.Observer;

/**
 * Class for the view of stations
 * 
 * @author Jaeger, Schmidt, Colt
 * @version 2016-07-15
 */
@SuppressWarnings("serial")
public class StationView extends ImageView implements Observer {
	
	/** the statistics view for this station
	 * it will appear below the station
	 */
	private StatisticsView statisticsView;


	/** Creates a new view with a an image (JLabel)
	 * 
	 * @param image image of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 */
	private StationView(String image, int xPos, int yPos){
		super(image, xPos, yPos);
	}
	
	
	/** Creates a new view with a an image (JLabel)
	 * 
	 * @param image image of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 * @param 
	 */
	private StationView(String image, int xPos, int yPos, boolean withStatistics){
		super(image, xPos, yPos);
		this.statisticsView = StatisticsView.create("0", xPos+20, yPos+150);
	}
	
	
	/** Creates a new view of the station
	 * 
	 * @param image image of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 * @param withStatistics if this view should have statistics label or not
	 * @return the StationView
	 */
	public static StationView create(String image, int xPos, int yPos, boolean withStatistics){
		if(withStatistics){
			return new StationView(image, xPos, yPos, true);
		}
		else{
			return new StationView(image, xPos, yPos);
		}
	}

	/**
	 * method for updating this Observer
	 */
	@Override
	public void update(Observable observable, Object value) {
		String newValue = ((Double)value).toString();
		
		//update the text in the statistics
		statisticsView.updateText(newValue);
	}

}
