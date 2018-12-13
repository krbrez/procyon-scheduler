package procyonScheduler.scheduler;

/**
 * The Response class for ExtendScheduleHandler
 *
 */
public class ExtendScheduleResponse {
	String response; // A response to show to the user
	int httpCode; // The HTTP Code for the lambda

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The response
	 * @param code
	 *            The HTTP code
	 */
	public ExtendScheduleResponse(String response, int code) {
		this.response = response;
		this.httpCode = code;
	}

	/**
	 * Constructor when successful
	 * 
	 * @param name
	 *            The response
	 */
	public ExtendScheduleResponse(String response) {
		this.response = response;
		this.httpCode = 200;
	}

	public String toString() {
		return response;
	}
}
