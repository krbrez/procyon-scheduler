package procyonScheduler.scheduler;

public class CreateScheduleResponse {
	String response;
	int httpCode;
	
	public CreateScheduleResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
