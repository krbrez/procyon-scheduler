package procyonScheduler.scheduler;

public class CreateScheduleResponse {
	String response;
	String id;
	String secretCode;
	int httpCode;
	
	public CreateScheduleResponse (String name, int code) {
		this.response = name;
		this.id = "";
		this.secretCode = "";
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String name, String id, String secretCode) {
		this.response = name;
		this.id = id;
		this.secretCode = secretCode;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You made Schedule " + response + ". Schedule creation successful!";
	}
}
