package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Schedule;

/**
 * The response class for ReportActivityHandler
 *
 */
public class ReportActivityResponse {
	String response; // A response to show the user
	ArrayList<Schedule> schedules; // The list of applicable schedules
	int httpCode; // The HTTP code for the lambda

	/**
	 * Constructor
	 * 
	 * @param response
	 *            The response
	 * @param schedules
	 *            The list of schedules
	 * @param code
	 *            The HTTP code
	 */
	public ReportActivityResponse(String response, ArrayList<Schedule> schedules, int code) {
		this.response = response;
		this.schedules = schedules;
		this.httpCode = code;
	}

	/**
	 * Constructor when successful
	 * 
	 * @param response
	 *            The response
	 * @param schedules
	 *            The list of schedules
	 */
	public ReportActivityResponse(String response, ArrayList<Schedule> schedules) {
		this.response = response;
		this.schedules = schedules;
		this.httpCode = 200;
	}

	public String toString() {
		return response + " Here are the schedules as requested: " + schedules;
	}
}
