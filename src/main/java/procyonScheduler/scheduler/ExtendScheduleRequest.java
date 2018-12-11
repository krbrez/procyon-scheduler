package procyonScheduler.scheduler;

public class ExtendScheduleRequest {
	String extDate;
	String secretCode;
	
	public ExtendScheduleRequest(String extDate, String secretCode) {
		this.extDate = extDate;
		this.secretCode = secretCode;
	}
	
	public String toString() {
		return "Extend(" + extDate + ", " + secretCode + ")";
	}
}
