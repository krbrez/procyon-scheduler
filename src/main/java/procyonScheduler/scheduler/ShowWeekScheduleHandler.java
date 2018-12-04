package procyonScheduler.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import procyonScheduler.model.Meeting;

public class ShowWeekScheduleHandler implements RequestStreamHandler {
	
	List<Meeting> showWeek(String id, String startDay) {
		List<Meeting> weekMeetings = new ArrayList<>();
		
		return weekMeetings;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub

	}

}
