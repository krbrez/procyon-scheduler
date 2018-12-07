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
public class CreateAndCancelMeetingTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	//Play with meeting 'hiQ3vL2P8cxn0JnQ'
	
	@Test
	public void testCreateAndCancelMeetingWorksForParticipant() throws Exception {
		//Create
		CreateMeetingHandler crHandler = new CreateMeetingHandler();

		CreateMeetingRequest crmr = new CreateMeetingRequest("hiQ3vL2P8cxn0JnQ", "TestingLabel");
		String createRequest = new Gson().toJson(crmr);
		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		crHandler.handleRequest(input, output, createContext("create"));

		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
		CreateMeetingResponse resp = new Gson().fromJson(put.body, CreateMeetingResponse.class);

		Assert.assertEquals(crmr.label, resp.label);
		Assert.assertEquals(resp.httpCode, 200);
		
		//Cancel
		CancelMeetingHandler cHandler = new CancelMeetingHandler();

		CancelMeetingRequest cmr = new CancelMeetingRequest("hiQ3vL2P8cxn0JnQ", resp.secretCode);
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

	@Test
	public void testcreateSchedule422() throws IOException {
		CreateScheduleHandler handler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("Kyra", "01:00:00.00", "2018-12-30", "02:00:00.00",
				"2018-12-30", "20");
		String createRequest = new Gson().toJson(csr);
		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("create"));

		HttpResponse post = new Gson().fromJson(output.toString(), HttpResponse.class);
		CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);

		Assert.assertEquals(resp.httpCode, 422);

	}

}
