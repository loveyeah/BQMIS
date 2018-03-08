package power.ejb.run.securityproduction.form;

import java.util.Date;

public class SpJEmpForm
{
	
	private Long empId;
	private String empName;
	private String empDuty;
	private String modifyby;
	private Date modifyDate;
	private String isUse;
	private String enterpriseCode;
	
	private String modifyName;
	private String modifyDateString;

	public String getModifyDateString() {
		return modifyDateString;
	}

	public void setModifyDateString(String modifyDateString) {
		this.modifyDateString = modifyDateString;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}


	public String getEmpDuty() {
		return empDuty;
	}

	public void setEmpDuty(String empDuty) {
		this.empDuty = empDuty;
	}

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
}