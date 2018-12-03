package procyonScheduler.scheduler;

public class CreateMeetingResponse {
	String response;
	int httpCode;
	
	public CreateMeetingResponse (String name, int code) {
		this.response = name;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateMeetingResponse (String name) {
		this.response = name;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Meeting " + response + ". Meeting creation successful!";
	}
}
