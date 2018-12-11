package procyonScheduler.scheduler;

public class ToggleFullDayResponse {
	String response;
	int httpCode;
	
	public ToggleFullDayResponse (String response, int code) {
		this.response = response;
		this.httpCode = code;
	}
	
	// 200 means success
	public ToggleFullDayResponse (String response) {
		this.response = response;
		this.httpCode = 200;
	}
	
	public String toString() {
		return response + ", " + Integer.toString(httpCode);
	}
}
