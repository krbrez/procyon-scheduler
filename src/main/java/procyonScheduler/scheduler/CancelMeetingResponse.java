package procyonScheduler.scheduler;

public class CancelMeetingResponse {
	String response;
	int httpCode;
	
	public CancelMeetingResponse(String name, int code) {
		this.response = name;
		this.httpCode = code;
	}
	
	//200 means success 
	public CancelMeetingResponse(String name) {
		this.response = name;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You cancelled meeting" + response + ". Meeting cancelled successfully.";
	}

}
