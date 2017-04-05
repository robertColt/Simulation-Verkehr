package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Class that reads the JSON scenario file and that can return the scenario
 * labels and values.
 * 
 * @author Verghelet
 * @version 2016-12-04
 */
public class JSONScenarioParser {
	/** The JSON Objects(all scenarios) are stored in this container. */
	private static ArrayList<JSONObject> jsObjects;
	/**
	 * A map that contains the scenario label and value(label=key; value=value)
	 */
	private static Map<String, Double> mappedScenarios;

	/**
	 * Populates the jsObjects container by reading the JSON scenario file.
	 */
	private static void parseJson() {
		jsObjects = new ArrayList<>();
		String line;
		try (FileReader file = new FileReader(JSONWriter.getScenarioJsonPath());
				BufferedReader rd = new BufferedReader(file);) {
			while ((line = rd.readLine()) != null) {
				jsObjects.add(new JSONObject(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Populates the labels container with the label of each Scenario JSON
	 * Object.
	 * 
	 * @return ArrayList<String> that contains the labels of each scenario.
	 */
	private static ArrayList<String> getLabels() {
		ArrayList<String> labels = new ArrayList<>();
		for (JSONObject jsonObject : jsObjects) {
			try {
				labels.add((String) jsonObject.get("label"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return labels;

	}

	/**
	 * Populates an ArrayList with jsonArrays(that contain the statistics,
	 * withouth the label)
	 * 
	 * @return ArrayList<JSONArray> all the statistics for each scenario
	 */
	private static ArrayList<JSONArray> getJSONStatistics() {
		ArrayList<JSONArray> jsStatistics = new ArrayList<>();
		JSONArray result = new JSONArray();

		for (JSONObject jsonObj : jsObjects) {
			try {
				result = jsonObj.getJSONArray("statistics");
				jsStatistics.add(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsStatistics;
	}

	/**
	 * Adds all the average waiting times of a car to a container.
	 * 
	 * @return ArrayList<Double> the avgWaitingTime of a car for each scenario.
	 */
	private static ArrayList<Double> getScenarioStatistics() {
		ArrayList<JSONArray> array = getJSONStatistics();
		ArrayList<Double> values = new ArrayList<>();
		for (JSONArray jsonArray : array) {
			try {
				// Object value gets the value of the Double/Integer associated
				// with the avgWaitingTime key
				Object value = jsonArray.getJSONObject(0).get("avgWaitingTime");
				// If value has no decimal point, it is returned as a Integer,
				// so it needs a double casting
				if (value instanceof Integer) {
					Integer intValue = new Integer((int) value);
					values.add(intValue.doubleValue());
				} else {
					values.add((double) value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	/**
	 * Here is the JSON file parsed, then the labels and values of each scenario
	 * are added to a map.
	 * 
	 * @return mappedScenario Map<String, Double> contains label of a scenario(String) and
	 *         average waiting time of a car(double).
	 */
	public static Map<String, Double> getMappedScenarios() {
		parseJson();
		ArrayList<String> labels = getLabels();
		ArrayList<Double> values = getScenarioStatistics();
		mappedScenarios = new HashMap<>();
		for (int i = 0; i < labels.size(); i++) {
			mappedScenarios.put(labels.get(i), values.get(i));
		}
		return mappedScenarios;

	}
}
