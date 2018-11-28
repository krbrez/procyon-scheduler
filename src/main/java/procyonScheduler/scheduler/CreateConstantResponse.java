package procyonScheduler.scheduler;

public class CreateConstantResponse {
	String response;
	int httpCode;
	
	public CreateConstantResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateConstantResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
