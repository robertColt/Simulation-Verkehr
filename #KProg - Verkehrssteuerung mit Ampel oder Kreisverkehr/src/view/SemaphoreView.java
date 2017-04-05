package view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;

import model.Station;
/**
 * @author Colt
 * View that is attached to every TrafficLightStation, that changes its colors.
 *
 */
@SuppressWarnings("serial")
public class SemaphoreView extends ImageView implements Observer{
	/**Path of the image where semaphores WEST and EAST are green*/
	private static final String HORIZONTAL_GREEN="isGreenHorizontal.png";
	
	/**Path of the image where semaphores SOUTH and NORTH are green*/
	private static final String VERTICAL_GREEN="isGreenVertical.png";
	
	/**The offset on the X Axis of the view relative to the station*/
	private static final int X_OFFSET=72;
	
	/**The offset on the Y Axis of the view relative to the station*/
	private static final int Y_OFFSET=-72;
	
	
	protected SemaphoreView(String image, int xPos, int yPos) {
		super(image, xPos, yPos);
		
	}
	/**
	 * Creates an image view for that is relative to a TrafficLightStation  
	 * @param station the station for which the View has to be created
	 * @param isGreenHorizontal boolean attribute from TrafficLightStation, true if direction East - WEst has green light
	 * @return SemaphoreView
	 */
	public static SemaphoreView createSemaphore(Station station, boolean isGreenHorizontal){
		int x=0;
		int y=0;
		x=station.getXPos()+X_OFFSET;
		y=station.getYPos()+Y_OFFSET;
		if(isGreenHorizontal)
			return new SemaphoreView(HORIZONTAL_GREEN, x, y);
		else
			return new SemaphoreView(VERTICAL_GREEN, x,y);
		
	}
	/**
	 * Method that changes the image of the view(2 options:green horizontal or green vertical(if isGreenHorizontal is false))
	 * @param isGreenHorizontal  boolean attribute from class TrafficLightStation
	 */
	public void setIcon(boolean isGreenHorizontal){
		ImageIcon icon=null;
		if(isGreenHorizontal)
			icon=new ImageIcon(HORIZONTAL_GREEN);
		else
			icon=new ImageIcon(VERTICAL_GREEN);
		this.setIcon(icon);
	}
	
	/**
	 * Method that updates the image of this view, when it gets notified by the LightSwitcher(class TrafficLightStation)
	 * @param observable the LightSwitcher from TrafficLightStation
	 * @param isGreenHorizontal boolean attribute from TrafficLightStation, true if direction from West to East green
	 */
	@Override
	public void update(Observable observable , Object isGreenHorizontal) {
		ImageIcon icon=null;
		if((Boolean)isGreenHorizontal)
			icon=new ImageIcon(HORIZONTAL_GREEN);
		else
			icon=new ImageIcon(VERTICAL_GREEN);
		this.setIcon(icon);
	}
	
}

