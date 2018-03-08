package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJAntiAccidentCheckup;

public class CheckupForm
{
	private SpJAntiAccidentCheckup check;
	
	//专业编码
	private String specialCode;
	// 专业名称
	private String specialName;
	//检查人
	private String checkName;
	
	// 检查时间
	private String checkDate;
	
	// 审核人
	private String approveName;
	
	// 审核时间
	private String approveDate;
	
	// 修改人
	private String modifyName;
	
	// 修改时间
	private String modifyDate;

	// 措施名称
	private String measureName;
	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public SpJAntiAccidentCheckup getCheck() {
		return check;
	}

	public void setCheck(SpJAntiAccidentCheckup check) {
		this.check = check;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getSpecialCode() {
		return specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}

	public String getSpecialName() {
		return specialName;
	}

	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	
}