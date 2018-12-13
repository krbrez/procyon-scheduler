package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Meeting;

public class SearchScheduleResponse {
	String response; // A response to show the user
	ArrayList<Meeting> meetings; // The list of meetings that fit the criteria
	int httpCode; // The HTTP Code for the lambda

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The response
	 * @param meetings
	 *            The list of meetings
	 * @param code
	 *            The HTTP code
	 */
	public SearchScheduleResponse(String response, int httpCode) {
		this.response = response;
		this.meetings = null;
		this.httpCode = httpCode;
	}

	/**
	 * Constructor when successful
	 * 
	 * @param meetings
	 *            The list of meetings
	 */
	public SearchScheduleResponse(ArrayList<Meeting> meetings) {
		this.response = "success";
		this.meetings = meetings;
		this.httpCode = 200;
	}

	public String toString() {
		return response;
	}

}
