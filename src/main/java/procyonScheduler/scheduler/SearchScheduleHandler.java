package procyonScheduler.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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

public class SearchScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	
	ArrayList<Meeting> searchSchedule(String schedule, String month, String year, String weekday, String day, String time)
			throws Exception {
		if (logger != null) {
			logger.log("in searchSchedule");
		}
		
		MeetingsDAO mDAO = new MeetingsDAO();
		ArrayList<Meeting> foundMeetings = mDAO.searchMeetings(schedule, month, year, weekday, day, time);
		
		return foundMeetings;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to search schedule");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "PUT,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		SearchScheduleResponse response = null;

		// extract body from incoming HTTP GET request. If any error, then
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
				response = new SearchScheduleResponse("schedule, month, year, weekday, day, time", 200); // OPTIONS
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
			response = new SearchScheduleResponse("Bad Request:" + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			SearchScheduleRequest req = new Gson().fromJson(body, SearchScheduleRequest.class);
			logger.log(req.toString());

			SearchScheduleResponse resp;
			try {
				ArrayList<Meeting> foundMeetings = searchSchedule(req.schedule, req.month, req.year, req.weekday, req.day, req.time);
				if (foundMeetings.size() > 0) {
					foundMeetings.sort(null);
					resp = new SearchScheduleResponse(foundMeetings);
				} else {
					resp = new SearchScheduleResponse("No meetings fitting search criteria. Try broadening your search.", 422);
				}
			} catch (Exception e) {
				resp = new SearchScheduleResponse("Unable to perform search: " + "(" + e.getMessage() + ")", 403);
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
