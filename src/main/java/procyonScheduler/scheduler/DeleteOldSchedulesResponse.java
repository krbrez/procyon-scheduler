package procyonScheduler.scheduler;

/**
 * The Response class for DeleteOldSchedulesResponse
 *
 */
public class DeleteOldSchedulesResponse {
	String response;	// A response to show the user
	int httpCode;	// The HTTP code for the lambda
	
	/**
	 * Constructor 
	 * @param n The response
	 * @param code The HTTP code
	 */
	public DeleteOldSchedulesResponse (String n, int code) {
		this.response = n;
		this.httpCode = code;
	}
	
	/**
	 * Constructor when successful
	 * @param n The response
	 */
	public DeleteOldSchedulesResponse (String n) {
		this.response = n;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "All schedules " + response + "days old or older have been successfully deleted.";
	}
}
