package procyonScheduler.scheduler;

import java.util.GregorianCalendar;

public class ToggleFullDayRequest {
	boolean openOrClose;  //True = open slots, False = close slots
	String scheduleID;
	String secretCode;
	String toggleMe;

	public ToggleFullDayRequest(boolean openOrClose, String scheduleID, String secretCode, String toggleMe) {
		this.openOrClose = openOrClose;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.toggleMe = toggleMe;
	}

	public String toString() {
		return "Manage(" + this.toggleMe + this.scheduleID + this.secretCode + "Change to " + this.openOrClose + ")";
	}
}
