package procyonScheduler.scheduler;

public class CreateMeetingRequest {
	String id;
	String label;
	
	public CreateMeetingRequest(String id, String label) {
		this.id = id;
		this.label = label;
	}
	
	public String toString() {
		return "Create(" + this.id + "," + this.label + "," + ")";
	}
}
