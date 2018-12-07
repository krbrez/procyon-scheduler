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
import procyonScheduler.model.Meeting;

public class CreateMeetingHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	String createMeeting(String id, String label) throws Exception {
		if (logger != null) {
			logger.log("in createMeeting");
		}
		MeetingsDAO mDAO = new MeetingsDAO();
		Meeting m = mDAO.getMeeting(id);
		if (m.getLabel().equals("") && m.getAvailable()) {
			if (label != "") {
				m.setLabel(label);
			} else {
				m.setLabel("Scheduled");
			}
			mDAO.updateMeeting(m);
			return m.getParticipantSecretCode();
		} else {
			return "";
		}
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

		CreateMeetingResponse response = null;

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
				response = new CreateMeetingResponse("name", 200); // OPTIONS
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
			response = new CreateMeetingResponse("Bad Request:" + pe.getMessage(), 422); // unable
																							// to
																							// process
																							// input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			CreateMeetingRequest req = new Gson().fromJson(body, CreateMeetingRequest.class);
			logger.log(req.toString());

			CreateMeetingResponse resp;
			try {
				String result = createMeeting(req.id, req.label);
				if (result != "") {
					resp = new CreateMeetingResponse(
							"Successfully created meeting.", req.label, result);
				} else {
					resp = new CreateMeetingResponse("Unable to create meeting", req.label, 422);
				}
			} catch (Exception e) {
				resp = new CreateMeetingResponse("Unable to create meeting: "+ " (" + e.getMessage() + ")", req.label, 403);
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
