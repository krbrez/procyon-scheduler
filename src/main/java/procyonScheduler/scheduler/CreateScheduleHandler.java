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
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;

import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.scheduler.CreateScheduleRequest;
import procyonScheduler.scheduler.CreateScheduleResponse;
import procyonScheduler.model.Schedule;
import procyonScheduler.model.Meeting;

public class CreateScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	boolean createSchedule(String name, String startT, String startD, String endT, String endD, int blockSize)
			throws Exception {
		if (logger != null) {
			logger.log("in createSchedule");
		}
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();

		// parse date time strings
		// time: HH:MM:SS.LL
		// date: YYYY-MM-DD
		int stH = 0, stY = 0, stM = 0, stDy = 0, endH = 0, endY = 0, endM = 0, endDy = 0;
		if (startT.length() == 11) {
			String stHstr = startT.split(":")[0];
			stH = Integer.parseInt(stHstr);
		}
		if (endT.length() == 11) {
			String endHstr = endT.split(":")[0];
			endH = Integer.parseInt(endHstr);
		}
		if (startD.length() == 10) {
			String[] stDt = startD.split("-");
			stY = Integer.parseInt(stDt[0]);
			stM = Integer.parseInt(stDt[1]);
			stDy = Integer.parseInt(stDt[2]);
		}
		if (endD.length() == 10) {
			String[] endDt = endD.split("-");
			endY = Integer.parseInt(endDt[0]);
			endM = Integer.parseInt(endDt[1]);
			endDy = Integer.parseInt(endDt[2]);
		}

		// create new schedule
		GregorianCalendar start = new GregorianCalendar(stY, stM, stDy, stH, 0);
		GregorianCalendar end = new GregorianCalendar(endY, endM, endDy, endH, 0);
		Schedule s = new Schedule(name, start, end, blockSize);
		boolean created = true;
		created = created && sDAO.addSchedule(s);

		// create meetings to fill schedule
		GregorianCalendar meetTime = start;
		while (!meetTime.equals(end)) {
			while (meetTime.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY) {
				while (meetTime.get(GregorianCalendar.HOUR_OF_DAY) < endH) {
					Meeting m = new Meeting("", meetTime, true, s);
					created = created && mDAO.addMeeting(m);
					meetTime.add(GregorianCalendar.MINUTE, blockSize);
				}
				meetTime.add(GregorianCalendar.DAY_OF_MONTH, 1);
				meetTime.set(GregorianCalendar.HOUR_OF_DAY, stH);
			}
			meetTime.add(GregorianCalendar.DAY_OF_MONTH, 2);
		}
		return created;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create constant");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateScheduleResponse response = null;

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
				response = new CreateScheduleResponse("name", 200); // OPTIONS
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
			response = new CreateScheduleResponse("Bad Request:" + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
			logger.log(req.toString());

			CreateScheduleResponse resp;
			try {
				if (createSchedule(req.name, req.startTime, req.startDate, req.endTime, req.endTime, req.blockSize)) {
					resp = new CreateScheduleResponse("Successfully created schedule: " + req.name);
				} else {
					resp = new CreateScheduleResponse("Unable to create schedule: " + req.name, 422);
				}
			} catch (Exception e) {
				resp = new CreateScheduleResponse("Unable to create schedule: " + req.name + "(" + e.getMessage() + ")",
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