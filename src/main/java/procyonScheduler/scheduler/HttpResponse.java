package procyonScheduler.scheduler;

/**
 * A class for generic HTTP responses, mostly for testing
 *
 */
public class HttpResponse {
	String body; // The body of the response

	/**
	 * Constructor
	 * 
	 * @param b
	 *            The body of the response
	 */
	public HttpResponse(String b) {
		body = b;
	}
}
