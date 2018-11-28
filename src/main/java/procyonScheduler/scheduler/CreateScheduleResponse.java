package procyonScheduler.scheduler;

public class CreateScheduleResponse {
	String response;
	int httpCode;
	
	public CreateScheduleResponse (String name, int code) {
		this.response = name;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String name) {
		this.response = name;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Schedule " + response + ". Schedule creation successful!";
	}
}
