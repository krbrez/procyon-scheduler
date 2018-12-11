package procyonScheduler.scheduler;

public class ReportActivityResponse {
	String response;
	int httpCode;
	
	public ReportActivityResponse (String n, int code) {
		this.response = n;
		this.httpCode = code;
	}
	
	// 200 means success
	public ReportActivityResponse (String n) {
		this.response = n;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "All meetings " + response + "days old or older have been successfully deleted.";
	}
}
