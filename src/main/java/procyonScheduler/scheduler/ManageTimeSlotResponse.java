package procyonScheduler.scheduler;

public class ManageTimeSlotResponse {
	String response;
	int httpCode;
	
	public ManageTimeSlotResponse (String response, int code) {
		this.response = response;
		this.httpCode = code;
	}
	
	// 200 means success
	public ManageTimeSlotResponse (String response) {
		this.response = response;
		this.httpCode = 200;
	}
	
	public String toString() {
		return response + ", " + Integer.toString(httpCode);
	}
}
