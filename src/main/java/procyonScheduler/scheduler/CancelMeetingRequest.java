package procyonScheduler.scheduler;

/**
 * The request class for CancelMeetingHandler
 *
 */
public class CancelMeetingRequest {
	String id;	// The ID for the meeting to be cancelled
	String code; // The secret code of either the meeting or the associated schedule
	
	/**
	 * Constructor
	 * @param id The ID for the meeting to be cancelled
	 * @param code The secret code of either the meeting or the associated schedule
	 */
	public CancelMeetingRequest(String id, String code) {
		this.id = id;
		this.code = code;
	}
	
	public String toString() {
		return "Cancel meeting with this id " + id; 
	}

}
