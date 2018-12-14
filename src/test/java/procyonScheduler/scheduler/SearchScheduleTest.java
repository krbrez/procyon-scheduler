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
 * Tests the SearchScheduleHandler class
 */
public class SearchScheduleTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Tests that all open meetings are returned properly if no filters
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNoFilters() throws Exception {
		SearchScheduleHandler searchHandler = new SearchScheduleHandler();

		SearchScheduleRequest swsr = new SearchScheduleRequest("onlt0O2wk7YFyUnG", "", "", "", "", "");
		String showRequest = new Gson().toJson(swsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(showRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		searchHandler.handleRequest(input, output, createContext("search"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		SearchScheduleResponse resp = new Gson().fromJson(put.body, SearchScheduleResponse.class);

		Assert.assertEquals(resp.meetings.size(), 153); //make sure it returns all schedules
		Assert.assertEquals(resp.httpCode, 200);

	}

	/**
	 * Tests that all open meetings are returned properly if no filters
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAllFilters() throws Exception {
		SearchScheduleHandler searchHandler = new SearchScheduleHandler();

		SearchScheduleRequest swsr = new SearchScheduleRequest("onlt0O2wk7YFyUnG", "December", "2018", "Monday", "17", "05:00");
		String showRequest = new Gson().toJson(swsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(showRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		searchHandler.handleRequest(input, output, createContext("search"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		SearchScheduleResponse resp = new Gson().fromJson(put.body, SearchScheduleResponse.class);

		Assert.assertEquals(resp.meetings.size(), 1); //make sure it returns all schedules
		Assert.assertEquals(resp.httpCode, 200);

	}

}
