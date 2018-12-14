package procyonScheduler.scheduler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

/**
 * Tests for the ManageTimeSlotHandler class
 */
public class ManageTimeSlotTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Test that the handler successfully closes and opens time slots
	 * 
	 * @throws Exception
	 */
	@Test
	public void testManageTimeSlot() throws Exception {

		// Close time slot
		ManageTimeSlotHandler closeHandler = new ManageTimeSlotHandler();

		ManageTimeSlotRequest ctsr = new ManageTimeSlotRequest("zWw9bmX3N4F8JAMD", "8lPq7LYmpQbpa1E4"); // ctsr
																										// =>
																										// close
																										// time
																										// slot
																										// request
		String closeRequest = new Gson().toJson(ctsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(closeRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		closeHandler.handleRequest(input, output, createContext("close slot"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		ManageTimeSlotResponse resp = new Gson().fromJson(put.body, ManageTimeSlotResponse.class);

		Assert.assertTrue(resp.response.contains(ctsr.meetingID));
		Assert.assertEquals(resp.httpCode, 200);

		// Open time slot
		ManageTimeSlotHandler openHandler = new ManageTimeSlotHandler();

		ManageTimeSlotRequest otsr = new ManageTimeSlotRequest("zWw9bmX3N4F8JAMD", "8lPq7LYmpQbpa1E4"); // ctsr
																										// =>
																										// open
																										// time
																										// slot
																										// request
		String openRequest = new Gson().toJson(otsr);
		String jsonRequest2 = new Gson().toJson(new HttpRequest(openRequest));

		InputStream input1 = new ByteArrayInputStream(jsonRequest2.getBytes());
		OutputStream output1 = new ByteArrayOutputStream();

		openHandler.handleRequest(input1, output1, createContext("open slot"));

		HttpResponse put1 = new Gson().fromJson(output.toString(), HttpResponse.class);
		ManageTimeSlotResponse resp1 = new Gson().fromJson(put1.body, ManageTimeSlotResponse.class);

		Assert.assertTrue(resp1.response.contains(otsr.meetingID));
		Assert.assertEquals(resp1.httpCode, 200);

	}

}
