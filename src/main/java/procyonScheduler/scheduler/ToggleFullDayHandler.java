package procyonScheduler.scheduler;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import model.Tile;
import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;

public class ToggleFullDayHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	
	//toggle all the slots in a given day to be either all open
	//or all closed, based on the input boolean 
	
	boolean toggleDay(boolean openOrClose, String scheduleID, String secretCode, GregorianCalendar toggleMe) throws Exception {
		if (logger != null) {
			logger.log("in toggleDay");
		}
		boolean toggled = false;
		
		MeetingsDAO mDAO = new MeetingsDAO();
		SchedulesDAO sDAO = new SchedulesDAO();
		
		//get the schedule with the given id
		Schedule editThisSchedule = sDAO.getSchedule(scheduleID);
		//check that the code is the right one for the schedule
		if (secretCode.equals(editThisSchedule.getSecretCode())) {
			//if this is the right schedule (which it is, to be at this point)
			//get all the meetings from this schedule
			ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO.getAllMeetingsFromSchedule(scheduleID);
			//for each meeting
			for (Iterator<Meeting> it = meetings.getNext(); it.hasNext(); ) { //what's something i can do to an arraylist of meetings?
				Meeting m = it.next();
				if (m.getDateTime().equals(toggleMe)) 
				{
					//change availability 
					toggled = true;
				}
			}
			
			
			//is the date correct?
				//if yes, change availability
				//if not, move on to next meeting
			
			
			
			
			//if(openOrClose) {	//want to OPEN meeting availability if openOrClose is true
				//List<Meeting> meetings = mDAO.getAllMeetingsFromSchedule(scheduleID)
//				for (Iterator<Tile> it = model.getBoard().iterator(); it.hasNext(); ) {
//					Tile t = it.next();
//					if (model.validDown(selectedTile, t) == false)
//					{
//						canMove = false;
//						//break;
//					}
//				}
			//else if (!openOrClose) { 	//want to CLOSE meeting availability if openOrClose is false
				
			
			//go through the schedule and find all meetings with that date
				//if boolean is true, we want to change availabilities to true	
					//do the changes, and set toggled to true
				//if boolean is false, we want to change availability to closed
					//do the changes, and set toggled to true
		}
		else {
			//there is a mistake!
			toggled = false;
		}
		return toggled;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to manage the given date");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "PUT,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ToggleFullDayResponse response = null;

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
				response = new ToggleFullDayResponse("scheduleID, secretCode", 200); // OPTIONS

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
			response = new ToggleFullDayResponse("Bad Request: " + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ToggleFullDayRequest req = new Gson().fromJson(body, ToggleFullDayRequest.class);
			logger.log(req.toString());

			ToggleFullDayResponse resp;
			try {
				if (toggleDay(req.openOrClose, req.scheduleID, req.secretCode, req.toggleMe)) {
					resp = new ToggleFullDayResponse("Successfully managed date: " + req.toggleMe);
				} else {
					resp = new ToggleFullDayResponse("Unable to manage date: " + req.toggleMe, 422);
				}
			} catch (Exception e) {
				resp = new ToggleFullDayResponse("Unable to manage date: " + req.toggleMe + "(" + e.getMessage() + ")",
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
