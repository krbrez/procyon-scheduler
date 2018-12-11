package procyonScheduler.scheduler;

public class ToggleByTimeResponse {
	String response;
	int httpCode;
	
	public ToggleByTimeResponse (String response, int code) {
		this.response = response;
		this.httpCode = code;
	}
	
	// 200 means success
	public ToggleByTimeResponse (String response) {
		this.response = response;
		this.httpCode = 200;
	}
	
	public String toString() {
		return response + ", " + Integer.toString(httpCode);
	}
}
