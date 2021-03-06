package procyonScheduler.scheduler;

/**
 * The Response class for CancelMeetingHandler
 *
 */
public class CancelMeetingResponse {
	String response; // A response to show to the user
	int httpCode;	// The HTTP Code for the lambda
	
	/**
	 * Constructor
	 * @param name The response
	 * @param code The HTTP code
	 */
	public CancelMeetingResponse(String name, int code) {
		this.response = name;
		this.httpCode = code;
	}
	
	/**
	 * Constructor when successful
	 * @param name The response
	 */
	public CancelMeetingResponse(String name) {
		this.response = name;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You cancelled meeting" + response + ". Meeting cancelled successfully.";
	}

}
