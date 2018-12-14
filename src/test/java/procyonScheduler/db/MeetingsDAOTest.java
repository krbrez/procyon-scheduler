package procyonScheduler.db;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import procyonScheduler.model.Meeting;

/**
 * Test cases for the MeetingsDAO class
 */
public class MeetingsDAOTest {

	/**
	 * Tests the constructor
	 */
	@Test
	public void testConstructorConnection() {
		MeetingsDAO mDAO = new MeetingsDAO();

		Assert.assertFalse(mDAO.conn.equals(null));
	}

	/**
	 * Tests getting a meeting from the DB and generating a Meeting object from
	 * it
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetMeetingAndGenerateMeeting() throws Exception {
		MeetingsDAO mDAO = new MeetingsDAO();

		// Use a meeting that I know is in the DB to be
		// able to check its values.
		Meeting testMeeting = mDAO.getMeeting("zWw9bmX3N4F8JAMD");
		Assert.assertEquals(testMeeting.getId(), "zWw9bmX3N4F8JAMD");
	}

	/**
	 * Tests adding and deleting a meeting from the DB
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddAndDeleteMeeting() throws Exception {
		GregorianCalendar gC = new GregorianCalendar(2018, 11, 5, 12, 20);
		Meeting m = new Meeting("", gC, true, "onlt0O2wk7YFyUnG");
		MeetingsDAO mDAO = new MeetingsDAO();
		String meetingID = m.getId();

		Assert.assertTrue(mDAO.addMeeting(m));
		Assert.assertEquals(mDAO.getMeeting(meetingID).getSchedule(), m.getSchedule());
		Assert.assertTrue(mDAO.deleteMeeting(m));
	}

	/**
	 * Tests updating a meeting's information
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateMeeting() throws Exception {
		GregorianCalendar gC = new GregorianCalendar(2018, 11, 5, 12, 20);
		Meeting m = new Meeting("", gC, true, "onlt0O2wk7YFyUnG");
		MeetingsDAO mDAO = new MeetingsDAO();
		String meetingID = m.getId();

		mDAO.addMeeting(m);
		m.setParticipantSecretCode("JJJJJJJJJJJJJJJJ");
		Assert.assertTrue(mDAO.updateMeeting(m));
		Assert.assertEquals(mDAO.getMeeting(meetingID).getParticipantSecretCode(), "JJJJJJJJJJJJJJJJ");

		// delete after to make other test cases easier
		mDAO.deleteMeeting(m);
	}

	/**
	 * Tests getting all meetings from a specific schedule
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllMeetingsFromSchedule() throws Exception {
		MeetingsDAO mDAO = new MeetingsDAO();

		ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO.getAllMeetingsFromSchedule("onlt0O2wk7YFyUnG");
		Assert.assertEquals("onlt0O2wk7YFyUnG", meetings.get(1).getSchedule());
	}

	/**
	 * Tests getting a specific week from a schedule
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetWeekFromSchedule() throws Exception {
		GregorianCalendar gC = new GregorianCalendar(2018, 11, 17);
		MeetingsDAO mDAO = new MeetingsDAO();

		ArrayList<Meeting> meetings = (ArrayList<Meeting>) mDAO.getWeekFromSchedule("onlt0O2wk7YFyUnG", gC);
		Assert.assertEquals("onlt0O2wk7YFyUnG", meetings.get(1).getSchedule());
	}
}
