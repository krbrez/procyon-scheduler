package procyonScheduler.scheduler;

/**
 * The Response class for ManageTimeSlotHandler
 *
 */
public class ManageTimeSlotResponse {
	String response; // A response to show to the user
	int httpCode; // The HTTP Code for the lambda

	/**
	 * Constructor
	 * 
	 * @param response
	 *            The response
	 * @param code
	 *            The HTTP code
	 */
	public ManageTimeSlotResponse(String response, int code) {
		this.response = response;
		this.httpCode = code;
	}

	/**
	 * Constructor when successful
	 * 
	 * @param name
	 *            The response
	 */
	public ManageTimeSlotResponse(String response) {
		this.response = response;
		this.httpCode = 200;
	}

	public String toString() {
		return response + ", " + Integer.toString(httpCode);
	}
}
