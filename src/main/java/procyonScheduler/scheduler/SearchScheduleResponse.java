package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Meeting;

public class SearchScheduleResponse {
	String response;
	ArrayList<Meeting> meetings;
	int httpCode;
	
	// non-200 constructor
	public SearchScheduleResponse(String response, int httpCode) {
		this.response = response;
		this.meetings = null;
		this.httpCode = httpCode;
	}
	
	// 200 constructor
	public SearchScheduleResponse(ArrayList<Meeting> meetings) {
		this.response = "success";
		this.meetings = meetings;
		this.httpCode = 200;
	}
	
	public String toString() {
		return response;
	}

}
