package power.ejb.hr.form;

public class Parameter {
	private String parmNo;
	private String parmName;
	private String parmValue;
	private String enterpriseCode;
	public String getParmNo() {
		return parmNo;
	}
	public void setParmNo(String parmNo) {
		this.parmNo = parmNo;
	}
	public String getParmName() {
		return parmName;
	}
	public void setParmName(String parmName) {
		this.parmName = parmName;
	}
	public String getParmValue() {
		return parmValue;
	}
	public void setParmValue(String parmValue) {
		this.parmValue = parmValue;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}
