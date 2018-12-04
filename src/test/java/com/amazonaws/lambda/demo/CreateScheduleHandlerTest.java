package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;
import procyonScheduler.scheduler.CreateScheduleHandler;
import procyonScheduler.scheduler.CreateScheduleRequest;
import procyonScheduler.scheduler.CreateScheduleResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateScheduleHandlerTest {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testAddTwoNumbers() throws IOException {
		// create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();
		try {
			// find the schedule with the right secret code
			Schedule deleteMe = sDAO.getScheduleBySecretCode("ZzjQ81EUi4TrkJFs");

			boolean deleted = true;
			deleted = deleted && sDAO.deleteSchedule(deleteMe);

			// delete all the meetings inside the schedule
			ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO.getAllMeetingsFromSchedule(deleteMe.getId());
			for (Meeting meeting: meetings) {
				System.out.println(meeting.getId());
				deleted = deleted && mDAO.deleteMeeting(meeting);
			}
		} catch (Exception e) {
			System.out.println("ahh");
		}
	}

}
