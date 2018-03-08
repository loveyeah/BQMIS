package power.ejb.manage.contract.form;

@SuppressWarnings("serial")
public class ClientTypeForm implements java.io.Serializable{
	private Long clientTypeId;
	private String clientTypeCode;
	private String typeName;
	private Long approveFlag;
	private String memo;
	private String lastModifiedBy;
	private String lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedName;
	public Long getClientTypeId() {
		return clientTypeId;
	}
	public void setClientTypeId(Long clientTypeId) {
		this.clientTypeId = clientTypeId;
	}
	public String getClientTypeCode() {
		return clientTypeCode;
	}
	public void setClientTypeCode(String clientTypeCode) {
		this.clientTypeCode = clientTypeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Long getApproveFlag() {
		return approveFlag;
	}
	public void setApproveFlag(Long approveFlag) {
		this.approveFlag = approveFlag;
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
	public String getLastModifiedName() {
		return lastModifiedName;
	}
	public void setLastModifiedName(String lastModifiedName) {
		this.lastModifiedName = lastModifiedName;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
