package power.ejb.manage.exam.form;

@SuppressWarnings("serial")
public class BpApproveForm implements java.io.Serializable{
	
	private Long declareId;
	private String yearMonth;
	private Long workflowNo;
	private String status;
	private String lastModifyName;
	private String lastModifyDate;
	
	public Long getDeclareId() {
		return declareId;
	}
	public void setDeclareId(Long declareId) {
		this.declareId = declareId;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Long getWorkflowNo() {
		return workflowNo;
	}
	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastModifyName() {
		return lastModifyName;
	}
	public void setLastModifyName(String lastModifyName) {
		this.lastModifyName = lastModifyName;
	}
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
}
