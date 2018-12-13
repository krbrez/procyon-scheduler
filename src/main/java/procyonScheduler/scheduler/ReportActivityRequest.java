package procyonScheduler.scheduler;

/**
 * The request class for ReportActivityHandler
 *
 */
public class ReportActivityRequest {
	int n; // The number of hours to report on

	/**
	 * Constructor
	 * 
	 * @param n
	 *            The number of hours to report on
	 */
	public ReportActivityRequest(int n) {
		this.n = n;
	}

	public String toString() {
		return "" + "Report(" + n + ")";
	}
}
