package procyonScheduler.scheduler;

public class CreateScheduleRequest {
	String name;
	double value;
	
	public CreateScheduleRequest (String n, double v) {
		this.name = n;
		this.value = v;
	}
	
	public String toString() {
		return "Create(" + name + "," + value + ")";
	}
}
