package procyonScheduler.db;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class SchedulesDAOTest {

	@Test
	public void testConstructorConnection() {
		MeetingsDAO sDAO = new MeetingsDAO();

		Assert.assertFalse(sDAO.conn.equals(null));
	}

	@Test
	public void testGetScheduleAndGenerateSchedule() throws Exception {
		SchedulesDAO sDAO = new SchedulesDAO();

		// Use a meeting that I know is in the DB to be
		// able to check its values.
		Schedule testSchedule = sDAO.getSchedule("onlt0O2wk7YFyUnG");
		Assert.assertEquals(testSchedule.getId(), "onlt0O2wk7YFyUnG");
	}

	@Test
	public void testAddAndDeleteSchedule() throws Exception {
		GregorianCalendar start = new GregorianCalendar(2018, 11, 5, 12, 00);
		GregorianCalendar end = new GregorianCalendar(2018, 11, 7, 15, 00);

		Schedule s = new Schedule("DeleteThis", start, end, 20);
		SchedulesDAO sDAO = new SchedulesDAO();
		String scheduleID = s.getId();

		Assert.assertTrue(sDAO.addSchedule(s));
		Assert.assertEquals(sDAO.getSchedule(scheduleID).getName(), "DeleteThis");
		Assert.assertTrue(sDAO.deleteSchedule(s));
	}

	@Test
	public void testGetScheduleBySecretCode() throws Exception{
		SchedulesDAO sDAO = new SchedulesDAO();
		
		Schedule s = sDAO.getScheduleBySecretCode("8lPq7LYmpQbpa1E4");
		Assert.assertEquals(s.getId(), "onlt0O2wk7YFyUnG");
	}
	
	 @Test
	 public void testGetAllSchedules() throws Exception{
	 SchedulesDAO sDAO = new SchedulesDAO();
	
	 ArrayList<Schedule> schedules = (ArrayList<Schedule>) sDAO.getAllSchedules();
	 Schedule s = sDAO.getSchedule("onlt0O2wk7YFyUnG");
	 Assert.assertEquals(schedules.get(1).getClass(), s.getClass());
	 }
}
