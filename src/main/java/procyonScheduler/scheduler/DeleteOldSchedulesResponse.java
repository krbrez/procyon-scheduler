package procyonScheduler.scheduler;

public class DeleteOldSchedulesResponse {
	String response;
	int httpCode;
	
	public DeleteOldSchedulesResponse (String n, int code) {
		this.response = n;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteOldSchedulesResponse (String n) {
		this.response = n;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "All schedules " + response + "days old or older have been successfully deleted.";
	}
}
