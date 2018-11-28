package procyonScheduler.scheduler;

public class CreateConstantRequest {
	String name;
	double value;
	
	public CreateConstantRequest (String n, double v) {
		this.name = n;
		this.value = v;
	}
	
	public String toString() {
		return "Create(" + name + "," + value + ")";
	}
}
