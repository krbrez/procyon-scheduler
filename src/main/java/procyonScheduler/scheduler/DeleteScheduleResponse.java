package procyonScheduler.scheduler;

public class DeleteScheduleResponse {
	String response;
	int httpCode;
	
	public DeleteScheduleResponse(String name, int code) {
		this.response = name;
		this.httpCode = code;
	}
	
	//200 means success 
	public DeleteScheduleResponse(String name) {
		this.response = name;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "You deleted Schedule " + response + ". Schedule deletion successful!";
	}

}
