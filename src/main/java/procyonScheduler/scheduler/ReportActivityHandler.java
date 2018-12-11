package procyonScheduler.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ReportActivityHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;

	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	boolean deleteOldSchedules(int n) throws Exception {
		// create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();

		boolean deleted = true;

		GregorianCalendar rightNow = new GregorianCalendar();
		rightNow.add(Calendar.DAY_OF_MONTH, n * -1);
		ArrayList<Schedule> schedules = (ArrayList<Schedule>) sDAO.getAllSchedules();
		ArrayList<Schedule> toDelete = new ArrayList<Schedule>();
		for (Schedule schedule : schedules) {
			if (rightNow.compareTo(schedule.getCreated()) >= 0) {
				toDelete.add(schedule);
			}
		}
		for (Schedule schedule : toDelete) {
			ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO
					.getAllMeetingsFromSchedule(schedule.getSecretCode());
			for (Meeting meeting : meetings) {
				deleted = deleted & mDAO.deleteMeeting(meeting);
			}
			deleted = deleted & sDAO.deleteSchedule(schedule);
		}
		return deleted;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to delete old schedules");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if
															// needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		DeleteOldSchedulesResponse response = null;

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
				response = new DeleteOldSchedulesResponse("name", 200); // OPTIONS
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
			response = new DeleteOldSchedulesResponse("Bad Request:" + pe.getMessage(), 422); // unable
																								// to
																								// process
																								// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			DeleteOldSchedulesRequest req = new Gson().fromJson(body, DeleteOldSchedulesRequest.class);
			logger.log(req.toString());

			DeleteOldSchedulesResponse resp;
			try {
				if (deleteOldSchedules(req.n)) {
					resp = new DeleteOldSchedulesResponse(
							"Successfully deleted all schedules " + req.n + " days or older.");
				} else {
					resp = new DeleteOldSchedulesResponse("Unable to delete schedules.", 422);
				}
			} catch (Exception e) {
				resp = new DeleteOldSchedulesResponse("Unable to delete schedules." + "(" + e.getMessage() + ")", 403);
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
