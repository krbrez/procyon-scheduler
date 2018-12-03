package procyonScheduler.scheduler;

import java.util.Iterator;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import procyonScheduler.db.MeetingsDAO;
import procyonScheduler.db.SchedulesDAO;
import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;

public class DeleteScheduleHandler {
	
	public LambdaLogger logger = null;
	
	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	
	boolean deleteSchedule(String secretCode)
			throws Exception {
		if (logger != null) {
			logger.log("in deleteSchedule");
		}
		
		//create DAO objects
		SchedulesDAO sDAO = new SchedulesDAO();
		MeetingsDAO mDAO = new MeetingsDAO();
		
		//find the schedule with the right secret code
		Schedule deleteMe = sDAO.getSchedule(secretCode);
		
		//delete the schedule
		sDAO.deleteSchedule(deleteMe);
		
		boolean deleted = true;
		deleted = deleted && sDAO.deleteSchedule(deleteMe); 	//does the deleted-ness rely on deleting all the meetings too?
																//if so, i don't know how to quantify that
		
		//delete all the meetings inside the schedule
		for (Iterator<Meeting> it = mDAO.getAllMeetingsFromSchedule(secretCode).iterator(); it.hasNext(); ) {
			Meeting m = it.next();
			mDAO.deleteMeeting(m);
		}
		
		logger.log("Here -- deleted");
		return deleted;
	}
	
	//this is the second part, it falls under the @Override, Kyra can help me

}
