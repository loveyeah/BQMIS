package power.ejb.manage.contract.form;

import java.util.Date;

import power.ejb.manage.contract.ConCConType;

public class ContypeInfo implements java.io.Serializable{
	private Long contypeId;
	private Long PContypeId;
	private String conTypeDesc;
	private String markCode;
	private String memo;
	private String lastModifiedBy;
	private String enterpriseCode;
	private String isUse;

	private  String lastModifiedDate;
	private String lastModifiedName;
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedName() {
		return lastModifiedName;
	}
	public void setLastModifiedName(String lastModifiedName) {
		this.lastModifiedName = lastModifiedName;
	}
	public Long getContypeId() {
		return contypeId;
	}
	public void setContypeId(Long contypeId) {
		this.contypeId = contypeId;
	}
	public Long getPContypeId() {
		return PContypeId;
	}
	public void setPContypeId(Long contypeId) {
		PContypeId = contypeId;
	}
	public String getConTypeDesc() {
		return conTypeDesc;
	}
	public void setConTypeDesc(String conTypeDesc) {
		this.conTypeDesc = conTypeDesc;
	}
	public String getMarkCode() {
		return markCode;
	}
	public void setMarkCode(String markCode) {
		this.markCode = markCode;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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

}
