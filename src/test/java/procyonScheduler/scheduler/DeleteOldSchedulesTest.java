package procyonScheduler.scheduler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Schedule;

/**
 * Tests the DeleteOldSchedules class
 */
public class DeleteOldSchedulesTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	/**
	 * Test the delete old schedules use case by creating a year old meeting in
	 * the database
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteOldSchedules() throws Exception {
		// Create super old schedule
		SchedulesDAO sDAO = new SchedulesDAO();
		GregorianCalendar start = new GregorianCalendar(2018, 11, 28, 10, 00);
		GregorianCalendar end = new GregorianCalendar(2019, 0, 2, 20, 00);
		GregorianCalendar created = new GregorianCalendar(2017, 1, 2, 15, 22);
		Schedule newSched = new Schedule("Kyra", start, end, 15, "2222222222222222", created, "2222222222222222");
		sDAO.addSchedule(newSched);

		// Delete super old schedule
		DeleteOldSchedulesHandler delHandler = new DeleteOldSchedulesHandler();

		DeleteOldSchedulesRequest dosr = new DeleteOldSchedulesRequest(365);

		String delRequest = new Gson().toJson(dosr);
		String jsonRequest = new Gson().toJson(new HttpRequest(delRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		delHandler.handleRequest(input, output, createContext("delete old schedules"));

		HttpResponse del = new Gson().fromJson(output.toString(), HttpResponse.class);
		DeleteOldSchedulesResponse resp = new Gson().fromJson(del.body, DeleteOldSchedulesResponse.class);

		Assert.assertTrue(resp.response.contains("1 schedules"));
		Assert.assertEquals(resp.httpCode, 200);

	}

}
