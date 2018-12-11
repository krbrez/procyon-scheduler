package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Schedule;

public class ReportActivityResponse {
	String response;
	ArrayList<Schedule> schedules;
	int httpCode;
	
	public ReportActivityResponse (String response, ArrayList<Schedule> schedules, int code) {
		this.response = response;
		this.schedules = schedules;
		this.httpCode = code;
	}
	
	// 200 means success
	public ReportActivityResponse (String response, ArrayList<Schedule> schedules) {
		this.response = response;
		this.schedules = schedules;
		this.httpCode = 200;
	}
	
	public String toString() {
		return response + " Here are the schedules as requested: " + schedules;
	}
}
