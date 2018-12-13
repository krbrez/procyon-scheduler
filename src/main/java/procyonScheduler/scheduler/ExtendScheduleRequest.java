package procyonScheduler.scheduler;

/**
 * The request class for ExtendScheduleHandlet
 *
 */
public class ExtendScheduleRequest {
	String extDate; // The date to extend to
	String secretCode; // The secret code to edit the schedule

	/**
	 * Constructor
	 * 
	 * @param extDate
	 *            The date to extend to
	 * @param secretCode
	 *            The secret code to edit the schedule
	 */
	public ExtendScheduleRequest(String extDate, String secretCode) {
		this.extDate = extDate;
		this.secretCode = secretCode;
	}

	public String toString() {
		return "Extend(" + extDate + ", " + secretCode + ")";
	}
}
