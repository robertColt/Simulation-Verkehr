package view;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import io.Statistics;


/**
 * An abstract JLabel class for the views of our models (object, stations, queues)
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-07
 */
@SuppressWarnings("serial")
public abstract class ActorView extends JLabel{
			
	/** Creates a new actor view (JLabel)
	 * 
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 */
	protected ActorView(int xPos, int yPos){
		
		super();
		this.setLocation(xPos, yPos);
						
		//initialize mouse events
		this.initMouseEvents();
		
		//add this to the simulation view
		SimulationView.addActorView(this);

	}
		
	//TODO noch notwendig ???
	/** 
	 * Use this to add some mouse actions to the view
	 * 
	 */
	private void initMouseEvents(){
		
		this.addMouseListener(new MyMouseListener(this));
		
	}
		
	class MyMouseListener extends MouseAdapter {
		
		private ActorView view;
		
		/** constructor gets the ActorView Object
		 * @param v the ActorView object
		 */
		public MyMouseListener(ActorView v) {
			this.view = v;
		}
		
		/**
		 * a reaction to mouseclicks
		 * later something better, think ...
		 * @param the event
		 */
		public void mouseClicked(MouseEvent e){
		    	
		    	//Example: output of available information 	
		    	Point p = this.view.getLocation();
		    	Statistics.show("my location is: " + p.getX() + " , " + p.getY());
		    	
		       
		    }  
			
		}
		
	
}
