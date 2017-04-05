package view;

/**
 * Class for the view of objects
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-15
 */
@SuppressWarnings("serial")
public class ObjectView extends ImageView {

	/** Creates a new view with a an image (JLabel)
	 * 
	 * @param image image of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 */
	private ObjectView(String image, int xPos, int yPos){
		
		super(image, xPos, yPos);
		
	}
	
	
	/** Creates a new view of the object
	 * 
	 * @param image image of the view
	 * @param xPos x position of the view
	 * @param yPos y position of the view
	 * 
	 * @return the ObjectView
	 */
	public static ObjectView create(String image, int xPos, int yPos){
	
		return new ObjectView(image, xPos, yPos);
		
	}

}
