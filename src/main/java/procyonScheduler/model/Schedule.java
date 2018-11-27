package procyonScheduler.model;
import java.util.GregorianCalendar;

public class Schedule {
	String name;					// non-unique user defined name 
	GregorianCalendar start;		// date and time to start schedule on
	GregorianCalendar end;			// date and time to end schedule on
	int blockSize;					// how many minutes each meeting is (may be 10, 15, 20, 30, 60)
	String secretCode;				// unique string required for organizer access also used as schedule ID
	GregorianCalendar created;		// date and time schedule was created
	
	// constructor
	public Schedule(String name, GregorianCalendar start, GregorianCalendar end, int blockSize) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.blockSize = blockSize;
		this.secretCode = "";
		this.created = new GregorianCalendar();
	}
	
	// meetings iterator
//	public Iterator<Meeting> meetings() {
//		return meetings.iterator();
//	}
	
	// adjust parameters of schedule (like one big setter)
	public boolean modifySchedule(GregorianCalendar start, GregorianCalendar end, int blockSize, String secretCode) {
		if(this.secretCode.equals(secretCode)) {
			this.start = start;
			this.end = end;
			this.blockSize = blockSize;
			return true;
		}
		else return false;
	}
	
	// schedule the meeting with the given id
	// returns the meeting's secret code
	public String scheduleMeeting(int id, String label) {
		return "";
	}
	
	public boolean cancelMeeting(int id, String secretCode) {
		return true;
	}
	
	//public ArrayList<Meeting> findAvailable(GregorianCalendar start, GregorianCalendar end) {
	//	return this.meetings;
	//}
	
}
