package procyonScheduler.model;
import java.util.GregorianCalendar;
import java.util.ArrayList;

public class Schedule {
	int id;							// unique ID to reference object
	String name;					// non-unique user defined name 
	GregorianCalendar start;		// date and time to start schedule on
	GregorianCalendar end;			// date and time to end schedule on
	int blockSize;					// how many minutes each meeting is (may be 10, 15, 20, 30, 60)
	String secretCode;				// unique string required for organizer access
	GregorianCalendar created;		// date and time schedule was created
	ArrayList<Meeting> meetings;	// set of meetings within this schedule
	
}
