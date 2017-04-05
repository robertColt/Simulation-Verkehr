package view;

import javax.swing.ImageIcon;


/**
 * A JLabel class for views with an image
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-15
 */
@SuppressWarnings("serial")
public abstract class ImageView extends ActorView {
			
	/** Creates a new view with a an image (JLabel)
	 * 
	 * @param image image of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 */
	protected ImageView(String image, int xPos, int yPos){
		
		super(xPos, yPos);
				
		//set up the image
		ImageIcon imageIcon = new ImageIcon(image);
		this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
		this.setIcon(imageIcon);
		
	
	}
	

}
