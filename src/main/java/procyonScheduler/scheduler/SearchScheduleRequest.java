package procyonScheduler.scheduler;

public class SearchScheduleRequest {
	String schedule;	// id of the schedule to search
	String month;		// name of the month to search in
	String year;		// number of the year to search in
	String weekday;		// name of the day of the week to search in
	String day;			// number of the day of the month to search in
	String time;		// time of the day to search in
	
	public SearchScheduleRequest(String schedule, String month, String year, String weekday, String day, String time) {
		this.schedule = schedule;
		this.month = month;
		this.year = year;
		this.weekday = weekday;
		this.day = day;
		this.time = time;
	}
	
	public String toString() {
		return "Search(" + schedule + ", " + month + ", " + year + ", " + weekday + ", " + day + ", " + time + ")";
	}

}
