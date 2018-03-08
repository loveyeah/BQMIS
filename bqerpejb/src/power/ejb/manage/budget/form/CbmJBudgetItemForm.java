package power.ejb.manage.budget.form;

public class CbmJBudgetItemForm implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 98645645216L;
	private long budgetItemId;
	private String itemAlias;
	// private long ensureBudget;
	// private long factHappen;

	private Long centerItemId;
	private Long centerId;
	private Long topicId;
	private String budgetTime;
	private Long itemId;
	private Long budgetMakeId;
	private Double forecastBudget;
	private Double adviceBudget;
	private Double adjustBudget;
	private Double budgetAdd;
	private Double budgetChange;
	private Double judgeBudget;
	private Double ensureBudget;
	private Double factHappen;
	private Double financeHappen;
	private String budgetBasis;
	// private String budgetStatus;
	private String enterpriseCode;
	private String deptName;
	private String unitName;
	private String centerCode;
	private String centerName;
	private String itemCode;
	private String itemName;

	private String deptItemAlias;
	private String dataSource;
	private String dispalyNo;

	// add by drdu 090821
	private Long changeId;
	private Long changeWorkFlowNo;
	private String changeStatus;
	private String changeDate;

	// add by fyyang 090819
	private Long unitId; // 单位id
	private String makeStatus; // 编制单状态
	private Long workFlowNo;
	private String financeItem; // 财务科目编码
	private String topicName; // 主题名称
	private String makeBy;// 编制人
	private String makeByName;// 编制人姓名
	private String makeDate;// 编制日期

	// add by liuyi 090819 预算汇总ID
	private Long budgetGatherId;
	// 汇总工作流序号
	private Long gatherWorkFlowNo;
	// 汇总状态
	private String gatherStatus;

	// add by ltong 20100504
	private Long centerTopicId;// 预算部门主题ID
	private String financeName;// 财务科目名称

	public String getDeptItemAlias() {
		return deptItemAlias;
	}

	public void setDeptItemAlias(String deptItemAlias) {
		this.deptItemAlias = deptItemAlias;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDispalyNo() {
		return dispalyNo;
	}

	public void setDispalyNo(String dispalyNo) {
		this.dispalyNo = dispalyNo;
	}

	public long getBudgetItemId() {
		return budgetItemId;
	}

	public void setBudgetItemId(long budgetItemId) {
		this.budgetItemId = budgetItemId;
	}

	public String getItemAlias() {
		return itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
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

	public Long getBudgetMakeId() {
		return budgetMakeId;
	}

	public void setBudgetMakeId(Long budgetMakeId) {
		this.budgetMakeId = budgetMakeId;
	}

	public Double getForecastBudget() {
		return forecastBudget;
	}

	public void setForecastBudget(Double forecastBudget) {
		this.forecastBudget = forecastBudget;
	}

	public Double getAdviceBudget() {
		return adviceBudget;
	}

	public void setAdviceBudget(Double adviceBudget) {
		this.adviceBudget = adviceBudget;
	}

	public Double getAdjustBudget() {
		return adjustBudget;
	}

	public void setAdjustBudget(Double adjustBudget) {
		this.adjustBudget = adjustBudget;
	}

	public Double getBudgetAdd() {
		return budgetAdd;
	}

	public void setBudgetAdd(Double budgetAdd) {
		this.budgetAdd = budgetAdd;
	}

	public Double getBudgetChange() {
		return budgetChange;
	}

	public void setBudgetChange(Double budgetChange) {
		this.budgetChange = budgetChange;
	}

	public Double getJudgeBudget() {
		return judgeBudget;
	}

	public void setJudgeBudget(Double judgeBudget) {
		this.judgeBudget = judgeBudget;
	}

	public Double getEnsureBudget() {
		return ensureBudget;
	}

	public void setEnsureBudget(Double ensureBudget) {
		this.ensureBudget = ensureBudget;
	}

	public Double getFactHappen() {
		return factHappen;
	}

	public void setFactHappen(Double factHappen) {
		this.factHappen = factHappen;
	}

	public Double getFinanceHappen() {
		return financeHappen;
	}

	public void setFinanceHappen(Double financeHappen) {
		this.financeHappen = financeHappen;
	}

	public String getBudgetBasis() {
		return budgetBasis;
	}

	public void setBudgetBasis(String budgetBasis) {
		this.budgetBasis = budgetBasis;
	}

	// public String getBudgetStatus() {
	// return budgetStatus;
	// }
	//
	// public void setBudgetStatus(String budgetStatus) {
	// this.budgetStatus = budgetStatus;
	// }

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Long getChangeId() {
		return changeId;
	}

	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}

	public String getMakeStatus() {
		return makeStatus;
	}

	public void setMakeStatus(String makeStatus) {
		this.makeStatus = makeStatus;
	}

	public Long getWorkFlowNo() {
		return workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	public String getFinanceItem() {
		return financeItem;
	}

	public void setFinanceItem(String financeItem) {
		this.financeItem = financeItem;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getMakeBy() {
		return makeBy;
	}

	public void setMakeBy(String makeBy) {
		this.makeBy = makeBy;
	}

	public String getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}

	public String getMakeByName() {
		return makeByName;
	}

	public void setMakeByName(String makeByName) {
		this.makeByName = makeByName;
	}

	public Long getBudgetGatherId() {
		return budgetGatherId;
	}

	public void setBudgetGatherId(Long budgetGatherId) {
		this.budgetGatherId = budgetGatherId;
	}

	public Long getGatherWorkFlowNo() {
		return gatherWorkFlowNo;
	}

	public void setGatherWorkFlowNo(Long gatherWorkFlowNo) {
		this.gatherWorkFlowNo = gatherWorkFlowNo;
	}

	public String getGatherStatus() {
		return gatherStatus;
	}

	public void setGatherStatus(String gatherStatus) {
		this.gatherStatus = gatherStatus;
	}

	public Long getChangeWorkFlowNo() {
		return changeWorkFlowNo;
	}

	public void setChangeWorkFlowNo(Long changeWorkFlowNo) {
		this.changeWorkFlowNo = changeWorkFlowNo;
	}

	public String getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @return the centerTopicId
	 */
	public Long getCenterTopicId() {
		return centerTopicId;
	}

	/**
	 * @param centerTopicId
	 *            the centerTopicId to set
	 */
	public void setCenterTopicId(Long centerTopicId) {
		this.centerTopicId = centerTopicId;
	}

	/**
	 * @return the financeName
	 */
	public String getFinanceName() {
		return financeName;
	}

	/**
	 * @param financeName
	 *            the financeName to set
	 */
	public void setFinanceName(String financeName) {
		this.financeName = financeName;
	}

}
