package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for printing statistics
 * 
 * @author Jaeger, Schmidt, Colt, Verghelet
 * @version 2015-11-18
 */
public class Statistics {

	private static String buffer; 
	/** the file name in wich the statistics are going to be written in */
	private static final String STATISTICS_FILE = "persistenz/Statistische Auswertungen";
	
	/** appends the given String to the buffer
	 *
	 * @param message the message to append
	 */
	public static void update(String message) {
		
		buffer = buffer + message + "\n";
	}
	
	/** writes the given String to console
	 *
	 * @param message the message to write to console
	 */
	public static void show(String message) {
		
		System.out.println(message);
	}
	/**
	 * Writes the given string to console using System.err stream.
	 * @param message the message to be shown
	 */
	public static void showErr(String message){
		System.err.println(message);
	}
	/** writes the content String to the STATISTICS_FILE file
	 * @param content the content to be written to the file
	 * */	
	public static void writeToFile( String content) {
		File file = new File(STATISTICS_FILE);
		
		try (FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fileWriter)){

			bw.write("\n" + content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
