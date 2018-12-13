package procyonScheduler.scheduler;

/**
 * The Response class for CreateMeetingHandler
 *
 */
public class CreateMeetingResponse {
	String response;	// The response to present to the user
	String label;	// The label that was attached to the meeting when scheduled
	String secretCode;	// The secret code the user will use to edit the meeting if desired
	int httpCode;	// The HTTP code to return
	
	/**
	 * Constructor for bad returns
	 * @param response The response to present to the user
	 * @param label The label the user put on the meeting
	 * @param code The secret code for the user
	 */
	public CreateMeetingResponse (String response, String label, int code) {
		this.response = response;
		this.httpCode = code;
		this.label = label;
		this.secretCode = "";
	}
	
	/**
	 * Constructor when successful
	 * @param response The response to present to the user
	 * @param label The label the user put on the meeting
	 * @param secretCode The secret code for the user
	 */
	public CreateMeetingResponse (String response, String label, String secretCode) {
		this.response = response;
		this.label = label;
		this.secretCode = secretCode;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Meeting " + label + ". With secret code: " + secretCode + ". Meeting creation successful!";
	}
}
