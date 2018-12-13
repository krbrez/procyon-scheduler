package procyonScheduler.scheduler;

/**
 * The request class for ToggleByTimeHandler
 *
 */
public class ToggleByTimeRequest {
	boolean openOrClose; // True = open slots, False = close slots
	String scheduleID; // The ID of the schedule to toggle on
	String secretCode; // The secret code of the schedule to verify organizer
						// edit access
	String toggleMe; // The time to toggle at

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
	 *            The time to toggle at
	 */
	public ToggleByTimeRequest(boolean openOrClose, String scheduleID, String secretCode, String toggleMe) {
		this.openOrClose = openOrClose;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.toggleMe = toggleMe;
	}

	public String toString() {
		// might want to change this
		return "Manage(" + this.toggleMe + this.scheduleID + this.secretCode + "Change to " + this.openOrClose + ")";
	}
}
