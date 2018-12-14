package procyonScheduler.scheduler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;



/**
 * Tests CreateMeetingHandler class and CancelMeetingHandler class. These two
 * classes are tested together so that any time a meeting is created, it can be
 * cancelled. However, we made every effort to test the functionality as units,
 * running asserts after each time we use one of the classes.
 */
public class CreateAndCancelMeetingTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Tests that creating and canceling a meeting works when a participant
	 * attempts to do both, i.e. using the participantSecretCode to cancel
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateAndCancelMeetingWorksForParticipant() throws Exception {
		// Create
		CreateMeetingHandler crHandler = new CreateMeetingHandler();

		CreateMeetingRequest crmr = new CreateMeetingRequest("zWw9bmX3N4F8JAMD", "TestingLabel");
		String createRequest = new Gson().toJson(crmr);
		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		crHandler.handleRequest(input, output, createContext("create"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		CreateMeetingResponse resp = new Gson().fromJson(put.body, CreateMeetingResponse.class);

		Assert.assertEquals(crmr.label, resp.label);
		Assert.assertEquals(resp.httpCode, 200);

		// Cancel
		CancelMeetingHandler cHandler = new CancelMeetingHandler();

		CancelMeetingRequest cmr = new CancelMeetingRequest("zWw9bmX3N4F8JAMD", resp.secretCode); // participant
																									// secret
																									// code
		String cancelRequest = new Gson().toJson(cmr);
		String jsonRequest2 = new Gson().toJson(new HttpRequest(cancelRequest));

		InputStream input2 = new ByteArrayInputStream(jsonRequest2.getBytes());
		OutputStream output2 = new ByteArrayOutputStream();

		cHandler.handleRequest(input2, output2, createContext("cancel"));

		HttpResponse put2 = new Gson().fromJson(output2.toString(), HttpResponse.class);
		CancelMeetingResponse resp2 = new Gson().fromJson(put2.body, CancelMeetingResponse.class);

		System.out.println(resp2);
		Assert.assertTrue(resp2.response.contains(cmr.id));
		Assert.assertEquals(resp2.httpCode, 200);
	}

	/**
	 * Tests that if a meeting is created by a participant, the organizer can
	 * use their secret code to cancel it
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCancelMeetingWorksForOrganizer() throws Exception {
		// Create
		CreateMeetingHandler crHandler = new CreateMeetingHandler();

		CreateMeetingRequest crmr = new CreateMeetingRequest("zWw9bmX3N4F8JAMD", "TestingLabel");
		String createRequest = new Gson().toJson(crmr);
		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		crHandler.handleRequest(input, output, createContext("create"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		CreateMeetingResponse resp = new Gson().fromJson(put.body, CreateMeetingResponse.class);

		// Cancel
		CancelMeetingHandler cHandler = new CancelMeetingHandler();

		CancelMeetingRequest cmr = new CancelMeetingRequest("zWw9bmX3N4F8JAMD", "8lPq7LYmpQbpa1E4"); // orgzanizer
																										// secret
																										// code
		String cancelRequest = new Gson().toJson(cmr);
		String jsonRequest2 = new Gson().toJson(new HttpRequest(cancelRequest));

		InputStream input2 = new ByteArrayInputStream(jsonRequest2.getBytes());
		OutputStream output2 = new ByteArrayOutputStream();

		cHandler.handleRequest(input2, output2, createContext("cancel"));

		HttpResponse put2 = new Gson().fromJson(output2.toString(), HttpResponse.class);
		CancelMeetingResponse resp2 = new Gson().fromJson(put2.body, CancelMeetingResponse.class);

		System.out.println(resp2);
		Assert.assertTrue(resp2.response.contains(cmr.id));
		Assert.assertEquals(resp2.httpCode, 200);
	}

	/**
	 * Test that createMeeting responds with a 4xx when given an invalid input
	 * 
	 * @throws IOException
	 */
	@Test
	public void testcreateMeeting422() throws IOException {
		CreateMeetingHandler handler = new CreateMeetingHandler();

		CreateMeetingRequest cmr = new CreateMeetingRequest("xx", "xx");
		String createRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("create"));

		HttpResponse post = new Gson().fromJson(output.toString(), HttpResponse.class);
		CreateMeetingResponse resp = new Gson().fromJson(post.body, CreateMeetingResponse.class);

		Assert.assertEquals(resp.httpCode, 403);

	}

	/**
	 * Test that cancel meeting responds with a 4xx when given invalid input
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCancelMeeting403() throws IOException {
		CancelMeetingHandler handler = new CancelMeetingHandler();

		CancelMeetingRequest cmr = new CancelMeetingRequest("xx", "xx");
		String cancelRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new HttpRequest(cancelRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("cancel"));

		HttpResponse post = new Gson().fromJson(output.toString(), HttpResponse.class);
		CancelMeetingResponse resp = new Gson().fromJson(post.body, CancelMeetingResponse.class);

		Assert.assertEquals(resp.httpCode, 403);

	}

}
