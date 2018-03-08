package power.ejb.equ.standardpackage;

import java.util.Date;

@SuppressWarnings("serial")
public class WoJWorkorderForm implements java.io.Serializable {
	private WoJWorkorder order;
	private String startDate;
	private String endDate;
	private String supervisorName;
	public WoJWorkorder getOrder() {
		return order;
	}
	public void setOrder(WoJWorkorder order) {
		this.order = order;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
}
