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
public class CancelMeetingTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

//	@Test
//	public void testCancelMeetingWorks() throws Exception {
//		CancelMeetingHandler cHandler = new CancelMeetingHandler();
//
//		CancelMeetingRequest cmr = new CancelMeetingRequest("SGoaVDRWpGsJ6CaA", "bY7cRtSEvxIoPPDP");
//		String cancelRequest = new Gson().toJson(cmr);
//		String jsonRequest = new Gson().toJson(new HttpRequest(cancelRequest));
//
//		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
//		OutputStream output = new ByteArrayOutputStream();
//
//		cHandler.handleRequest(input, output, createContext("cancel"));
//
//		HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
//		CancelMeetingResponse resp = new Gson().fromJson(put.body, CancelMeetingResponse.class);
//
//		Assert.assertTrue(resp.response.contains(cmr.id));
//		Assert.assertEquals(resp.httpCode, 200);
//	}

//	@Test
//	public void testcreateSchedule422() throws IOException {
//		CreateScheduleHandler handler = new CreateScheduleHandler();
//
//		CreateScheduleRequest csr = new CreateScheduleRequest("Kyra", "01:00:00.00", "2018-12-30", "02:00:00.00",
//				"2018-12-30", "20");
//		String createRequest = new Gson().toJson(csr);
//		String jsonRequest = new Gson().toJson(new HttpRequest(createRequest));
//
//		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
//		OutputStream output = new ByteArrayOutputStream();
//
//		handler.handleRequest(input, output, createContext("create"));
//
//		HttpResponse post = new Gson().fromJson(output.toString(), HttpResponse.class);
//		CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
//
//		Assert.assertEquals(resp.httpCode, 422);
//
//	}

}
