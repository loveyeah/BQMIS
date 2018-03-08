package power.ejb.manage.plan.trainplan.form;

import power.ejb.manage.plan.trainplan.BpJTrainingDetail;;

@SuppressWarnings("serial")
public class BpJTrainingTypeForm implements java.io.Serializable {

	private BpJTrainingDetail trainDetail;
	
	private String billName;
	private String chargeName;
	private String deptName;
	private String deptCode;
	private String planTypeName;
	private String trainingTypeName;
    private String fillDate;
    private String workflowNo;
    private String workflowStatus;
    private String reportTime;
    private String reportByName;
    private String reportBy;
    private String trainingMonth;
    //汇总表Id
   private String gatherId;
   
   private String fillBy;
   private String fillName;
   
	public String getFillBy() {
	return fillBy;
}
public void setFillBy(String fillBy) {
	this.fillBy = fillBy;
}
public String getFillName() {
	return fillName;
}
public void setFillName(String fillName) {
	this.fillName = fillName;
}
	public BpJTrainingDetail getTrainDetail() {
		return trainDetail;
	}
	public void setTrainDetail(BpJTrainingDetail trainDetail) {
		this.trainDetail = trainDetail;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPlanTypeName() {
		return planTypeName;
	}
	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}
	public String getTrainingTypeName() {
		return trainingTypeName;
	}
	public void setTrainingTypeName(String trainingTypeName) {
		this.trainingTypeName = trainingTypeName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	public String getWorkflowNo() {
		return workflowNo;
	}
	public void setWorkflowNo(String workflowNo) {
		this.workflowNo = workflowNo;
	}
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getReportByName() {
		return reportByName;
	}
	public void setReportByName(String reportByName) {
		this.reportByName = reportByName;
	}
	public String getReportBy() {
		return reportBy;
	}
	public void setReportBy(String reportBy) {
		this.reportBy = reportBy;
	}
	public String getGatherId() {
		return gatherId;
	}
	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}
	public String getTrainingMonth() {
		return trainingMonth;
	}
	public void setTrainingMonth(String trainingMonth) {
		this.trainingMonth = trainingMonth;
	}

}