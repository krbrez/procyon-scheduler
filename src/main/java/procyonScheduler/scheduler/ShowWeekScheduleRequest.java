package procyonScheduler.scheduler;

public class ShowWeekScheduleRequest {
	String id; // The ID of the schedule to be viewed
	String startDay; // The Monday the desired week starts on

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The ID of the schedule to be viewed
	 * @param startDay
	 *            The Monday the desired week starts on
	 */
	public ShowWeekScheduleRequest(String id, String startDay) {
		this.id = id;
		this.startDay = startDay;
	}

	public String toString() {
		return "Show(" + this.id + "," + this.startDay + ")";
	}
}
