package procyonScheduler.scheduler;

/**
 * The request class for ManageTimeSlotHandler
 *
 */
public class ManageTimeSlotRequest {
	String meetingID; // The ID for the meeting to be toggled
	String secretCode; // The secret code of the schedule, ensuring schedule
						// edit access

	/**
	 * Constructor
	 * 
	 * @param meetingID
	 *            The ID for the meeting to be toggled
	 * @param secretCode
	 *            The secret code of the schedule, ensuring schedule edit access
	 */
	public ManageTimeSlotRequest(String meetingID, String secretCode) {
		this.meetingID = meetingID;
		this.secretCode = secretCode;
	}

	public String toString() {
		return "Manage(" + this.meetingID + this.secretCode + ")";
	}
}
