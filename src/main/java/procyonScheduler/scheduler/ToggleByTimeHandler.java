package procyonScheduler.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Schedule;

public class ToggleByTimeHandler implements RequestStreamHandler {
public LambdaLogger logger = null;
	
	//toggle all the slots in a given day to be either all open
	//or all closed, based on the input boolean 
	
	boolean toggleTime(boolean openOrClose, String scheduleID, String secretCode, String toggleMe) throws Exception {
		if (logger != null) {
			logger.log("in toggleDay");
		}
		boolean toggled = false;
		
		MeetingsDAO mDAO = new MeetingsDAO();
		SchedulesDAO sDAO = new SchedulesDAO();
		
		//somewhere i must parse the input time into something legible, but where?
		
		//get the schedule with the given id
		Schedule editThisSchedule = sDAO.getSchedule(scheduleID);
		//check that the code is the right one for the schedule
		if (secretCode.equals(editThisSchedule.getSecretCode())) {
			if(openOrClose) {	//want to OPEN meeting availability
				
			}
			else if (!openOrClose) { 	//want to CLOSE meeting availability
				
			}
			//go through the schedule and find all meetings with that date
				//if boolean is true, we want to change availabilities to true	
					//do the changes, and set toggled to true
				//if boolean is false, we want to change availability to closed
					//do the changes, and set toggled to true
		}
		else 
		{
			//there is a mistake!
			toggled = false;
		}
		return toggled;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to manage the given time");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "PUT,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ToggleByTimeResponse response = null;

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
				response = new ToggleByTimeResponse("scheduleID, secretCode", 200); // OPTIONS

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
			response = new ToggleByTimeResponse("Bad Request: " + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ToggleByTimeRequest req = new Gson().fromJson(body, ToggleByTimeRequest.class);
			logger.log(req.toString());

			ToggleByTimeResponse resp;
			try {
				if (toggleTime(req.openOrClose, req.scheduleID, req.secretCode, req.toggleMe)) {
					resp = new ToggleByTimeResponse("Successfully managed time: " + req.toggleMe);
				} else {
					resp = new ToggleByTimeResponse("Unable to manage time: " + req.toggleMe, 422);
				}
			} catch (Exception e) {
				resp = new ToggleByTimeResponse("Unable to manage time: " + req.toggleMe + "(" + e.getMessage() + ")",
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
