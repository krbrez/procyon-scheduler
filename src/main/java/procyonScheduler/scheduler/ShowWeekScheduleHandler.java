package procyonScheduler.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.model.Meeting;

public class ShowWeekScheduleHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	List<Meeting> showWeek(String id, String startDay) throws Exception {
		if (logger != null) {
			logger.log("in showWeek");
		}
		
		List<Meeting> weekMeetings = new ArrayList<>();
		MeetingsDAO mDAO = new MeetingsDAO();
		
		int stY = 0, stM = 0, stDy = 0;
		
		if (startDay.length() == 10) {
			String[] stDt = startDay.split("-");
			stY = Integer.parseInt(stDt[0]);
			stM = Integer.parseInt(stDt[1]) - 1;	// GregorianCalendar months start at 0
			stDy = Integer.parseInt(stDt[2]);
		}
		
		GregorianCalendar startDayGC = new GregorianCalendar(stY, stM, stDy);
		
		weekMeetings = mDAO.getWeekFromSchedule(id, startDayGC);
		return weekMeetings;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create schedule");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateScheduleResponse response = null;

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
				response = new ShowWeekScheduleResponse("name", 200); // OPTIONS
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
			response = new ShowWeekScheduleResponse("Bad Request:" + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ShowWeekScheduleRequest req = new Gson().fromJson(body, ShowWeekScheduleRequest.class);
			logger.log(req.toString());

			ShowWeekScheduleResponse resp;
			try {
				List<Meeting> weekMeetings = showWeek(req.id, req.startDay);
				if (weekMeetings.size() > 0) {
					resp = new CreateScheduleResponse("Successfully fetched week: " + weekMeetings.toString());
				} else {
					resp = new CreateScheduleResponse("Unable to fetch week: " + req.startDay + " of Schedule: " + req.id, 422);
				}
			} catch (Exception e) {
				resp = new CreateScheduleResponse("Unable to fetch week: " + req.startDay + " of Schedule: " + req.id + "(" + e.getMessage() + ")",
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
