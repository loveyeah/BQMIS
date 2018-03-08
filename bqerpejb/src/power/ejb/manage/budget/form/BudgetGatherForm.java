package power.ejb.manage.budget.form;

import java.util.Date;

public class BudgetGatherForm implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9089767668L;
	private Long budgetGatherId;
	private Long topicId;
	private String budgetTime;
	private String gatherBy;
	private Date gatherDate;
	private Long workFlowNo;
	private String gatherStatus;
	private String enterpriseCode;

	private Long centerId;
	private Long itemId;
	private Double adviceBudget;
	private String dataSource;

	// add by fyyang 090822
	private String topicName;
	private String gatherByName;
	private String strGatherDate;

	// add by liuyi
	private Long budgetMakeId;

	public Long getBudgetMakeId() {
		return budgetMakeId;
	}

	public void setBudgetMakeId(Long budgetMakeId) {
		this.budgetMakeId = budgetMakeId;
	}

	public Long getBudgetGatherId() {
		return budgetGatherId;
	}

	public void setBudgetGatherId(Long budgetGatherId) {
		this.budgetGatherId = budgetGatherId;
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

	public String getGatherBy() {
		return gatherBy;
	}

	public void setGatherBy(String gatherBy) {
		this.gatherBy = gatherBy;
	}

	public Date getGatherDate() {
		return gatherDate;
	}

	public void setGatherDate(Date gatherDate) {
		this.gatherDate = gatherDate;
	}

	public Long getWorkFlowNo() {
		return workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	public String getGatherStatus() {
		return gatherStatus;
	}

	public void setGatherStatus(String gatherStatus) {
		this.gatherStatus = gatherStatus;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Double getAdviceBudget() {
		return adviceBudget;
	}

	public void setAdviceBudget(Double adviceBudget) {
		this.adviceBudget = adviceBudget;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getGatherByName() {
		return gatherByName;
	}

	public void setGatherByName(String gatherByName) {
		this.gatherByName = gatherByName;
	}

	public String getStrGatherDate() {
		return strGatherDate;
	}

	public void setStrGatherDate(String strGatherDate) {
		this.strGatherDate = strGatherDate;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}