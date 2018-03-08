package power.ejb.manage.budget.form;

import power.ejb.manage.budget.CbmJMasterItem;

public class ExeControlForm {
	/** 预算执行控制 */
	private CbmJMasterItem cjm;
	/** 预算部门编码 */
	private String centerCode;
	/** 预算部门名称 */
	private String centerName;
	/** 预算指标编码 */
	private String itemCode;
	/** 预算指标名称 */
	private String itemName;
	// /** 预算时间 */
	// private String budgetTime;
	/** 填写人姓名 */
	private String fillName;
	/** 填写时间 */
	private String fillTime;

	private Long budgetMakeId;

	private Double adviceBudget; // 预算值 add by fyyang 090823

	public CbmJMasterItem getCjm() {
		return cjm;
	}

	public void setCjm(CbmJMasterItem cjm) {
		this.cjm = cjm;
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

	public String getFillName() {
		return fillName;
	}

	public void setFillName(String fillName) {
		this.fillName = fillName;
	}

	public String getFillTime() {
		return fillTime;
	}

	public void setFillTime(String fillTime) {
		this.fillTime = fillTime;
	}

	public Double getAdviceBudget() {
		return adviceBudget;
	}

	public void setAdviceBudget(Double adviceBudget) {
		this.adviceBudget = adviceBudget;
	}

	public Long getBudgetMakeId() {
		return budgetMakeId;
	}

	public void setBudgetMakeId(Long budgetMakeId) {
		this.budgetMakeId = budgetMakeId;
	}
}