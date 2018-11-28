package procyonScheduler.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import procyonScheduler.model.Schedule;

public class SchedulesDAO {

	java.sql.Connection conn;

	public SchedulesDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			conn = null;
		}
	}

	public Schedule getSchedule(String organizerSecretCode) throws Exception {

		try {
			Schedule schedule = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE organizerSecretCode=?;");
			ps.setString(1, organizerSecretCode);
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

	public boolean deleteSchedule(Schedule schedule) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules WHERE secretCode = ?;");
			ps.setString(1, schedule.getSecretCode());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to delete schedule: " + e.getMessage());
		}
	}

	/*
	 * Revisit when adding use cases
	 */
	// public boolean updateSchedule(Schedule schedule) throws Exception {
	// try {
	// String query = "UPDATE Constants SET value=? WHERE name=?;";
	// PreparedStatement ps = conn.prepareStatement(query);
	// ps.setDouble(1, constant.value);
	// ps.setString(2, constant.name);
	// int numAffected = ps.executeUpdate();
	// ps.close();
	//
	// return (numAffected == 1);
	// } catch (Exception e) {
	// throw new Exception("Failed to update report: " + e.getMessage());
	// }
	// }

	public boolean addSchedule(Schedule schedule) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE organizerSecretCode = ?;");
			ps.setString(1, schedule.getSecretCode());
			ResultSet resultSet = ps.executeQuery();

			// already present?
			while (resultSet.next()) {
				Schedule c = generateSchedule(resultSet);
				resultSet.close();
				return false;
			}

			// Name
			// Start
			// End
			// blockSize
			// organizerSecretCode*
			// creationTime

			ps = conn.prepareStatement(
					"INSERT INTO Schedules (name,start,end,blockSize,organizerSecretCode,creationTime) values(?,?,?,?,?,?,?);");
			ps.setString(1, schedule.getName());
			// You are here
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert constant: " + e.getMessage());
		}
	}

	public List<Schedule> getAllSchedules() throws Exception {

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

	private Schedule generateSchedule(ResultSet resultSet) throws Exception {
		String name = resultSet.getString("name");
		Double value = resultSet.getDouble("value");
		return new Constant(name, value);
	}

}