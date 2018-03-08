package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

 
@Entity
@Table(name = "HR_C_ASSISTANT_MANAGER_UNITS")
public class HrCAssistantManagerUnits implements java.io.Serializable {
 
	private Long assistantManagerUnitsId;
	private String assistantManagerUnitsName;
	private String isUse;
	private String retrieveCode; 
	public HrCAssistantManagerUnits() {
	}

	/** minimal constructor */
	public HrCAssistantManagerUnits(Long assistantManagerUnitsId) {
		this.assistantManagerUnitsId = assistantManagerUnitsId;
	}

	/** full constructor */
	public HrCAssistantManagerUnits(Long assistantManagerUnitsId,
			String assistantManagerUnitsName, String isUse, String retrieveCode) {
		this.assistantManagerUnitsId = assistantManagerUnitsId;
		this.assistantManagerUnitsName = assistantManagerUnitsName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "ASSISTANT_MANAGER_UNITS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAssistantManagerUnitsId() {
		return this.assistantManagerUnitsId;
	}

	public void setAssistantManagerUnitsId(Long assistantManagerUnitsId) {
		this.assistantManagerUnitsId = assistantManagerUnitsId;
	}

	@Column(name = "ASSISTANT_MANAGER_UNITS_NAME", length = 100)
	public String getAssistantManagerUnitsName() {
		return this.assistantManagerUnitsName;
	}

	public void setAssistantManagerUnitsName(String assistantManagerUnitsName) {
		this.assistantManagerUnitsName = assistantManagerUnitsName;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

}