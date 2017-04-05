package io;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import model.EndStation;
import model.MappedStation;
import model.RoundaboutStation;
import model.StartStation;
import model.Station;
import model.TheObject;
import model.TrafficLightStation;

/**
 * This is an abstract factory that creates instances of different types like
 * objects, start station and end station
 * 
 * @author Jaeger, Schmidt, Ilie, Dates
 * @version 2016-07-05
 */
public class Factory {

	/** the objects XML data file */
	private static String theObjectDataFile ;

	/** the stations XML data file */
	private static String theStationDataFile ;

	/** the start station XML data file */
	private static String theStartStationDataFile ;

	/** the end station XML data file */
	private static String theEndStationDataFile ;

	/**
	 * the x position of the starting station, also position for all starting
	 * objects
	 */
	private static int XPOS_STARTSTATION;

	/**
	 * the y position of the starting station, also position for all starting
	 * objects
	 */
	private static int YPOS_STARTSTATION;
	

	/**
	 * create the actors for the starting scenario
	 * @param paths an array of paths for the xml files 
	 */
	
	public static void createStartScenario(String paths[]) {
		/*
		 * Important: The start station must be created before the objects,
		 * because the objects constructor sets themselves into the start
		 * stations outgoing queue
		 */
		
		setPaths(paths);
		createStartStation();
		createProcessStations();
		createEndStation();
		createObjects();

	}
	/**
	 * Method where the xml-paths are set
	 * @param paths received from Simulation
	 */
	private static void setPaths(String[] paths){
		theObjectDataFile=paths[0];
		theStationDataFile=paths[1];
		theStartStationDataFile=paths[2];
		theEndStationDataFile=paths[3];
	}

	/**
	 * create the start station
	 * 
	 */
	private static void createStartStation() {

		try {

			// read the information from the XML file into a JDOM Document
			Document theXMLDoc = new SAXBuilder().build(theStartStationDataFile);

			// the <settings> ... </settings> node
			Element root = theXMLDoc.getRootElement();

			// get the start_station into a List object
			Element startStation = root.getChild("start_station");

			// get the label
			String label = startStation.getChildText("label");

			// position
			XPOS_STARTSTATION = Integer.parseInt(startStation.getChildText("x_position"));
			YPOS_STARTSTATION = Integer.parseInt(startStation.getChildText("y_position"));

			// the <view> ... </view> node
			Element viewGroup = startStation.getChild("view");
			// the image
			String image = viewGroup.getChildText("image");

			// creating a new StartStation object
			StartStation.create(label, XPOS_STARTSTATION, YPOS_STARTSTATION, image);

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create some objects out of the XML file
	 * 
	 */
	private static void createObjects() {

		try {

			// read the information from the XML file into a JDOM Document
			Document theXMLDoc = new SAXBuilder().build(theObjectDataFile);

			// the <settings> ... </settings> node, this is the files root
			// Element
			Element root = theXMLDoc.getRootElement();

			// get all the objects into a List object
			List<Element> allObjects = root.getChildren("object");

			// separate every JDOM "object" Element from the list and create
			// Java TheObject objects
			for (Element theObject : allObjects) {

				// data variables:
				String label = null;
				int processtime = 0;
				int speed = 0;
				String image = null;

				// read data
				label = theObject.getChildText("label");
				processtime = Integer.parseInt(theObject.getChildText("processtime"));
				speed = Integer.parseInt(theObject.getChildText("speed"));

				// the <view> ... </view> node
				Element viewGroup = theObject.getChild("view");
				// read data
				image = viewGroup.getChildText("image");

				// get all the stations, where the object wants to go to
				// the <sequence> ... </sequence> node
				Element sequenceGroup = theObject.getChild("sequence");

				List<Element> allMappedStations = sequenceGroup.getChildren("mappedstation");

				// get the elements into a queue
				Queue<MappedStation> stationsToGo = new LinkedList<MappedStation>();
				int entrance = 0;
				int exit = 0;
				String stationLabel = "";
				MappedStation st;
				for (Element theMappedStation : allMappedStations) {
					entrance = Integer.parseInt(theMappedStation.getChildText("entrance"));
					exit = Integer.parseInt(theMappedStation.getChildText("exit"));
					stationLabel = theMappedStation.getChildText("station");
					st = new MappedStation(entrance, exit, stationLabel);
					for (Station station : Station.getAllStations()) {
						if (station.getLabel().equals(st.getStationLabel()))
							st.setStation(station);
					}
					stationsToGo.add(st);

				}

				// creating a new TheObject object
				TheObject.create(label, stationsToGo, processtime, speed, XPOS_STARTSTATION, YPOS_STARTSTATION, image);

			}

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create some process stations out of the XML file
	 * 
	 */
	private static void createProcessStations() {

		try {

			// read the information from the XML file into a JDOM Document
			Document theXMLDoc = new SAXBuilder().build(theStationDataFile);

			// the <settings> ... </settings> node
			Element root = theXMLDoc.getRootElement();

			// get all the stations into a List object
			List<Element> stations = root.getChildren("station");

			// separate every JDOM "station" Element from the list and create
			// Java Station objects
			for (Element station : stations) {

				// data variables:
				String label = null;
				int inQueues = 0;
				int outQueues = 0;
				double troughPut = 0;
				int xPos = 0;
				int yPos = 0;
				String image = null;

				// read data
				label = station.getChildText("label");
				inQueues = Integer.parseInt(station.getChildText("inqueues"));
				outQueues = Integer.parseInt(station.getChildText("outqueues"));
				troughPut = Double.parseDouble(station.getChildText("troughput"));
				xPos = Integer.parseInt(station.getChildText("x_position"));
				yPos = Integer.parseInt(station.getChildText("y_position"));

				// the <view> ... </view> node
				Element viewGroup = station.getChild("view");
				// read data
				image = viewGroup.getChildText("image");

				// creating a new Station object
				if (label.contains("TrafficLightStation"))
					TrafficLightStation.create(label, inQueues, outQueues, troughPut, xPos, yPos, image);
				else
					RoundaboutStation.create(label, inQueues, outQueues, troughPut, xPos, yPos, image);

			}

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * create the end station
	 * 
	 */
	private static void createEndStation() {

		try {

			// read the information from the XML file into a JDOM Document
			Document theXMLDoc = new SAXBuilder().build(theEndStationDataFile);

			// the <settings> ... </settings> node
			Element root = theXMLDoc.getRootElement();

			// get the end_station into a List object
			Element endStation = root.getChild("end_station");

			// get label
			String label = endStation.getChildText("label");

			// position
			int xPos = Integer.parseInt(endStation.getChildText("x_position"));
			int yPos = Integer.parseInt(endStation.getChildText("y_position"));

			// the <view> ... </view> node
			Element viewGroup = endStation.getChild("view");
			// the image
			String image = viewGroup.getChildText("image");

			// creating a new EndStation object
			EndStation.create(label, xPos, yPos, image);

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
