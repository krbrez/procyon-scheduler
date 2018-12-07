package procyonScheduler.scheduler;

public class CreateMeetingResponse {
	String response;
	String label;
	String secretCode;
	int httpCode;
	
	public CreateMeetingResponse (String response, String label, int code) {
		this.response = response;
		this.httpCode = code;
		this.label = label;
		this.secretCode = "";
	}
	
	// 200 means success
	public CreateMeetingResponse (String response, String label, String secretCode) {
		this.response = response;
		this.label = label;
		this.secretCode = secretCode;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Meeting " + label + ". With secret code: " + secretCode + ". Meeting creation successful!";
	}
}
