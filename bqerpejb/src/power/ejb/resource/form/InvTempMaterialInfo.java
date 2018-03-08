package power.ejb.resource.form;

import power.ejb.resource.InvCTempMaterial;

@SuppressWarnings("serial")
public class InvTempMaterialInfo implements java.io.Serializable{

	private InvCTempMaterial temp;
	/** 上次修改人*/
	private String modifyName;
	/** 上次修改日期*/
	private String modifyDate;
	/** 核准日期*/
	private String checkDate;
	/** 核准人*/
	private String checkName;
	/** 审核人*/
	private String approveName;
	/** 审核日期*/
	private String approveDate;
	/** 物料分类名称*/
	private String maertialClassName;
	/** 物料ID  add by drdu 091026*/
	private Long materialId;
	/** 计量单位  add by drdu 091026*/
	private String unitName;
	
	public InvCTempMaterial getTemp() {
		return temp;
	}
	public void setTemp(InvCTempMaterial temp) {
		this.temp = temp;
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
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
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
	public String getMaertialClassName() {
		return maertialClassName;
	}
	public void setMaertialClassName(String maertialClassName) {
		this.maertialClassName = maertialClassName;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
