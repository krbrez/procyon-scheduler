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
import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;

public class ExtendScheduleHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	boolean extendSchedule(String extDate, String secretCode) throws Exception {
		if (logger != null) {
			logger.log("in extendSchedule");
		}
		// create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();

		// parse date strings
		// date: YYYY-MM-DD
		int extY = 0, extM = 0, extDy = 0;
		if (extDate.length() == 10) {
			String[] extDt = extDate.split("-");
			extY = Integer.parseInt(extDt[0]);
			extM = Integer.parseInt(extDt[1]) - 1;
			extDy = Integer.parseInt(extDt[2]);
		}
		
		Schedule s = sDAO.getScheduleBySecretCode(secretCode);
		
		GregorianCalendar extend = new GregorianCalendar(extY, extM, extDy, s.getStart().get(GregorianCalendar.HOUR_OF_DAY), 0);
		if (extend.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY
				|| extend.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY) {
			return false;
		}
		
		boolean extended = true;
		GregorianCalendar start = (GregorianCalendar)s.getStart().clone();
		GregorianCalendar end = (GregorianCalendar)s.getEnd().clone();
		if(extend.compareTo(start) < 0) {
			extend.set(GregorianCalendar.HOUR_OF_DAY, start.get(GregorianCalendar.HOUR_OF_DAY));
			// create meetings to extend schedule
			GregorianCalendar meetTime = (GregorianCalendar)extend.clone();
			GregorianCalendar endTime = (GregorianCalendar)start.clone();
			endTime.set(GregorianCalendar.HOUR_OF_DAY, end.get(GregorianCalendar.HOUR_OF_DAY));
			endTime.add(GregorianCalendar.DAY_OF_MONTH, -1);
			while (meetTime.compareTo(endTime) < 0) {
				while ((meetTime.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY) && (meetTime.compareTo(endTime) < 0)) {
					while ((meetTime.get(GregorianCalendar.HOUR_OF_DAY) < end.get(GregorianCalendar.HOUR_OF_DAY)) && (meetTime.compareTo(endTime) < 0)) {
						Meeting m = new Meeting("", meetTime, true, s.getId());
						extended = extended && mDAO.addMeeting(m);
						meetTime.add(GregorianCalendar.MINUTE, s.getBlockSize());
					}
					meetTime.add(GregorianCalendar.DAY_OF_MONTH, 1);
					meetTime.set(GregorianCalendar.HOUR_OF_DAY, start.get(GregorianCalendar.HOUR_OF_DAY));
				}
				meetTime.add(GregorianCalendar.DAY_OF_MONTH, 2);
			}
			s.modifySchedule(extend, end, secretCode);
			sDAO.updateSchedule(s);
		}
		else if(extend.compareTo(end) > 0) {
			extend.set(GregorianCalendar.HOUR_OF_DAY, end.get(GregorianCalendar.HOUR_OF_DAY));
			// create meetings to extend schedule
			GregorianCalendar meetTime = (GregorianCalendar)end.clone();
			meetTime.set(GregorianCalendar.HOUR_OF_DAY, start.get(GregorianCalendar.HOUR_OF_DAY));
			meetTime.add(GregorianCalendar.DAY_OF_MONTH, 1);
			while (meetTime.compareTo(extend) < 0) {
				while ((meetTime.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY) && (meetTime.compareTo(extend) < 0)) {
					while ((meetTime.get(GregorianCalendar.HOUR_OF_DAY) < end.get(GregorianCalendar.HOUR_OF_DAY)) && (meetTime.compareTo(extend) < 0)) {
						Meeting m = new Meeting("", meetTime, true, s.getId());
						extended = extended && mDAO.addMeeting(m);
						meetTime.add(GregorianCalendar.MINUTE, s.getBlockSize());
					}
					meetTime.add(GregorianCalendar.DAY_OF_MONTH, 1);
					meetTime.set(GregorianCalendar.HOUR_OF_DAY, start.get(GregorianCalendar.HOUR_OF_DAY));
				}
				meetTime.add(GregorianCalendar.DAY_OF_MONTH, 2);
			}
			s.modifySchedule(start, extend, secretCode);
			sDAO.updateSchedule(s);
		}
		else return false;

		return extended;
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to extend schedule");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "PUT,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ExtendScheduleResponse response = null;

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
				response = new ExtendScheduleResponse("extDate, secretCode"); // OPTIONS

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
			response = new ExtendScheduleResponse("Bad Request: " + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ExtendScheduleRequest req = new Gson().fromJson(body, ExtendScheduleRequest.class);
			logger.log(req.toString());

			ExtendScheduleResponse resp;
			try {
				if (extendSchedule(req.extDate, req.secretCode)) {
					resp = new ExtendScheduleResponse("Successfully extended schedule.");
				} else {
					resp = new ExtendScheduleResponse("Unable to extend schedule.", 422);
				}
			} catch (Exception e) {
				resp = new ExtendScheduleResponse("Unable to extend schedule: " + "(" + e.getMessage() + ")",
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
