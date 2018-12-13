package procyonScheduler.scheduler;

import java.util.GregorianCalendar;

/**
 * The Request class for ToggleFullDayHandler
 *
 */
public class ToggleFullDayRequest {
	boolean openOrClose; // True = open slots, False = close slots
	String scheduleID; // The ID of the schedule to toggle on
	String secretCode; // The secret code of the schedule to verify organizer
	// edit access
	String toggleMe; // The day to toggle

	/**
	 * Constructor
	 * 
	 * @param openOrClose
	 *            True = open slots, False = close slots
	 * @param scheduleID
	 *            The ID of the schedule to toggle on
	 * @param secretCode
	 *            The secret code of the schedule to verify organizer edit
	 *            access
	 * @param toggleMe
	 *            The day to toggle
	 */
	public ToggleFullDayRequest(boolean openOrClose, String scheduleID, String secretCode, String toggleMe) {
		this.openOrClose = openOrClose;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.toggleMe = toggleMe;
	}

	public String toString() {
		return "Manage(" + this.toggleMe + this.scheduleID + this.secretCode + "Change to " + this.openOrClose + ")";
	}
}
