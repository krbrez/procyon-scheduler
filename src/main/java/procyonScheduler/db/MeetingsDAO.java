package procyonScheduler.db;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import java.util.GregorianCalendar;
import java.util.HashMap;

import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;
import procyonScheduler.scheduler.DeleteScheduleHandler;

/**
 * A class that helps connect the code to the Meetings table in the database
 *
 */
public class MeetingsDAO {

	java.sql.Connection conn;

	/**
	 * Constructor; connects to database
	 */
	public MeetingsDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			conn = null;
		}
	}

	/**
	 * Gets the meeting with the given ID from the database
	 * 
	 * @param id
	 *            The ID for the meeting to be retrieved
	 * @return Meeting object of the meeting with the given ID
	 * @throws Exception
	 */
	public Meeting getMeeting(String id) throws Exception {
		try {
			Meeting meeting = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE id=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				meeting = generateMeeting(resultSet);
			}
			resultSet.close();
			ps.close();

			return meeting;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting meeting: " + e.getMessage());
		}
	}

	/**
	 * Deletes the given meeting from the database
	 * 
	 * @param meeting
	 *            The meeting to be deleted
	 * @return A boolean telling whether or not the meeting was successfully
	 *         deleted
	 * @throws Exception
	 */
	public boolean deleteMeeting(Meeting meeting) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Meetings WHERE id = ?;");
			ps.setString(1, meeting.getId());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to delete meeting: " + e.getMessage());
		}
	}

	/**
	 * Update the meeting so that the database matches the object given
	 * 
	 * @param meeting
	 *            A meeting with the ID of the meeting to be changed and new
	 *            information otherwise
	 * @return A boolean telling whether or not the meeting was successfully
	 *         updated in the database
	 * @throws Exception
	 */
	public boolean updateMeeting(Meeting meeting) throws Exception {
		try {
			String query = "UPDATE Meetings SET Label=?, DateTime=?, Available=?, Schedule=?, participantSecretCode=? WHERE id=?;";
			// 1: label 2: dateTime 3: available 4: schedule 5: PSC 6: id
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(6, meeting.getId());

			ps.setString(1, meeting.getLabel());
			// Convert the dateTime Gregorian Calendar object to the string
			String year = Integer.toString(meeting.getDateTime().get(Calendar.YEAR));
			int m = meeting.getDateTime().get(Calendar.MONTH);
			DecimalFormat form = new DecimalFormat("00");
			String month = form.format(Double.valueOf(m));
			int d = meeting.getDateTime().get(Calendar.DAY_OF_MONTH);
			String day = form.format(Double.valueOf(d));
			int h = meeting.getDateTime().get(Calendar.HOUR_OF_DAY);
			String hour = form.format(Double.valueOf(h));
			int n = meeting.getDateTime().get(Calendar.MINUTE);
			String minute = form.format(Double.valueOf(n));
			ps.setString(2, year + '/' + month + '/' + day + '-' + hour + ':' + minute);
			int intA;
			if (meeting.getAvailable()) {
				intA = 1;
			} else {
				intA = 0;
			}
			ps.setInt(3, intA);
			ps.setString(4, meeting.getSchedule());
			ps.setString(5, meeting.getParticipantSecretCode());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);
		} catch (Exception e) {
			throw new Exception("Failed to update meeting: " + e.getMessage());
		}
	}

	/**
	 * Add a new meeting to the database
	 * 
	 * @param meeting
	 *            The meeting object holding the information to be added to the
	 *            database
	 * @return A boolean indicating whether or not the meeting was added to the
	 *         database successfully
	 * @throws Exception
	 */
	public boolean addMeeting(Meeting meeting) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE id = ?;");
			ps.setString(1, meeting.getId());
			ResultSet resultSet = ps.executeQuery();

			// already present?
			while (resultSet.next()) {
				Meeting c = generateMeeting(resultSet);
				resultSet.close();
				return false;
			}

			ps = conn.prepareStatement(
					"INSERT INTO Meetings (label,dateTime,available,schedule,participantSecretCode,id) values(?,?,?,?,?,?);");
			ps.setString(1, meeting.getLabel());
			// Convert the dateTime Gregorian Calendar object to the string
			String year = Integer.toString(meeting.getDateTime().get(Calendar.YEAR));
			int m = meeting.getDateTime().get(Calendar.MONTH);
			DecimalFormat form = new DecimalFormat("00");
			String month = form.format(Double.valueOf(m));
			int d = meeting.getDateTime().get(Calendar.DAY_OF_MONTH);
			String day = form.format(Double.valueOf(d));
			int h = meeting.getDateTime().get(Calendar.HOUR_OF_DAY);
			String hour = form.format(Double.valueOf(h));
			int n = meeting.getDateTime().get(Calendar.MINUTE);
			String minute = form.format(Double.valueOf(n));
			ps.setString(2, year + '/' + month + '/' + day + '-' + hour + ':' + minute);
			int intA;
			if (meeting.getAvailable()) {
				intA = 1;
			} else {
				intA = 0;
			}
			ps.setInt(3, intA);
			ps.setString(4, meeting.getSchedule());
			ps.setString(5, meeting.getParticipantSecretCode());
			ps.setString(6, meeting.getId());
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert meeting: " + e.getMessage());
		}
	}

	/**
	 * Gets all the meetings from a given schedule
	 * 
	 * @param id
	 *            The ID of the schedule desired to retrieve meetings from
	 * @return An ArrayList of meetings associated with that schedule
	 * @throws Exception
	 */
	public ArrayList<Meeting> getAllMeetingsFromSchedule(String id) throws Exception {

		ArrayList<Meeting> allMeetings = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE schedule = ?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();

			// Add all the meetings to the array
			while (resultSet.next()) {
				Meeting m = generateMeeting(resultSet);
				allMeetings.add(m);
			}
			resultSet.close();
			ps.close();
			return allMeetings;

		} catch (Exception e) {
			throw new Exception("Failed in getting meetings: " + e.getMessage());
		}
	}

	/**
	 * Retrieve all meetings from schedule with id occurring in the week
	 * starting with startDay (i.e. startDay must be a Monday)
	 * 
	 * @param id
	 *            the id of the schedule desired to retrieve meetings from
	 * @param startDay
	 *            the Monday of the week to be retrieved
	 * @return All of the meetings found in the desired week, Monday-Friday
	 * @throws Exception
	 */
	public ArrayList<Meeting> getWeekFromSchedule(String id, GregorianCalendar startDay) throws Exception {

		SchedulesDAO sDAO = new SchedulesDAO();
		Schedule s = sDAO.getSchedule(id);

		List<Meeting> allMeetings = new ArrayList<>();

		// set when week starts and ends
		GregorianCalendar endDay = (GregorianCalendar) startDay.clone();
		startDay.set(GregorianCalendar.HOUR_OF_DAY, s.getStart().get(GregorianCalendar.HOUR_OF_DAY));
		endDay.add(GregorianCalendar.DAY_OF_MONTH, 4);
		endDay.set(GregorianCalendar.HOUR_OF_DAY, s.getEnd().get(GregorianCalendar.HOUR_OF_DAY));

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE schedule=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Meeting m = generateMeeting(resultSet);
				allMeetings.add(m);
			}
			resultSet.close();
			ps.close();

			// Only keep the ones that occur during that week
			ArrayList<Meeting> weekMeetings = new ArrayList<Meeting>();
			for (int i = 0; i < allMeetings.size(); i++) {
				GregorianCalendar occurs = allMeetings.get(i).getDateTime();
				if ((occurs.compareTo(startDay) >= 0) && (occurs.compareTo(endDay) <= 0)) {
					weekMeetings.add(allMeetings.get(i));
				}
			}
			return weekMeetings;

		} catch (Exception e) {
			throw new Exception("Failed in getting meetings: " + e.getMessage());
		}
	}

	/**
	 * Create a meeting object from what has been returned from the database
	 * 
	 * @param resultSet
	 *            The resultSet of a meeting from the SQL call
	 * @return A meeting object with the information for that meeting retrieved
	 *         from the db
	 * @throws Exception
	 */
	private Meeting generateMeeting(ResultSet resultSet) throws Exception {
		String participantSecretCode = resultSet.getString("participantSecretCode");
		String label = resultSet.getString("label");
		String dateTime = resultSet.getString("dateTime");
		int available = resultSet.getInt("available");
		String schedule = resultSet.getString("schedule");
		String id = resultSet.getString("id");

		// Translate dateTime into gregorian calendar
		GregorianCalendar dT = new GregorianCalendar(Integer.parseInt(dateTime.substring(0, 4)),
				Integer.parseInt(dateTime.substring(5, 7)), Integer.parseInt(dateTime.substring(8, 10)),
				Integer.parseInt(dateTime.substring(11, 13)), Integer.parseInt(dateTime.substring(14, 16)));

		// Translate available into boolean
		boolean avail;
		if (available == 0) {
			avail = false;
		} else {
			avail = true;
		}

		// Get the schedule that is attached to this meeting using SchedulesDAO
		SchedulesDAO sd = new SchedulesDAO();
		Schedule newSched = sd.getSchedule(schedule);

		return new Meeting(id, label, dT, avail, newSched.getId(), participantSecretCode);
	}

}