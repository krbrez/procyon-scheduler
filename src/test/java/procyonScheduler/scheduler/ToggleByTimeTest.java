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
 * Tests for the ToggleByTimeHandler class
 */
public class ToggleByTimeTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Test that the handler successfully closes and opens the given time slots
	 * 
	 * @throws Exception
	 */
	@Test
	public void testManageTimeSlot() throws Exception {

		// Close time slot
		ToggleByTimeHandler closeHandler = new ToggleByTimeHandler();

		ToggleByTimeRequest ctsr = new ToggleByTimeRequest(false, "onlt0O2wk7YFyUnG", "8lPq7LYmpQbpa1E4", "05:00");
		String closeRequest = new Gson().toJson(ctsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(closeRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		closeHandler.handleRequest(input, output, createContext("close slot"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		ToggleByTimeResponse resp = new Gson().fromJson(put.body, ToggleByTimeResponse.class);

		Assert.assertTrue(resp.response.contains("Success"));
		Assert.assertEquals(resp.httpCode, 200);

		// Open time slot
		ToggleByTimeHandler openHandler = new ToggleByTimeHandler();

		ToggleByTimeRequest otsr = new ToggleByTimeRequest(true, "onlt0O2wk7YFyUnG", "8lPq7LYmpQbpa1E4", "05:00"); // ctsr
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
		ToggleByTimeResponse resp1 = new Gson().fromJson(put1.body, ToggleByTimeResponse.class);

		Assert.assertTrue(resp1.response.contains("Success"));
		Assert.assertEquals(resp1.httpCode, 200);

	}

}
