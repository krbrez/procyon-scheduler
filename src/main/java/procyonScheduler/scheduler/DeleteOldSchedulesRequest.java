package procyonScheduler.scheduler;

/**
 * The request class for DeleteOldSchedulesHandler
 *
 */
public class DeleteOldSchedulesRequest {
	int n; // the number of days old the schedules to be deleted should be older
			// than

	/**
	 * Constructor
	 * 
	 * @param n
	 *            number of days for how old the schedules have to be to be
	 *            deleted
	 */
	public DeleteOldSchedulesRequest(int n) {
		this.n = n;
	}

	public String toString() {
		return "" + "Delete(" + n + ")";
	}
}
