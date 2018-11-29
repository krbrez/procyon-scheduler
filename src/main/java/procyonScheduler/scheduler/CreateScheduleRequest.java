package procyonScheduler.scheduler;

public class CreateScheduleRequest {
	String name;
	String startTime;
	String startDate;
	String endTime;
	String endDate;
	int blockSize;

	public CreateScheduleRequest(String name, String startT, String startD, String endT, String endD, String blockSize) {
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
