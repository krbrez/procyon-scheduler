package procyonScheduler.scheduler;

public class ShowWeekScheduleRequest {
	String id;
	String startDay;
	
	public ShowWeekScheduleRequest(String id, String startDay) {
		this.id = id;
		this.startDay = startDay;
	}
	
	public String toString() {
		return "Show(" + this.id + "," + this.startDay + ")";
	}
}
