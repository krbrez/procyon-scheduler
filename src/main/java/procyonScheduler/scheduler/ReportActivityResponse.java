package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Schedule;

public class ReportActivityResponse {
	ArrayList<Schedule> schedules;
	int httpCode;
	
	public ReportActivityResponse (ArrayList<Schedule> schedules, int code) {
		this.schedules = schedules;
		this.httpCode = code;
	}
	
	// 200 means success
	public ReportActivityResponse (ArrayList<Schedule> schedules) {
		this.schedules = schedules;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Here are the schedules as requested: " + schedules;
	}
}
