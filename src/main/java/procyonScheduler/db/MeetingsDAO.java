package procyonScheduler.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.GregorianCalendar;

import procyonScheduler.model.Meeting;
import procyonScheduler.model.Schedule;

public class MeetingsDAO {

	java.sql.Connection conn;

	public MeetingsDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			conn = null;
		}
	}

	public Meeting getMeeting(String participantSecretCode) throws Exception {
		try {
			Meeting meeting = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meetings WHERE participantSecretCode=?;");
			ps.setString(1, participantSecretCode);
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

	public boolean deleteMeeting(Meeting meeting) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Constants WHERE participantSecretCode = ?;");
			ps.setString(1, meeting.getParticipantSecretCode());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to delete meeting: " + e.getMessage());
		}
	}

	public boolean updateMeeting(Meeting meeting) throws Exception {
		try {
			String query = "UPDATE Constants SET value=? WHERE name=?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, meeting.getLabel());
			// Convert the dateTime Gregorian Calendar object to the string 
			String dateTime = Integer.toString(meeting.getDateTime());
			ps.setString(2, constant.name);
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);
		} catch (Exception e) {
			throw new Exception("Failed to update report: " + e.getMessage());
		}
	}

	public boolean addConstant(Constant constant) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Constants WHERE name = ?;");
			ps.setString(1, constant.name);
			ResultSet resultSet = ps.executeQuery();

			// already present?
			while (resultSet.next()) {
				Constant c = generateConstant(resultSet);
				resultSet.close();
				return false;
			}

			ps = conn.prepareStatement("INSERT INTO Constants (name,value) values(?,?);");
			ps.setString(1, constant.name);
			ps.setDouble(2, constant.value);
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert constant: " + e.getMessage());
		}
	}

	public List<Constant> getAllConstants() throws Exception {

		List<Constant> allConstants = new ArrayList<>();
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM Constants";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Constant c = generateConstant(resultSet);
				allConstants.add(c);
			}
			resultSet.close();
			statement.close();
			return allConstants;

		} catch (Exception e) {
			throw new Exception("Failed in getting books: " + e.getMessage());
		}
	}

	private Meeting generateMeeting(ResultSet resultSet) throws Exception {
		String participantSecretCode = resultSet.getString("participantSecretCode");
		String label = resultSet.getString("label");
		String dateTime = resultSet.getString("dateTime");
		int available = resultSet.getInt("available");
		String schedule = resultSet.getString("schedule");

		// Translate dateTime into gregorian calendar
		GregorianCalendar dT = new GregorianCalendar(Integer.parseInt(dateTime.substring(0, 3)),
				Integer.parseInt(dateTime.substring(5, 6)), Integer.parseInt(dateTime.substring(8, 9)),
				Integer.parseInt(dateTime.substring(11, 12)), Integer.parseInt(dateTime.substring(14, 15)));

		// Translate available into boolean
		boolean avail;
		if (available == 0) {
			avail = false;
		} else {
			avail = true;
		}

		// Get the schedule that is attached to this meeting using ScheduleDAO
		Schedule newSched;

		return new Meeting(label, dT, avail, newSched, participantSecretCode);
	}

}