package power.ejb.workticket.form;

import java.util.Date;

@SuppressWarnings("serial")
public class WorkticketCDanger implements java.io.Serializable{
	private String dangerId;
	private String dangerName;
	private String dangerTypeId;
	private String PDangerId;
	private String orderBy;
	private String modifyBy;
	private String modifyDate;
	private String enterpriseCode;
	private String isUse;
	private String typeName;
	public String getDangerId() {
		return dangerId;
	}
	public void setDangerId(String dangerId) {
		this.dangerId = dangerId;
	}
	public String getDangerName() {
		return dangerName;
	}
	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
	}
	public String getDangerTypeId() {
		return dangerTypeId;
	}
	public void setDangerTypeId(String dangerTypeId) {
		this.dangerTypeId = dangerTypeId;
	}
	public String getPDangerId() {
		return PDangerId;
	}
	public void setPDangerId(String dangerId) {
		PDangerId = dangerId;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
