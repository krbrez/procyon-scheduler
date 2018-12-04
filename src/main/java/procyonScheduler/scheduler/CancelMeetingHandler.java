package procyonScheduler.scheduler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Meeting;

public class CancelMeetingHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null; 
	
	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	
	boolean cancelMeeting(String idOrCode) {
			throws Exception {
		if (logger != null) {
			logger.log("in cancelMeeting");
		}
		}
		//create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();
		
		//check if the code given is either participant code
		//or meeting id (coming from organizer)
		
		//get the meeting (and its id, and then can get other stuff
		
		//if it is, reset the data inside the meeting
			
		}
			
		//Meeting m = new Meeting()
		//boolean cancelled = true;
		//cancelled = cancelled && 
		//return cancelled;
	}
	
	//the second part, the @Override part

}
