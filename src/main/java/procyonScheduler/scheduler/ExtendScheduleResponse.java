package procyonScheduler.scheduler;

public class ExtendScheduleResponse {
	String response;
	int httpCode;
	
	public ExtendScheduleResponse (String response, int code) {
		this.response = response;
		this.httpCode = code;
	}
	
	// 200 means success
	public ExtendScheduleResponse (String response) {
		this.response = response;
		this.httpCode = 200;
	}
	
	public String toString() {
		return response;
	}
}
