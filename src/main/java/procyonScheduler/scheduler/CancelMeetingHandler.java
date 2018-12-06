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

public class CancelMeetingHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null; 
	
	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	
	boolean cancelMeeting(String id, String code) throws Exception {
		if (logger != null) {
			logger.log("in cancelMeeting");
		}
		
		boolean cancelled = false;
		
		//create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();
		
		//find meeting using ID
		Meeting cancelMe = mDAO.getMeeting(id);
		logger.log("This is the meeting with this ID" + cancelMe.getId()); 
		
		//is the entered code the same as this meeting's participant secret code?
		if (code == cancelMe.getParticipantSecretCode()) {
			//if yes, change this meeting (change to "factory settings")
			cancelMe.cancel();
			mDAO.updateMeeting(cancelMe);
			cancelled = true;
		}
		//if not, GET the schedule this meeting is part of (get ID first, then use that to get the rest of the schedule)
		else {
			String meetingsSchedulesId = cancelMe.getSchedule();
			Schedule meetingsSchedule = sDAO.getSchedule(meetingsSchedulesId);
			
			//is the code the same as the code of the meeting?
			if (code == meetingsSchedule.getSecretCode()) {
				//if yes, change this meeting (change to "factory settings")
				cancelMe.cancel();
				mDAO.updateMeeting(cancelMe);
				cancelled = true;
			}
			else {
				//if no, there's an oopsie somewhere
				cancelled = false;
			}
		}
		return cancelled;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to cancel meeting");
	
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,PUT,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");
	
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);
	
		CancelMeetingResponse response = null;
	
		// extract body from incoming HTTP POST request. If any error, then
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
				response = new CancelMeetingResponse("name", 200); // OPTIONS
																	// needs a
																	// 200
																	// response
				responseJson.put("body", new Gson().toJson(response));
				processed = true;
				body = null;
			} else {
				body = (String) event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make
													// testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new CancelMeetingResponse("Bad Request:" + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			CancelMeetingRequest req = new Gson().fromJson(body, CancelMeetingRequest.class);
			logger.log(req.toString());
	
			CancelMeetingResponse resp;
			try {
				if (cancelMeeting(req.id, req.code)) {
					resp = new CancelMeetingResponse("Successfully cancelled meeting: " + req.id);
				} else {
					resp = new CancelMeetingResponse("Unable to cancel meeting: " + req.id, 422);
				}
			} catch (Exception e) {
				resp = new CancelMeetingResponse("Unable to cancel meeting: " + req.id + "(" + e.getMessage() + ")",
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
