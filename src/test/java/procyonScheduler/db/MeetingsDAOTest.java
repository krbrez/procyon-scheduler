package procyonScheduler.db;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import procyonScheduler.model.Meeting;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class MeetingsDAOTest {

	@Test
	public void testConstructorConnection() {
		MeetingsDAO mDAO = new MeetingsDAO();

		Assert.assertFalse(mDAO.equals(null));
	}

	@Test
	public void testGetMeetingAndGenerateMeeting() throws Exception{
		MeetingsDAO mDAO = new MeetingsDAO();
		
		// Use a meeting that I know is in the DB to be 
		// able to check its values.
		Meeting testMeeting = mDAO.getMeeting("hiQ3vL2P8cxn0JnQ");
		Assert.assertEquals(testMeeting.getId(), "hiQ3vL2P8cxn0JnQ");
	}
	
	@Test
	public void testAddAndDeleteMeeting() throws Exception{
		GregorianCalendar gC = new GregorianCalendar(2018, 11, 5, 12, 20);
		Meeting m  = new Meeting("", gC, true, "0a891YnhrRILP4N4");
		MeetingsDAO mDAO = new MeetingsDAO();
		String meetingID = m.getId();
		
		Assert.assertTrue(mDAO.addMeeting(m));
		Assert.assertEquals(mDAO.getMeeting(meetingID).getSchedule(), m.getSchedule());		
		Assert.assertTrue(mDAO.deleteMeeting(m));		
	}
	
	@Test 
	public void testUpdateMeeting() throws Exception{
		GregorianCalendar gC = new GregorianCalendar(2018, 11, 5, 12, 20);
		Meeting m  = new Meeting("", gC, true, "0a891YnhrRILP4N4");
		MeetingsDAO mDAO = new MeetingsDAO();
		String meetingID = m.getId();
		
		mDAO.addMeeting(m);
		m.setParticipantSecretCode("JJJJJJJJJJJJJJJJ");
		Assert.assertTrue(mDAO.updateMeeting(m));
		Assert.assertEquals(mDAO.getMeeting(meetingID).getParticipantSecretCode(), "JJJJJJJJJJJJJJJJ");
	}
	
	@Test
	public void testGetAllMeetingsFromSchedule() throws Exception{
		MeetingsDAO mDAO = new MeetingsDAO();
		
		ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO.getAllMeetingsFromSchedule("0a891YnhrRILP4N4");
		Assert.assertEquals("0a891YnhrRILP4N4", meetings.get(1).getSchedule());
	}
	
	@Test
	public void testGetWeekFromSchedule() throws Exception{
		GregorianCalendar gC = new GregorianCalendar(2018, 10, 30);
		MeetingsDAO mDAO = new MeetingsDAO();
		
		ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO.getWeekFromSchedule("0a891YnhrRILP4N4", gC);
		Assert.assertEquals("0a891YnhrRILP4N4", meetings.get(1).getSchedule());
	}
}
