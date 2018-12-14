package procyonScheduler.scheduler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Schedule;

/**
 * Tests the ReportActivityHandler class
 */
public class ReportActivityTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Tests that this class does in fact return a report of the most recent
	 * classes
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReportActivity() throws Exception {
		// This creates a new schedule to check for in the returns and then
		// deletes it after so as
		// to be able to know what the most recent schedule is and not
		// have an issue or clutter the database

		// Make a schedule to extend

		CreateScheduleHandler cHandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("Kyrax", "01:00:00.00", "2018-11-30", "02:00:00.00",
				"2018-11-30", "20");
		String createRequest = new Gson().toJson(csr);
		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		cHandler.handleRequest(input, output, createContext("create"));

		HttpResponse post = new Gson().fromJson(output.toString(), HttpResponse.class);
		CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);

		// Report activity

		ReportActivityHandler reportHandler = new ReportActivityHandler();

		ReportActivityRequest rar = new ReportActivityRequest(1);
		String reportRequest = new Gson().toJson(rar);
		String jsonRequest2 = new Gson().toJson(new HttpRequest(reportRequest));

		InputStream input2 = new ByteArrayInputStream(jsonRequest2.getBytes());
		OutputStream output2 = new ByteArrayOutputStream();

		reportHandler.handleRequest(input2, output2, createContext("report activity"));

		HttpResponse put = new Gson().fromJson(output2.toString(), HttpResponse.class);
		ReportActivityResponse resp2 = new Gson().fromJson(put.body, ReportActivityResponse.class);

		Assert.assertEquals(resp2.httpCode, 200);
		boolean returnsCreated = false;
		for (Schedule schedule : resp2.schedules) {
			if (schedule.getId().equals(resp.id)) {
				returnsCreated = true;
			}
		}
		Assert.assertTrue(returnsCreated);

		// Delete the schedule
		DeleteScheduleHandler dHandler = new DeleteScheduleHandler();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(resp.secretCode);
		String deleteRequest = new Gson().toJson(dsr);
		String dJsonRequest = new Gson().toJson(new HttpRequest(deleteRequest));

		InputStream input1 = new ByteArrayInputStream(dJsonRequest.getBytes());
		OutputStream output1 = new ByteArrayOutputStream();

		dHandler.handleRequest(input1, output1, createContext("delete"));

		HttpResponse delete = new Gson().fromJson(output2.toString(), HttpResponse.class);
		DeleteScheduleResponse resp1 = new Gson().fromJson(delete.body, DeleteScheduleResponse.class);
	}

}
