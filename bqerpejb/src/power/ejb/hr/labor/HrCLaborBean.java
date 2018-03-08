package power.ejb.hr.labor;

public class HrCLaborBean {

	private String changeDate;
	private String startDate;
	private String oldLbWorkId;
	private String oldLbWorkName;
	private String newLbWorkId;
	private String newLbWorkName;
	private String laborChangeId;
	
	public String getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getOldLbWorkId() {
		return oldLbWorkId;
	}
	public void setOldLbWorkId(String oldLbWorkId) {
		this.oldLbWorkId = oldLbWorkId;
	}
	public String getNewLbWorkId() {
		return newLbWorkId;
	}
	public void setNewLbWorkId(String newLbWorkId) {
		this.newLbWorkId = newLbWorkId;
	}
	public String getLaborChangeId() {
		return laborChangeId;
	}
	public void setLaborChangeId(String laborChangeId) {
		this.laborChangeId = laborChangeId;
	}
	public String getNewLbWorkName() {
		return newLbWorkName;
	}
	public void setNewLbWorkName(String newLbWorkName) {
		this.newLbWorkName = newLbWorkName;
	}
	public String getOldLbWorkName() {
		return oldLbWorkName;
	}
	public void setOldLbWorkName(String oldLbWorkName) {
		this.oldLbWorkName = oldLbWorkName;
	}
	
	
	
}
