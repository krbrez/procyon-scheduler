package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

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
		String createdDateTime = "2018/10/22-05:05";
		GregorianCalendar creationTime = new GregorianCalendar(Integer.parseInt(createdDateTime.substring(0, 4)),
				Integer.parseInt(createdDateTime.substring(5, 7)), Integer.parseInt(createdDateTime.substring(8, 10)),
				Integer.parseInt(createdDateTime.substring(11, 13)),
				Integer.parseInt(createdDateTime.substring(14, 16)));
		System.out.println(" date: " + Integer.toString(creationTime.get(GregorianCalendar.YEAR)) + "-"
				+ Integer.toString(creationTime.get(GregorianCalendar.MONTH)) + "-"
				+ Integer.toString(creationTime.get(GregorianCalendar.DAY_OF_MONTH)) + " time: "
				+ Integer.toString(creationTime.get(GregorianCalendar.HOUR_OF_DAY)) + ":"
				+ Integer.toString(creationTime.get(GregorianCalendar.MINUTE)));
		
	}

}
