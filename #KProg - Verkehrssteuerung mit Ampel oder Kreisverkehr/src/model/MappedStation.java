package model;
/**
 * Class for the relation between TheObject and Station, which has entrance and exit index for the station
 * @author Verghelet
 *
 */
public class MappedStation {
	/**The String of the station*/
	private String stationLabel;
	/**The entrance index where TheObject comes in into a station(from 0 to 3)*/
	private int entranceNr;
	/**The eixt index where TheObject goes out of a station(from 0 to 3)*/
	private int exitNr;
	/**The Station which is mapped(has the same label as stationLabel*/
	private Station station;
	/**
	 * Getter for the station which is mapped
	 * @return Station 
	 */
	public Station getStation() {
		return station;
	}
	/**
	 * Getter for the label of the station which is mapped
	 * @return String
	 */
	public String getStationLabel() {
		return stationLabel;
	}
	/**
	 * Setter for the station
	 * @param station the station
	 */
	public void setStation(Station station) {
		this.station = station;
	}
	/**
	 * Constructs a MappedStation
	 * @param entranceNr index of entering queue (0,1,2,3) corresponding (west, south, east, north)
	 * @param exitNr index of exiting queue (0,1,2,3) corresponding (west, south, east, north)
	 * @param stationLabel the label of the station
	 */
	public MappedStation(int entranceNr, int exitNr, String stationLabel) {
		this.stationLabel = stationLabel;
		this.entranceNr = entranceNr;
		this.exitNr = exitNr;

	}
	/**
	 * Getter for the entrance number
	 * @return int the entrance index
	 */
	public int getEntranceNr() {
		return entranceNr;
	}
	/**
	 * Getter for the exit number
	 * @return int the exit index
	 */
	public int getExitNr() {
		return exitNr;
	}

}
