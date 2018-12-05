package procyonScheduler.scheduler;

public class CancelMeetingRequest {
	String id;
	String code;
	
	public CancelMeetingRequest(String id, String code) {
		this.id = id;
		this.code = code;
	}
	
	public String toString() {
		return "Cancel meeting with this id " + id; 
	}

}
