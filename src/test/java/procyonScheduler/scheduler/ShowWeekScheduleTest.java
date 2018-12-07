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

import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Schedule;
import procyonScheduler.scheduler.CreateScheduleHandler;
import procyonScheduler.scheduler.CreateScheduleRequest;
import procyonScheduler.scheduler.CreateScheduleResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ShowWeekScheduleTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	// Play with schedule '0a891YnhrRILP4N4'

	@Test
	public void testShowWeekSchedule() throws Exception {
		// Create
		ShowWeekScheduleHandler swHandler = new ShowWeekScheduleHandler();

		ShowWeekScheduleRequest swsr = new ShowWeekScheduleRequest("0a891YnhrRILP4N4", "2018-12-03");
		String showRequest = new Gson().toJson(swsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(showRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		swHandler.handleRequest(input, output, createContext("show"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		ShowWeekScheduleResponse resp = new Gson().fromJson(put.body, ShowWeekScheduleResponse.class);

		Assert.assertEquals(resp.meetings.get(1).getSchedule(), "0a891YnhrRILP4N4");
		Assert.assertEquals(resp.httpCode, 200);

	}

	@Test
	public void testShowWeekSchedule403() throws Exception {
		// Create
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

		ShowWeekScheduleRequest swsr = new ShowWeekScheduleRequest("0a891YnhrRILP4N4", "2020-10-22");
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
