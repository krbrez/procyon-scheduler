package procyonScheduler.scheduler;

/** 
 * A class for generic HTTP requests, mostly for testing
 *
 */
public class HttpRequest {
	String body; // The body of the request
	
	/**
	 * Constructor
	 * @param s The body of the request
	 */
	public HttpRequest(String s) {
		body = s;
	}
}
