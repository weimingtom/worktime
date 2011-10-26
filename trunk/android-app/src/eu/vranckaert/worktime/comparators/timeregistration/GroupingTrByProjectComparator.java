package eu.vranckaert.worktime.comparators.timeregistration;

import eu.vranckaert.worktime.model.TimeRegistration;

import java.util.Calendar;
import java.util.Comparator;

public class GroupingTrByProjectComparator implements Comparator<TimeRegistration> {

	public int compare(TimeRegistration tr0, TimeRegistration tr1) {
		int compareResult = 0;
		
		compareResult = tr0.getTask().getProject().getName().compareTo(tr1.getTask().getProject().getName());
		
		if (compareResult == 0) {
			compareResult = tr0.getTask().getName().compareTo(tr1.getTask().getName());
			
			if (compareResult == 0) {
				Calendar startTime0 = Calendar.getInstance();
				startTime0.setTime(tr0.getStartTime());
				startTime0.set(Calendar.HOUR, 0);
				startTime0.set(Calendar.HOUR_OF_DAY, 0);
				startTime0.set(Calendar.MINUTE, 0);
				startTime0.set(Calendar.SECOND, 0);
				startTime0.set(Calendar.MILLISECOND, 0);
				
				Calendar startTime1 = Calendar.getInstance();
				startTime1.setTime(tr1.getStartTime());
				startTime1.set(Calendar.HOUR, 0);
				startTime1.set(Calendar.HOUR_OF_DAY, 0);
				startTime1.set(Calendar.MINUTE, 0);
				startTime1.set(Calendar.SECOND, 0);
				startTime1.set(Calendar.MILLISECOND, 0);
				
				compareResult = startTime0.compareTo(startTime1);
			}
		}
		
		return compareResult;
	}

}
