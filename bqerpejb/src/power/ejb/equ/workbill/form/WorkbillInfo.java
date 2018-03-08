package power.ejb.equ.workbill.form;

import power.ejb.equ.workbill.EquJWo;

public class WorkbillInfo implements java.io.Serializable{
	private EquJWo model;
	private String repairdepartmentName;
	private String professionName;
	private String requiremanName;
	private String workchargeName;
    private String workorderStatusFlag;
	private String workorderTypeFlag;
	private String equipmentName;
	private String failureCode;
	private String failureContent;
	public String getFailureContent() {
		return failureContent;
	}
	public void setFailureContent(String failureContent) {
		this.failureContent = failureContent;
	}
	public String getFailureCode() {
		return failureCode;
	}
	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}
	public EquJWo getModel() {
		return model;
	}
	public void setModel(EquJWo model) {
		this.model = model;
	}
	public String getRepairdepartmentName() {
		return repairdepartmentName;
	}
	public void setRepairdepartmentName(String repairdepartmentName) {
		this.repairdepartmentName = repairdepartmentName;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getRequiremanName() {
		return requiremanName;
	}
	public void setRequiremanName(String requiremanName) {
		this.requiremanName = requiremanName;
	}
	public String getWorkchargeName() {
		return workchargeName;
	}
	public void setWorkchargeName(String workchargeName) {
		this.workchargeName = workchargeName;
	}
	public String getWorkorderStatusFlag() {
		return workorderStatusFlag;
	}
	public void setWorkorderStatusFlag(String workorderStatusFlag) {
		this.workorderStatusFlag = workorderStatusFlag;
	}
	public String getWorkorderTypeFlag() {
		return workorderTypeFlag;
	}
	public void setWorkorderTypeFlag(String workorderTypeFlag) {
		this.workorderTypeFlag = workorderTypeFlag;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
	
}
