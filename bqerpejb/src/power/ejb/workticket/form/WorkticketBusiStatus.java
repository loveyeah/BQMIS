package power.ejb.workticket.form;

public class WorkticketBusiStatus implements java.io.Serializable{
	private Long workticketStausId;
	private String workticketStatusName;
	public Long getWorkticketStausId() {
		return workticketStausId;
	}
	public void setWorkticketStausId(Long workticketStausId) {
		this.workticketStausId = workticketStausId;
	}
	public String getWorkticketStatusName() {
		return workticketStatusName;
	}
	public void setWorkticketStatusName(String workticketStatusName) {
		this.workticketStatusName = workticketStatusName;
	}

}
