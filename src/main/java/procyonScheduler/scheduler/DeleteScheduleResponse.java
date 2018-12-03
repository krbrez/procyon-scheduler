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
		//this.httpCode = 400;
		//this.httpCode = 404;
	}
	
	//400 means bad input parameter
	
	//404 means schedule not found

}
