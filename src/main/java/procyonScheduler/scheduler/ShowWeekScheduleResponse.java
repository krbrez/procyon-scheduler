package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Meeting;


public class ShowWeekScheduleResponse {
	String id;
	String startDay;
	int blockSize;
	ArrayList<Meeting> meetings;
	int httpCode;
	
	public ShowWeekScheduleResponse (String id, String startDay, int code) {
		this.id = id;
		this.startDay = startDay;
		this.blockSize = 0;
		this.meetings = new ArrayList<Meeting>();
		this.httpCode = code;
	}
	
	// 200 means success
	public ShowWeekScheduleResponse (String id, String startDay, int blockSize, ArrayList<Meeting> meetings) {
		this.id = id;
		this.startDay = startDay;
		this.blockSize = blockSize;
		this.meetings = meetings;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You requested the week starting " + this.startDay + " of schedule " + this.id + ". Meetings found: " + this.meetings.toString();
	}
}
