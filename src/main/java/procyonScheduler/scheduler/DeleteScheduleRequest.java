package procyonScheduler.scheduler;

public class DeleteScheduleRequest {
	String secretCode;
	
	public DeleteScheduleRequest(String secretCode) {
		this.secretCode = secretCode;
	}
	
	public String toString() {
		return "Delete Schedule with this code " + secretCode;
	}

}
