package power.ejb.resource.form;

public class CostFromInfo  implements java.io.Serializable
{
	private String deptCode;
	private String deptName;
	private String  costCode;
	private String costName;
	private Double  costQty;
	private Double  budgect ;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCostCode() {
		return costCode;
	}
	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public Double getCostQty() {
		return costQty;
	}
	public void setCostQty(Double costQty) {
		this.costQty = costQty;
	}
	public Double getBudgect() {
		return budgect;
	}
	public void setBudgect(Double budgect) {
		this.budgect = budgect;
	}
	}