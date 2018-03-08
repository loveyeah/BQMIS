package power.ejb.manage.contract.form;



@SuppressWarnings("serial")
public class ClientCharacterForm implements java.io.Serializable{
	private Long characterId;
	private String characterCode;
	private String characterName;
	private Long approveFlag;
	private String memo;
	private String lastModifiedBy;
	private String lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedName;
	public Long getCharacterId() {
		return characterId;
	}
	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}
	public String getCharacterCode() {
		return characterCode;
	}
	public void setCharacterCode(String characterCode) {
		this.characterCode = characterCode;
	}
	public String getCharacterName() {
		return characterName;
	}
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
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
