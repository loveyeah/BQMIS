package power.ejb.manage.contract.form;



@SuppressWarnings("serial")
public class ClientImportForm implements java.io.Serializable{
	private Long idimportId;
	private String importCode;
	private String importName;
	private Long approveFlag;
	private String memo;
	private String lastModifiedBy;
	private String lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedName;
	public Long getIdimportId() {
		return idimportId;
	}
	public void setIdimportId(Long idimportId) {
		this.idimportId = idimportId;
	}
	public String getImportCode() {
		return importCode;
	}
	public void setImportCode(String importCode) {
		this.importCode = importCode;
	}
	public String getImportName() {
		return importName;
	}
	public void setImportName(String importName) {
		this.importName = importName;
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
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
}
