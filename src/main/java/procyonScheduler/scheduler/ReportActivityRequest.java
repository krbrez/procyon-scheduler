package procyonScheduler.scheduler;

public class ReportActivityRequest {
	int n;
	
	public ReportActivityRequest(int n) {
		this.n = n;
	}
	
	public String toString() {
		return ""
				+ "Report(" + n + ")";
	}
}
