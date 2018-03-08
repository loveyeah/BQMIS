package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJAntiAccident;
import power.ejb.run.securityproduction.SpJAntiAccidentAmend;

public class AmendForm
{
	private SpJAntiAccidentAmend amend;
	
	//计划完成时间
	private String planFinishDate;
	
	//整改完成时间
	private String amendFinishDate;
	
	//整改责任部门
	private String chargeDeptName;
	
	// 整改责任人
	private String chargeName;
	
	// 整改监督部门
	private String superviseDeptName;
	
	// 整改监督人
	private String superviseName;
	
	//修改人
	private String modifyName;
	
	//修改时间
	private String modifyDate;
	
	//项目类别
	private String proType;
	
	//专业编码
	private String specialCode;
	// 专业名称
	private String specialName;

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

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public SpJAntiAccidentAmend getAmend() {
		return amend;
	}

	public void setAmend(SpJAntiAccidentAmend amend) {
		this.amend = amend;
	}

	public String getPlanFinishDate() {
		return planFinishDate;
	}

	public void setPlanFinishDate(String planFinishDate) {
		this.planFinishDate = planFinishDate;
	}

	public String getAmendFinishDate() {
		return amendFinishDate;
	}

	public void setAmendFinishDate(String amendFinishDate) {
		this.amendFinishDate = amendFinishDate;
	}

	public String getChargeDeptName() {
		return chargeDeptName;
	}

	public void setChargeDeptName(String chargeDeptName) {
		this.chargeDeptName = chargeDeptName;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getSuperviseDeptName() {
		return superviseDeptName;
	}

	public void setSuperviseDeptName(String superviseDeptName) {
		this.superviseDeptName = superviseDeptName;
	}

	public String getSuperviseName() {
		return superviseName;
	}

	public void setSuperviseName(String superviseName) {
		this.superviseName = superviseName;
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
}