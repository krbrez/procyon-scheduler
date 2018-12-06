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
public class CreateAndDeleteScheduleHandlersTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCreateAndDeleteScheduleWorks() throws Exception {
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

		Assert.assertTrue(resp.response.contains(csr.name));
		Assert.assertEquals(resp.httpCode, 200);

		DeleteScheduleHandler dHandler = new DeleteScheduleHandler();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(resp.secretCode);
		String deleteRequest = new Gson().toJson(dsr);
		String dJsonRequest = new Gson().toJson(new HttpRequest(deleteRequest));

		InputStream input2 = new ByteArrayInputStream(dJsonRequest.getBytes());
		OutputStream output2 = new ByteArrayOutputStream();

		dHandler.handleRequest(input2, output2, createContext("delete"));

		HttpResponse delete = new Gson().fromJson(output2.toString(), HttpResponse.class);
		DeleteScheduleResponse resp2 = new Gson().fromJson(delete.body, DeleteScheduleResponse.class);

		Assert.assertTrue(resp2.response.contains(resp.secretCode));
		Assert.assertEquals(resp2.httpCode, 200);
	}

	@Test
	public void testcreateSchedule422() throws IOException {
		CreateScheduleHandler handler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("Kyrax", "01:00:00.00", "2018-12-30", "02:00:00.00",
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

	@Test
	public void testdeleteSchedule403() throws IOException {
		DeleteScheduleHandler handler = new DeleteScheduleHandler();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest("x");
		String deleteRequest = new Gson().toJson(dsr);
		String jsonRequest = new Gson().toJson(new HttpRequest(deleteRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("delete"));

		HttpResponse delete = new Gson().fromJson(output.toString(), HttpResponse.class);
		DeleteScheduleResponse resp = new Gson().fromJson(delete.body, DeleteScheduleResponse.class);

		Assert.assertEquals(resp.httpCode, 403);
	}

}
