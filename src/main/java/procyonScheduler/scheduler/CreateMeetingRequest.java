package procyonScheduler.scheduler;

/**
 * The Request class for CreateMeetingHandler
 *
 */
public class CreateMeetingRequest {
	String id; // The ID for the meeting to be scheduled
	String label; // The label to be attached to said meeting

	/**
	 * Class constructor
	 * 
	 * @param id
	 *            The ID for the meeting to be scheduled
	 * @param label
	 *            The label to be attached to said meeting
	 */
	public CreateMeetingRequest(String id, String label) {
		this.id = id;
		this.label = label;
	}

	public String toString() {
		return "Create(" + this.id + "," + this.label + "," + ")";
	}
}
