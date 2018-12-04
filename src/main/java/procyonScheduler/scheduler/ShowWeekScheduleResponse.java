package procyonScheduler.scheduler;

public class ShowWeekScheduleResponse {
	String response;
	int httpCode;
	
	public ShowWeekScheduleResponse (String name, int code) {
		this.response = name;
		this.httpCode = code;
	}
	
	// 200 means success
	public ShowWeekScheduleResponse (String name) {
		this.response = name;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Schedule " + response + ". Schedule creation successful!";
	}
}
