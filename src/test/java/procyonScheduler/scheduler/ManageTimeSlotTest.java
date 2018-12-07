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
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ManageTimeSlotTest {
	
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	
	//Play with meeting 'hiQ3vL2P8cxn0JnQ'
	//Available (open) is 1, unavailable (closed) = 0
	
		@Test
		public void testManageTimeSlot() throws Exception {  

			//Close time slot
			ManageTimeSlotHandler closeHandler = new ManageTimeSlotHandler();

			ManageTimeSlotRequest ctsr = new ManageTimeSlotRequest("hiQ3vL2P8cxn0JnQ", "cKN8l0Vs7IYnS2BU"); //ctsr => close time slot request
			String closeRequest = new Gson().toJson(ctsr);
			String jsonRequest = new Gson().toJson(new HttpRequest(closeRequest));

			InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
			OutputStream output = new ByteArrayOutputStream();

			closeHandler.handleRequest(input, output, createContext("close slot"));

			HttpResponse put = new Gson().fromJson(output.toString(), HttpResponse.class);
			ManageTimeSlotResponse resp = new Gson().fromJson(put.body, ManageTimeSlotResponse.class);

			Assert.assertTrue(resp.response.contains(ctsr.meetingID));
			Assert.assertEquals(resp.httpCode, 200);
			
		}

}
