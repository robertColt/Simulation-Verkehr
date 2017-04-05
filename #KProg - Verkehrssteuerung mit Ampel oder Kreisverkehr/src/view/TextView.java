package view;

import java.awt.Color;

import javax.swing.BorderFactory;

/**
 * A JLabel class for views with text
 * 
 * @author Jaeger, Schmidt
 * @version 2016-02-03
 */
@SuppressWarnings("serial")
public abstract class TextView extends ActorView {
	
	/** the width of the JLabel */
	private static final int WIDTH = 30; 
	
	/** the height of the JLabel */
	private static final int HEIGHT = 30; 
	
	/** Creates a new view with text (JLabel)
	 * 
	 * @param text the text content of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 */
	protected TextView(String text, int xPos, int yPos){
		
		super(xPos, yPos);
		this.setSize(WIDTH, HEIGHT);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		this.setHorizontalAlignment(CENTER);
		this.setText(text);
	}
	
	/** Updates the text of the view
	 * 
	 * @param text the text content of the view
	 * 
	 */
	public void updateText(String text){
	
		this.setText(text);
		
	}
	
}
