/**
 * 工种设置
 */
package power.web.hr.worktype;

/**
 * @author Administrator
 * 
 */
public class WorkTypeInfo {
	private Long typeOfWorkId;
	private String typeOfWorkName;
	private String typeOfWorkType;
	private String isUse;
	private String retrieveCode;
	
	public Long getTypeOfWorkId() {
		return typeOfWorkId;
	}
	public void setTypeOfWorkId(Long typeOfWorkId) {
		this.typeOfWorkId = typeOfWorkId;
	}
	public String getTypeOfWorkName() {
		return typeOfWorkName;
	}
	public void setTypeOfWorkName(String typeOfWorkName) {
		this.typeOfWorkName = typeOfWorkName;
	}
	public String getTypeOfWorkType() {
		return typeOfWorkType;
	}
	public void setTypeOfWorkType(String typeOfWorkType) {
		this.typeOfWorkType = typeOfWorkType;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	

}



