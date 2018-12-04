package procyonScheduler.scheduler;

public class CancelMeetingRequest {
	String code;
	
	public CancelMeetingRequest(String code) {
		this.code = code;
	}
	
	public String toString() {
		return "Cancel meeting with this code " + code;
	}

}
