package procyonScheduler.db;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import procyonScheduler.model.Schedule;

/**
 * A class to interface the Java code with the Schedules SQL table
 *
 */
public class SchedulesDAO {

	public java.sql.Connection conn;

	/**
	 * Constructor that establishes a connection (if possible)
	 */
	public SchedulesDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			conn = null;
		}
	}

	/**
	 * Get information for a schedule from the database
	 * 
	 * @param id
	 *            The ID of the schedule to retrieve
	 * @return A Schedule object representing the desired schedule
	 * @throws Exception
	 */
	public Schedule getSchedule(String id) throws Exception {

		try {
			Schedule schedule = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				schedule = generateSchedule(resultSet);
			}
			resultSet.close();
			ps.close();

			return schedule;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting schedule: " + e.getMessage());
		}
	}

	/**
	 * Retrieve a schedule from the database, but by using the secret code
	 * instead of the ID
	 * 
	 * @param secretCode
	 *            The secret code of the desired schedule
	 * @return A Schedule object representing the desired schedule
	 * @throws Exception
	 */
	public Schedule getScheduleBySecretCode(String secretCode) throws Exception {
		try {
			Schedule schedule = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE organizerSecretCode=?;");
			ps.setString(1, secretCode);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				schedule = generateSchedule(resultSet);
			}
			resultSet.close();
			ps.close();

			return schedule;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting schedule: " + e.getMessage());
		}
	}

	/**
	 * Delete the desired schedule from the database
	 * 
	 * @param schedule
	 *            A Schedule object to be deleted
	 * @return A boolean indicating whether or not the schedule was successfully
	 *         deleted from the database
	 * @throws Exception
	 */
	public boolean deleteSchedule(Schedule schedule) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules WHERE id = ?;");
			ps.setString(1, schedule.getId());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to delete schedule: " + e.getMessage());
		}
	}

	/**
	 * Update the schedule with the ID stored in the Schedule object to have the
	 * information given
	 * 
	 * @param schedule
	 *            A Schedule object with potentially new information
	 * @return A boolean indicating whether or not the schedule has been updated
	 *         in the database
	 * @throws Exception
	 */
	public boolean updateSchedule(Schedule schedule) throws Exception {
		try {
			String query = "UPDATE Schedules SET name=?, start=?, end=?, blockSize=?, organizerSecretCode=?, creationTime=? WHERE id=?;";
			// name: 1, start: 2, end: 3, blockSize: 4, secretCode: 5,
			// creationTime: 6, id: 7
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(7, schedule.getId());
			ps.setString(1, schedule.getName());
			// Convert the start Gregorian Calendar object to the string
			String year = Integer.toString(schedule.getStart().get(Calendar.YEAR));
			int m = schedule.getStart().get(Calendar.MONTH);
			DecimalFormat form = new DecimalFormat("00");
			form.setRoundingMode(RoundingMode.DOWN);
			String month = form.format(Double.valueOf(m));
			int d = schedule.getStart().get(Calendar.DAY_OF_MONTH);
			String day = form.format(Double.valueOf(d));
			int h = schedule.getStart().get(Calendar.HOUR_OF_DAY);
			String hour = form.format(Double.valueOf(h));
			int n = schedule.getStart().get(Calendar.MINUTE);
			String minute = form.format(Double.valueOf(n));
			ps.setString(2, year + '/' + month + '/' + day + '-' + hour + ':' + minute);
			// Convert the end Gregorian Calendar object to the string
			String yearE = Integer.toString(schedule.getEnd().get(Calendar.YEAR));
			int mE = schedule.getEnd().get(Calendar.MONTH);
			String monthE = form.format(Double.valueOf(mE));
			int dE = schedule.getEnd().get(Calendar.DAY_OF_MONTH);
			String dayE = form.format(Double.valueOf(dE));
			int hE = schedule.getEnd().get(Calendar.HOUR_OF_DAY);
			String hourE = form.format(Double.valueOf(hE));
			int nE = schedule.getEnd().get(Calendar.MINUTE);
			String minuteE = form.format(Double.valueOf(nE));
			ps.setString(3, yearE + '/' + monthE + '/' + dayE + '-' + hourE + ':' + minuteE);
			ps.setInt(4, schedule.getBlockSize());
			ps.setString(5, schedule.getSecretCode());
			// Convert the created Gregorian Calendar object to the string
			String yearC = Integer.toString(schedule.getCreated().get(Calendar.YEAR));
			int mC = schedule.getCreated().get(Calendar.MONTH);
			String monthC = form.format(Double.valueOf(mC));
			int dC = schedule.getCreated().get(Calendar.DAY_OF_MONTH);
			String dayC = form.format(Double.valueOf(dC));
			int hC = schedule.getCreated().get(Calendar.HOUR_OF_DAY);
			String hourC = form.format(Double.valueOf(hC));
			int nC = schedule.getCreated().get(Calendar.MINUTE);
			String minuteC = form.format(Double.valueOf(nC));
			ps.setString(6, yearC + '/' + monthC + '/' + dayC + '-' + hourC + ':' + minuteC);
			int numAffected = ps.executeUpdate();
			ps.close();
			return (numAffected == 1);
		} catch (Exception e) {
			throw new Exception("Failed to update schedule: " + e.getMessage());
		}
	}

	/**
	 * Add the desired schedule to the database
	 * 
	 * @param schedule
	 *            The schedule to be added to the database
	 * @return A boolean indicating whether or not the schedule was added to the
	 *         database
	 * @throws Exception
	 */
	public boolean addSchedule(Schedule schedule) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE id = ?;");
			ps.setString(1, schedule.getId());
			ResultSet resultSet = ps.executeQuery();

			// already present?
			while (resultSet.next()) {
				Schedule c = generateSchedule(resultSet);
				resultSet.close();
				return false;
			}

			ps = conn.prepareStatement(
					"INSERT INTO Schedules (name,start,end,blockSize,organizerSecretCode,creationTime,id) values(?,?,?,?,?,?,?);");
			ps.setString(1, schedule.getName());
			// Convert the start Gregorian Calendar object to the string
			String year = Integer.toString(schedule.getStart().get(Calendar.YEAR));
			int m = schedule.getStart().get(Calendar.MONTH);
			DecimalFormat form = new DecimalFormat("00");
			form.setRoundingMode(RoundingMode.DOWN);
			String month = form.format(Double.valueOf(m));
			int d = schedule.getStart().get(Calendar.DAY_OF_MONTH);
			String day = form.format(Double.valueOf(d));
			int h = schedule.getStart().get(Calendar.HOUR_OF_DAY);
			String hour = form.format(Double.valueOf(h));
			int n = schedule.getStart().get(Calendar.MINUTE);
			String minute = form.format(Double.valueOf(n));
			ps.setString(2, year + '/' + month + '/' + day + '-' + hour + ':' + minute);
			// Convert the end Gregorian Calendar object to the string
			String yearE = Integer.toString(schedule.getEnd().get(Calendar.YEAR));
			int mE = schedule.getEnd().get(Calendar.MONTH);
			String monthE = form.format(Double.valueOf(mE));
			int dE = schedule.getEnd().get(Calendar.DAY_OF_MONTH);
			String dayE = form.format(Double.valueOf(dE));
			int hE = schedule.getEnd().get(Calendar.HOUR_OF_DAY);
			String hourE = form.format(Double.valueOf(hE));
			int nE = schedule.getEnd().get(Calendar.MINUTE);
			String minuteE = form.format(Double.valueOf(nE));
			ps.setString(3, yearE + '/' + monthE + '/' + dayE + '-' + hourE + ':' + minuteE);
			ps.setInt(4, schedule.getBlockSize());
			ps.setString(5, schedule.getSecretCode());
			// Convert the created Gregorian Calendar object to the string
			String yearC = Integer.toString(schedule.getCreated().get(Calendar.YEAR));
			int mC = schedule.getCreated().get(Calendar.MONTH);
			String monthC = form.format(Double.valueOf(mC));
			int dC = schedule.getCreated().get(Calendar.DAY_OF_MONTH);
			String dayC = form.format(Double.valueOf(dC));
			int hC = schedule.getCreated().get(Calendar.HOUR_OF_DAY);
			String hourC = form.format(Double.valueOf(hC));
			int nC = schedule.getCreated().get(Calendar.MINUTE);
			String minuteC = form.format(Double.valueOf(nC));
			ps.setString(6, yearC + '/' + monthC + '/' + dayC + '-' + hourC + ':' + minuteC);
			ps.setString(7, schedule.getId());
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert schedule: " + e.getMessage());
		}
	}

	/**
	 * Return all schedules stored by the Schedules table
	 * @return A list of schedules stored in the Schedules table of the database
	 * @throws Exception
	 */
	public List<Schedule> getAllSchedules() throws Exception {

		List<Schedule> allSchedules = new ArrayList<>();
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM Schedules";
			ResultSet resultSet = statement.executeQuery(query);

			//Add the schedules to the array
			while (resultSet.next()) {
				Schedule s = generateSchedule(resultSet);
				allSchedules.add(s);
			}
			resultSet.close();
			statement.close();
			return allSchedules;

		} catch (Exception e) {
			throw new Exception("Failed in getting schedules: " + e.getMessage());
		}
	}

	/**
	 * Create a Schedule object to represent the given information retrieved from the database
	 * @param resultSet The resultSet returned by the SQL call
	 * @return A Schedule object representing the given information retrieved from the database
	 * @throws Exception
	 */
	private Schedule generateSchedule(ResultSet resultSet) throws Exception {
		String name = resultSet.getString("name");
		String startDateTime = resultSet.getString("start");
		// Translate startDateTime into gregorian calendar
		GregorianCalendar start = new GregorianCalendar(Integer.parseInt(startDateTime.substring(0, 4)),
				Integer.parseInt(startDateTime.substring(5, 7)), Integer.parseInt(startDateTime.substring(8, 10)),
				Integer.parseInt(startDateTime.substring(11, 13)), Integer.parseInt(startDateTime.substring(14, 16)));
		String endDateTime = resultSet.getString("end");
		// Translate endDateTime into gregorian calendar
		GregorianCalendar end = new GregorianCalendar(Integer.parseInt(endDateTime.substring(0, 4)),
				Integer.parseInt(endDateTime.substring(5, 7)), Integer.parseInt(endDateTime.substring(8, 10)),
				Integer.parseInt(endDateTime.substring(11, 13)), Integer.parseInt(endDateTime.substring(14, 16)));
		int blockSize = resultSet.getInt("blockSize");
		String organizerSecretCode = resultSet.getString("organizerSecretCode");
		String createdDateTime = resultSet.getString("creationTime");
		// Translate createdDateTime into gregorian calendar
		GregorianCalendar creationTime = new GregorianCalendar(Integer.parseInt(createdDateTime.substring(0, 4)),
				Integer.parseInt(createdDateTime.substring(5, 7)), Integer.parseInt(createdDateTime.substring(8, 10)),
				Integer.parseInt(createdDateTime.substring(11, 13)),
				Integer.parseInt(createdDateTime.substring(14, 16)));
		String id = resultSet.getString("id");

		return new Schedule(name, start, end, blockSize, organizerSecretCode, creationTime, id);
	}

}