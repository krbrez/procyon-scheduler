package procyonScheduler.scheduler;

/**
 * The Response class for CreateScheduleHandler
 *
 */
public class CreateScheduleResponse {
	String response;	// A response to show the user
	String id;	// The unique ID of the schedule
	String secretCode;	// The unique secret code to edit the schedule
	int httpCode;	// The HTTP code
	
	/**
	 * Constructor
	 * @param name The response
	 * @param code The HTTP code
	 */
	public CreateScheduleResponse (String name, int code) {
		this.response = name;
		this.id = "";
		this.secretCode = "";
		this.httpCode = code;
	}
	
	/**
	 * Constructor when successful
	 * @param name The response
	 * @param id The ID of the schedule
	 * @param secretCode The secret code of the schedule
	 */
	public CreateScheduleResponse (String name, String id, String secretCode) {
		this.response = name;
		this.id = id;
		this.secretCode = secretCode;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Schedule " + response + ". Schedule creation successful!";
	}
}
