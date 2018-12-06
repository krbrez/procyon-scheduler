package procyonScheduler.scheduler;

public class ManageTimeSlotRequest {
	String meetingID;
	String secretCode;

	public ManageTimeSlotRequest(String meetingID, String secretCode) {
		this.meetingID = meetingID;
		this.secretCode = secretCode;
	}

	public String toString() {
		return "Manage(" + this.meetingID + this.secretCode + ")";
	}
}
