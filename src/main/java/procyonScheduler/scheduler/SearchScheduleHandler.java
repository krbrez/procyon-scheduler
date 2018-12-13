package procyonScheduler.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

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

/**
 * Handler class for the SearchOpenTimeSlot use case for participants
 *
 */
public class SearchScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	/**
	 * Returns the list of open meetings that fit certain filters
	 * 
	 * @param schedule
	 *            The ID for the schedule to search
	 * @param month
	 *            The month desired, empty if no input
	 * @param year
	 *            The year desired, empty if no input
	 * @param weekday
	 *            The day of the week desired, empty if no input
	 * @param day
	 *            The day of the month desired, empty if no input
	 * @param time
	 *            The time of day desired, empty if no input
	 * @return The list of open meetings that fit all applied filters
	 * @throws Exception
	 */
	ArrayList<Meeting> searchSchedule(String schedule, String month, String year, String weekday, String day,
			String time) throws Exception {
		if (logger != null) {
			logger.log("in searchSchedule");
		}

		MeetingsDAO mDAO = new MeetingsDAO();
		ArrayList<Meeting> foundMeetings = mDAO.getAllMeetingsFromSchedule(schedule);

		Iterator<Meeting> meetingsItr = foundMeetings.iterator();

		// Parsing for filters
		HashMap<String, Integer> months = new HashMap<>();
		months.put("january", 0);
		months.put("february", 1);
		months.put("march", 2);
		months.put("april", 3);
		months.put("may", 4);
		months.put("june", 5);
		months.put("july", 6);
		months.put("august", 7);
		months.put("september", 8);
		months.put("october", 9);
		months.put("november", 10);
		months.put("december", 11);

		HashMap<String, Integer> weekdays = new HashMap<>();
		weekdays.put("sunday", 1);
		weekdays.put("monday", 2);
		weekdays.put("tuesday", 3);
		weekdays.put("wednesday", 4);
		weekdays.put("thursday", 5);
		weekdays.put("friday", 6);
		weekdays.put("saturday", 7);

		// Go through all meetings for schedule and remove any that are closed
		// or do not fit filters
		while (meetingsItr.hasNext()) {
			Meeting m = (Meeting) meetingsItr.next();
			if (!m.getAvailable()) {
				meetingsItr.remove();
				if (logger != null)
					logger.log("avail");
			} else if (!m.getLabel().equals("")) {
				meetingsItr.remove();
				if (logger != null)
					logger.log("label");
			} else if (!month.equals("")
					&& m.getDateTime().get(GregorianCalendar.MONTH) != months.get(month.toLowerCase())) {
				meetingsItr.remove();
				if (logger != null)
					logger.log("month");
			} else if (!year.equals("") && m.getDateTime().get(GregorianCalendar.YEAR) != Integer.parseInt(year)) {
				meetingsItr.remove();
				if (logger != null)
					logger.log("year");
			} else if (!weekday.equals("")
					&& m.getDateTime().get(GregorianCalendar.DAY_OF_WEEK) != weekdays.get(weekday.toLowerCase())) {
				meetingsItr.remove();
				if (logger != null)
					logger.log("weekday");
			} else if (!day.equals("")
					&& m.getDateTime().get(GregorianCalendar.DAY_OF_MONTH) != Integer.parseInt(day)) {
				meetingsItr.remove();
				if (logger != null)
					logger.log("day");
			} else if (!time.equals("")) {
				String[] timeSplit = time.split(":");
				int hour = Integer.parseInt(timeSplit[0]);
				int minute = Integer.parseInt(timeSplit[1]);
				if (m.getDateTime().get(GregorianCalendar.HOUR_OF_DAY) != hour
						|| m.getDateTime().get(GregorianCalendar.MINUTE) != minute) {
					meetingsItr.remove();
					if (logger != null)
						logger.log("time");
				}
			}
		}
		return foundMeetings;
	}

	/**
	 * The specific handleRequest method for this lambda
	 */
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
				ArrayList<Meeting> foundMeetings = searchSchedule(req.schedule, req.month, req.year, req.weekday,
						req.day, req.time);
				if (foundMeetings.size() > 0) {
					Comparator<Meeting> chrono = new Comparator<Meeting>() {
						public int compare(Meeting m1, Meeting m2) {
							return m1.getDateTime().compareTo(m2.getDateTime());
						}
					};
					foundMeetings.sort(chrono);
					resp = new SearchScheduleResponse(foundMeetings);
				} else {
					resp = new SearchScheduleResponse(
							"No meetings fitting search criteria. Try broadening your search.", 422);
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
