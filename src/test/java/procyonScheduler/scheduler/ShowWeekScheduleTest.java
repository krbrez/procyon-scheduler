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
 * Tests the ShowWeekScheduleHandler class
 */
public class ShowWeekScheduleTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Tests that a week is returned from the proper schedule properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShowWeekSchedule() throws Exception {
		ShowWeekScheduleHandler swHandler = new ShowWeekScheduleHandler();

		ShowWeekScheduleRequest swsr = new ShowWeekScheduleRequest("onlt0O2wk7YFyUnG", "2018-12-17");
		String showRequest = new Gson().toJson(swsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(showRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		swHandler.handleRequest(input, output, createContext("show"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		ShowWeekScheduleResponse resp = new Gson().fromJson(put.body, ShowWeekScheduleResponse.class);

		Assert.assertEquals(resp.meetings.get(1).getSchedule(), "onlt0O2wk7YFyUnG");
		Assert.assertEquals(resp.httpCode, 200);

	}

	/**
	 * Tests that the handler is given improper input, it returns a 4xx
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShowWeekSchedule403() throws Exception {
		ShowWeekScheduleHandler swHandler = new ShowWeekScheduleHandler();

		ShowWeekScheduleRequest swsr = new ShowWeekScheduleRequest("xx", "xx");
		String showRequest = new Gson().toJson(swsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(showRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		swHandler.handleRequest(input, output, createContext("show"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		ShowWeekScheduleResponse resp = new Gson().fromJson(put.body, ShowWeekScheduleResponse.class);

		Assert.assertEquals(resp.httpCode, 403);
	}

	@Test
	public void testShowWeekSchedule422() throws Exception {
		// Create
		ShowWeekScheduleHandler swHandler = new ShowWeekScheduleHandler();

		ShowWeekScheduleRequest swsr = new ShowWeekScheduleRequest("onlt0O2wk7YFyUnG", "2020-10-22");
		String showRequest = new Gson().toJson(swsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(showRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		swHandler.handleRequest(input, output, createContext("show"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		ShowWeekScheduleResponse resp = new Gson().fromJson(put.body, ShowWeekScheduleResponse.class);

		Assert.assertEquals(resp.httpCode, 422);
	}

}
