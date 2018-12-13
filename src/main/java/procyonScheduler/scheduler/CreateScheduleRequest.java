package procyonScheduler.scheduler;

/**
 * The request class for CreateScheduleHandler
 *
 */
public class CreateScheduleRequest {
	String name; // The name of the schedule
	String startTime; // The time each day on the schedule starts
	String startDate; // The start date of the schedule
	String endTime; // The time each day on the schedule ends
	String endDate; // The end date of the schedule
	int blockSize; // The amount of time each meeting on the schedule takes up

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the schedule
	 * @param startT
	 *            The time each day on the schedule starts
	 * @param startD
	 *            The start date of the schedule
	 * @param endT
	 *            The time each day on the schedule ends
	 * @param endD
	 *            The end date of the schedule
	 * @param blockSize
	 *            The amount of time each meeting on the schedule
	 */
	public CreateScheduleRequest(String name, String startT, String startD, String endT, String endD,
			String blockSize) {
		this.name = name;
		this.startTime = startT;
		this.startDate = startD;
		this.endTime = endT;
		this.endDate = endD;
		this.blockSize = Integer.parseInt(blockSize);
	}

	public String toString() {
		return "Create(" + name + "," + startTime + "," + startDate + "," + endTime + "," + endDate + "," + blockSize
				+ ")";
	}
}
