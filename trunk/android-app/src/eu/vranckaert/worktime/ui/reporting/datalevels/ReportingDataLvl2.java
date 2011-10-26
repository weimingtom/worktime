package eu.vranckaert.worktime.ui.reporting.datalevels;

import eu.vranckaert.worktime.model.TimeRegistration;

import java.util.ArrayList;
import java.util.List;

public class ReportingDataLvl2 {
	private Object key;
	private List<TimeRegistration> timeRegistrations;

	public ReportingDataLvl2() {
		super();
	}

	public ReportingDataLvl2(Object key) {
		super();
		this.key = key;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public List<TimeRegistration> getTimeRegistrations() {
		return timeRegistrations;
	}

	public void setTimeRegistrations(List<TimeRegistration> timeRegistrations) {
		this.timeRegistrations = timeRegistrations;
	}
	
	public void addTimeRegistration(TimeRegistration timeRegistration) {
		if (timeRegistrations == null) {
			timeRegistrations = new ArrayList<TimeRegistration>();
		}
		
		timeRegistrations.add(timeRegistration);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportingDataLvl2 other = (ReportingDataLvl2) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}
