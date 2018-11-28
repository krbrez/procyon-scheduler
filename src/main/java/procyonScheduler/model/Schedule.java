package procyonScheduler.model;
import java.util.GregorianCalendar;
import java.util.Random;

public class Schedule {
	String name;					// non-unique user defined name 
	GregorianCalendar start;		// date and time to start schedule on
	GregorianCalendar end;			// date and time to end schedule on
	int blockSize;					// how many minutes each meeting is (may be 10, 15, 20, 30, 60)
	String secretCode;				// unique string required for organizer access also used as schedule ID
	GregorianCalendar created;		// date and time schedule was created
	
	// constructor for initiation of schedule
	public Schedule(String name, GregorianCalendar start, GregorianCalendar end, int blockSize) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.blockSize = blockSize;
		// generate a random secret code
		this.secretCode = "";
		Random r = new Random();
		for(int i = 0; i < 16; i++) {
			int n = r.nextInt(62);
			if(n < 10) {
				this.secretCode += Integer.toString(n);
			}
			else if(n < 36) {
				this.secretCode += (char)(n + 55);
			}
			else { // n 37 - 61
				this.secretCode += (char)(n + 60);
			}
		}
		this.created = new GregorianCalendar();
	}
	
	// constructor for pulling schedule out of database
	public Schedule(String name, GregorianCalendar start, GregorianCalendar end, int blockSize, String secretCode, GregorianCalendar created) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.blockSize = blockSize;
		this.secretCode = secretCode;
		this.created = created;
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
	
	public String getSecretCode(){
		return secretCode;
	}
	
	//public ArrayList<Meeting> findAvailable(GregorianCalendar start, GregorianCalendar end) {
	//	return this.meetings;
	//}
	
}
