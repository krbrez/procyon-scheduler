package procyonScheduler.model;

import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Class to represent individual schedules
 *
 */
public class Schedule implements Comparable<Schedule> {
	String name; // non-unique user defined name
	GregorianCalendar start; // date and time to start schedule on
	GregorianCalendar end; // date and time to end schedule on
	int blockSize; // how many minutes each meeting is (may be 10, 15, 20, 30,
					// 60)
	String secretCode; // unique string required for organizer access also used
						// as schedule ID
	GregorianCalendar created; // date and time schedule was created
	String id;

	/**
	 * Constructor for making a new schedule; other necessary fields are
	 * generated
	 * 
	 * @param name
	 *            The name for the schedule
	 * @param start
	 *            The start day and time for the schedule
	 * @param end
	 *            The end day and time for the schedule
	 * @param blockSize
	 *            The size of each meeting in minutes
	 */
	public Schedule(String name, GregorianCalendar start, GregorianCalendar end, int blockSize) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.blockSize = blockSize;
		// generate a random secret code
		this.secretCode = "";
		Random r = new Random();
		for (int i = 0; i < 16; i++) {
			int n = r.nextInt(62);
			if (n < 10) {
				this.secretCode += Integer.toString(n);
			} else if (n < 36) {
				this.secretCode += (char) (n + 55);
			} else { // n 36 - 61
				this.secretCode += (char) (n + 61);
			}
		}
		// generate a random ID
		this.id = "";
		for (int i = 0; i < 16; i++) {
			int n = r.nextInt(62);
			if (n < 10) {
				this.id += Integer.toString(n);
			} else if (n < 36) {
				this.id += (char) (n + 55);
			} else { // n 36 - 61
				this.id += (char) (n + 61);
			}
		}
		this.created = new GregorianCalendar();
	}

	/**
	 * Constructor for getting an existing schedule, used when a meeting already
	 * exists and just needs to be represented
	 * 
	 * @param name
	 *            The name of the schedule
	 * @param start
	 *            The start date and time
	 * @param end
	 *            The end date and time
	 * @param blockSize
	 *            The size of each meeting in minutes
	 * @param secretCode
	 *            The unique secret code for edits to the schedule
	 * @param created
	 *            The date and time the schedule was created
	 * @param id
	 *            The unique schedule identifier
	 */
	public Schedule(String name, GregorianCalendar start, GregorianCalendar end, int blockSize, String secretCode,
			GregorianCalendar created, String id) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.blockSize = blockSize;
		this.secretCode = secretCode;
		this.created = created;
		this.id = id;
	}

	/**
	 * A comparator for sorting by the day of creation
	 * 
	 * @param other
	 *            The Schedule to compared to
	 * @return A comparison int
	 */
	public int compareTo(Schedule other) {
		return this.created.compareTo(other.created);
	}

	/**
	 * Adjust parameters of schedule
	 * @param start The start date and time of the schedule
	 * @param end The end date and time of the schedule
	 * @param secretCode The unique secret code to edit the schedule
	 * @return True if the user has access per secret code, false if not
	 */
	public boolean modifySchedule(GregorianCalendar start, GregorianCalendar end, String secretCode) {
		if (this.secretCode.equals(secretCode)) {
			this.start = start;
			this.end = end;
			return true;
		} else
			return false;
	}

	/**
	 * Getter for the secret code
	 * @return The secret code
	 */
	public String getSecretCode() {
		return secretCode;
	}

	/**
	 * Getter for the name
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the start date and time
	 * @return The start date and time
	 */
	public GregorianCalendar getStart() {
		return start;
	}

	/**
	 * Getter for the end date and time
	 * @return The end date and time
	 */
	public GregorianCalendar getEnd() {
		return end;
	}

	/**
	 * Getter for the block size
	 * @return The block size for each meeting, in minutes
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * Getter for the creation date and time
	 * @return The creation date and time
	 */
	public GregorianCalendar getCreated() {
		return created;
	}

	/**
	 * Getter for the ID
	 * @return The schedule ID
	 */
	public String getId() {
		return id;
	}

}
