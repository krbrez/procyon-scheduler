package procyonScheduler.scheduler;

public class DeleteOldSchedulesRequest {
	int n;
	
	public DeleteOldSchedulesRequest(int n) {
		this.n = n;
	}
	
	public String toString() {
		return ""
				+ "Delete(" + n + ")";
	}
}
