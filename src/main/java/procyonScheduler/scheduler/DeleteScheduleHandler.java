package procyonScheduler.scheduler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class DeleteScheduleHandler {
	
	public LambdaLogger logger = null;
	
	/**
	 * Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	
	boolean deleteSchedule(String name, String secretCode)
			throws Exception {
		if (logger != null) {
			logger.log("in deleteSchedule");
		}
		
		//do i need to create DAO objects? 
		
		//find the schedule with the right name
		
		//delete the schedule
		
		
		boolean deleted = true;
		deleted = deleted && sDAO.deleteSchedule(s);
		
		logger.log("Here -- deleted");
		return deleted;
	}
	
	//this is the second part, it falls under the @Override

}
