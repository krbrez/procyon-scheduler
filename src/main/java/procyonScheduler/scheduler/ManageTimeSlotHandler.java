package procyonScheduler.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;

public class ManageTimeSlotHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	
	// toggle whether a time slot is available for a meeting to be scheduled or not
	boolean manageTimeSlot(String meetingID, String secretCode) throws Exception {
		if (logger != null) {
			logger.log("in manageTimeSlot");
		}
		boolean toggled = false;
		
		MeetingsDAO mDAO = new MeetingsDAO();
		SchedulesDAO sDAO = new SchedulesDAO();
		
		Meeting m = mDAO.getMeeting(meetingID);
		Schedule s = sDAO.getSchedule(m.getSchedule());
		// check the secret code
		if(secretCode.equals(s.getSecretCode())) {
			m.setAvailable(!m.getAvailable());
			mDAO.updateMeeting(m);
			toggled = true;
		}
		
		return toggled;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to manage time slot");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "PUT,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ManageTimeSlotResponse response = null;

		// extract body from incoming HTTP PUT request. If any error, then
		// return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());

			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS")) {
				logger.log("Options request");
				response = new ManageTimeSlotResponse("meetingID, secretCode", 200); // OPTIONS

				// needs a
				// 200
				// response
				responseJson.put("body", new Gson().toJson(response));
				processed = true;
				body = null;
			} else {
				body = (String) event.get("body");
				if (body == null) {
					body = event.toJSONString(); // this is only here to make
													// testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new ManageTimeSlotResponse("Bad Request: " + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ManageTimeSlotRequest req = new Gson().fromJson(body, ManageTimeSlotRequest.class);
			logger.log(req.toString());

			ManageTimeSlotResponse resp;
			try {
				if (manageTimeSlot(req.meetingID, req.secretCode)) {
					resp = new ManageTimeSlotResponse("Successfully managed time slot: " + req.meetingID);
				} else {
					resp = new ManageTimeSlotResponse("Unable to manage time slot: " + req.meetingID, 422);
				}
			} catch (Exception e) {
				resp = new ManageTimeSlotResponse("Unable to manage time slot: " + req.meetingID + "(" + e.getMessage() + ")",
						403);
			}

			// compute proper response
			responseJson.put("body", new Gson().toJson(resp));
		}

		logger.log("end result:" + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();

	}

}
