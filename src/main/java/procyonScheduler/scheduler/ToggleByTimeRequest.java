package procyonScheduler.scheduler;

public class ToggleByTimeRequest {
	boolean openOrClose;  //True = open slots, False = close slots
	String scheduleID;
	String secretCode;
	String toggleMe;

	public ToggleByTimeRequest(boolean openOrClose, String scheduleID, String secretCode, String toggleMe) {
		this.openOrClose = openOrClose;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.toggleMe = toggleMe;
	}

	public String toString() {
		//might want to change this
		return "Manage(" + this.toggleMe + this.scheduleID + this.secretCode + "Change to " + this.openOrClose + ")";
	}
}
