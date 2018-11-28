package procyonScheduler.scheduler;

public class CreateScheduleRequest {
	String name;
	String startT;
	String startD;
	String endT;
	String endD;
	int blockSize;

	public CreateScheduleRequest(String name, String startT, String startD, String endT, String endD, int blockSize) {
		this.name = name;
		this.startT = startT;
		this.startD = startD;
		this.endT = endT;
		this.endD = endD;
		this.blockSize = blockSize;
	}

	public String toString() {
		return "Create(" + name + "," + startT + "," + startD + "," + endT + "," + endD + "," + blockSize + ")";
	}
}
