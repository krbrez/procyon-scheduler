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

public class CancelMeetingHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null; 
	
	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	
	boolean cancelMeeting(String code, String id) throws Exception {
		if (logger != null) {
			logger.log("in cancelMeeting");
		}
		
		//create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();
		
		//find the meeting
		//Meeting cancelMe = mDAO.getMeeting(code);
		//logger.log("Here" + cancelMe.getId());
		
		//find meeting using ID
		
		//is the entered code the same as this meeting's participant secret code?
		
			//if yes, change this meeting (change to "factory settings")
		
			//if no, GET the schedule this meeting is part of 
		
			//is the code the same as the code of the meeting?
				
				//if yes, change this meeting (change to "factory settings")
		
				//if no, there's an oopsie somewhere
		
		
		
		
	
		//these are the funky thingies that set the boolean Cancelled, says if it actually did the thing	
		//Meeting m = new Meeting()
		boolean cancelled = true;
		//cancelled = cancelled && 
		return cancelled;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create schedule");
	
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
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
					body = event.toJSONString(); // this is only here to make
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
				if (cancelMeeting(req.code)) {
					resp = new CancelMeetingResponse("Successfully created schedule: " + req.code);
				} else {
					resp = new CancelMeetingResponse("Unable to create schedule: " + req.code, 422);
				}
			} catch (Exception e) {
				resp = new CancelMeetingResponse("Unable to create schedule: " + req.code + "(" + e.getMessage() + ")",
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
