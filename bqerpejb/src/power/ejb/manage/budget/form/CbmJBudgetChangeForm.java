package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class CbmJBudgetChangeForm implements java.io.Serializable {
	/** 部门预算明细ID */
	private Long budgetItemId;
	/** 预算部门指标 */
	private Long centerItemId;
	/** 预算部门 */
	private Long centerId;
	/** 预算主题 */
	private Long topicId;
	/** 预算时间 */
	private String budgetTime;
	/** 指标 */
	private Long itemId;
	/** 审定预算 */
	private Double ensureBudget;

	/** 预算部门名称 */
	private String centerName;
	/** 预算主题名称 */
	private String topicName;
	/** 指标名称 */
	private String itemName;

	/** 变更单ID */
	private Long changeId;
	/** 原预算 */
	private Double originBudget;
	/** 预算变更 */
	private Double budgetChange;
	/** 现预算 */
	private Double newBudget;
	/** 变更理由 */
	private String changeReason;
	/** 变更人 */
	private String changeName;
	/** 变更时间 */
	private String changeDate;
	/** 变更状态 */
	private String changeStatus;

	/** 判断按钮的标识 */
	private String btnChange;
	/** 工作流编号 */
	private Long workFlowNo;

	public Long getBudgetItemId() {
		return budgetItemId;
	}

	public void setBudgetItemId(Long budgetItemId) {
		this.budgetItemId = budgetItemId;
	}

	public Long getCenterItemId() {
		return centerItemId;
	}

	public void setCenterItemId(Long centerItemId) {
		this.centerItemId = centerItemId;
	}

	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getBudgetTime() {
		return budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Double getEnsureBudget() {
		return ensureBudget;
	}

	public void setEnsureBudget(Double ensureBudget) {
		this.ensureBudget = ensureBudget;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getChangeId() {
		return changeId;
	}

	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}

	public Double getOriginBudget() {
		return originBudget;
	}

	public void setOriginBudget(Double originBudget) {
		this.originBudget = originBudget;
	}

	public Double getBudgetChange() {
		return budgetChange;
	}

	public void setBudgetChange(Double budgetChange) {
		this.budgetChange = budgetChange;
	}

	public Double getNewBudget() {
		return newBudget;
	}

	public void setNewBudget(Double newBudget) {
		this.newBudget = newBudget;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	public String getChangeName() {
		return changeName;
	}

	public void setChangeName(String changeName) {
		this.changeName = changeName;
	}

	public String getBtnChange() {
		return btnChange;
	}

	public void setBtnChange(String btnChange) {
		this.btnChange = btnChange;
	}

	public Long getWorkFlowNo() {
		return workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
}
