package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCTypeOfWork entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_TYPE_OF_WORK", schema = "power")
public class HrCTypeOfWork implements java.io.Serializable {

	// Fields

	private Long typeOfWorkId;
	private String typeOfWorkName;
	private String typeOfWorkType;
	private String isUse;
	private String retrieveCode;

	// Constructors

	/** default constructor */
	public HrCTypeOfWork() {
	}

	/** minimal constructor */
	public HrCTypeOfWork(Long typeOfWorkId) {
		this.typeOfWorkId = typeOfWorkId;
	}

	/** full constructor */
	public HrCTypeOfWork(Long typeOfWorkId, String typeOfWorkName,
			String typeOfWorkType, String isUse, String retrieveCode) {
		this.typeOfWorkId = typeOfWorkId;
		this.typeOfWorkName = typeOfWorkName;
		this.typeOfWorkType = typeOfWorkType;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "TYPE_OF_WORK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTypeOfWorkId() {
		return this.typeOfWorkId;
	}

	public void setTypeOfWorkId(Long typeOfWorkId) {
		this.typeOfWorkId = typeOfWorkId;
	}

	@Column(name = "TYPE_OF_WORK_NAME", length = 100)
	public String getTypeOfWorkName() {
		return this.typeOfWorkName;
	}

	public void setTypeOfWorkName(String typeOfWorkName) {
		this.typeOfWorkName = typeOfWorkName;
	}

	@Column(name = "TYPE_OF_WORK_TYPE", length = 1)
	public String getTypeOfWorkType() {
		return this.typeOfWorkType;
	}

	public void setTypeOfWorkType(String typeOfWorkType) {
		this.typeOfWorkType = typeOfWorkType;
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