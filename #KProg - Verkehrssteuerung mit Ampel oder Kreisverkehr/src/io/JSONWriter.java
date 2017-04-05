package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Class that has the job to write car-, station- and scenario statistics to a JSON file
 * @author Verghelet
 * @version 03-12-2016
 *
 */
public class JSONWriter {
	/**The JSONObject that needs to be written to a file*/
	private static JSONObject jsObj;
	/**
	 * Getter for the Scenarios JSON file path.
	 * @return String the path of the JSON file where the Scenarios are stored
	 * as JSON Objects.
	 */
	public static String getScenarioJsonPath() {
		return SCENARIO_JSON_PATH;
	}
	/**Constant String that contains the path of the Stations JSON file.*/
	private final static String STATION_JSON_PATH="persistenz/Stations.json";
	/**Constant String that contains the path of the Cars JSON file.*/
	private final static String CAR_JSON_PATH="persistenz/Cars.json";
	/**Constant String that contains the path of the Scenarios JSON file.*/
	private final static String SCENARIO_JSON_PATH="persistenz/Scenarios.json";
	/**
	 * Stores the label and statistics in a JSON Object
	 * @param values the statistics of each object, that have a String as key and a double as value
	 * @param label the label of each JSON object
	 */
	private static void getStatistics(Map<String, Double> values, String label) {
		try {
			jsObj=new JSONObject();
			jsObj.put("label", label);
			JSONArray statistics = new JSONArray();
			statistics.put(values);
			jsObj.put("statistics", statistics);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Stores the data(statistics=which are located in the map; label=the string)
	 * @param values the statistics, where the key is a string and the value is a double
	 * @param label the label of each object
	 */
	public static void writeToFile(Map<String, Double> values, String label) {
		String filePath="";
		if(label.contains("Objekt"))
			filePath=CAR_JSON_PATH;
		else if(label.contains("Station"))
			filePath=STATION_JSON_PATH;
		else if(label.contains("Szenario"))
			filePath=SCENARIO_JSON_PATH;
		getStatistics(values, label);
		File file = new File(filePath);
		try (FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fileWriter)) {
			bw.write(jsObj.toString()+"\n");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
