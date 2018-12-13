package procyonScheduler.scheduler;

import java.util.ArrayList;

import procyonScheduler.model.Meeting;

public class ShowWeekScheduleResponse {
	String id; // The ID of the schedule being returned
	String startDay; // The Monday the week starts on
	int blockSize; // The length of each meeting in minutes
	ArrayList<Meeting> meetings; // The list of meetings from the schedule in
									// the desired week
	int httpCode; // The HTTP Code for the lambda

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The ID of the schedule that isn't being returned
	 * @param startDay
	 *            The Monday the week starts on
	 * @param code
	 *            The HTTP Code for the lambda
	 */
	public ShowWeekScheduleResponse(String id, String startDay, int code) {
		this.id = id;
		this.startDay = startDay;
		this.blockSize = 0;
		this.meetings = new ArrayList<Meeting>();
		this.httpCode = code;
	}

	/**
	 * Constructor when successful
	 * 
	 * @param id
	 *            The ID of the schedule being returned
	 * @param startDay
	 *            The Monday the week starts on
	 * @param blockSize
	 *            The length of each meeting in minute
	 * @param meetings
	 *            The list of meetings from the schedule in the desired week
	 */
	public ShowWeekScheduleResponse(String id, String startDay, int blockSize, ArrayList<Meeting> meetings) {
		this.id = id;
		this.startDay = startDay;
		this.blockSize = blockSize;
		this.meetings = meetings;
		this.httpCode = 200;
	}

	public String toString() {
		return "You requested the week starting " + this.startDay + " of schedule " + this.id + ". Meetings found: "
				+ this.meetings.toString();
	}
}
