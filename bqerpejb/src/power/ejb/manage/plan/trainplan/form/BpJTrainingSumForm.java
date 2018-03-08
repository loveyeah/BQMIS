package power.ejb.manage.plan.trainplan.form;

@SuppressWarnings("serial")
public class BpJTrainingSumForm implements java.io.Serializable{
	
	private Long trainingMainId;
	private Long approvalId;
	private String trainingMonth;
	private String trainingDep;
	private String trainingDepName;
	private Long trainingTypeId;
	private String trainingTypeName;
	private Long trainingNumber;
	private Long finishNumber;
	private Long workflowStatus;
	private Long workflowNo;
	
	public Long getTrainingMainId() {
		return trainingMainId;
	}
	public void setTrainingMainId(Long trainingMainId) {
		this.trainingMainId = trainingMainId;
	}
	public Long getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}
	public String getTrainingMonth() {
		return trainingMonth;
	}
	public void setTrainingMonth(String trainingMonth) {
		this.trainingMonth = trainingMonth;
	}
	public String getTrainingDep() {
		return trainingDep;
	}
	public void setTrainingDep(String trainingDep) {
		this.trainingDep = trainingDep;
	}
	public String getTrainingDepName() {
		return trainingDepName;
	}
	public void setTrainingDepName(String trainingDepName) {
		this.trainingDepName = trainingDepName;
	}
	public Long getTrainingTypeId() {
		return trainingTypeId;
	}
	public void setTrainingTypeId(Long trainingTypeId) {
		this.trainingTypeId = trainingTypeId;
	}
	public String getTrainingTypeName() {
		return trainingTypeName;
	}
	public void setTrainingTypeName(String trainingTypeName) {
		this.trainingTypeName = trainingTypeName;
	}
	public Long getTrainingNumber() {
		return trainingNumber;
	}
	public void setTrainingNumber(Long trainingNumber) {
		this.trainingNumber = trainingNumber;
	}
	public Long getFinishNumber() {
		return finishNumber;
	}
	public void setFinishNumber(Long finishNumber) {
		this.finishNumber = finishNumber;
	}
	public Long getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public Long getWorkflowNo() {
		return workflowNo;
	}
	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}
}
