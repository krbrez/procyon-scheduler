package procyonScheduler.scheduler;

/**
 * The request class for DeleteScheduleHandler
 *
 */
public class DeleteScheduleRequest {
	String secretCode; // The secret code to access the schedule

	/**
	 * Constructor
	 * 
	 * @param secretCode
	 *            The secret code to access the schedule
	 */
	public DeleteScheduleRequest(String secretCode) {
		this.secretCode = secretCode;
	}

	public String toString() {
		return "Delete Schedule with this code " + secretCode;
	}

}
