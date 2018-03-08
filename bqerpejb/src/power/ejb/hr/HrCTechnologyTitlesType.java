package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCTechnologyTitlesType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_TECHNOLOGY_TITLES_TYPE", schema = "power")
public class HrCTechnologyTitlesType implements java.io.Serializable {

	// Fields

	private Long technologyTitlesTypeId;
	private String technologyTitlesTypeName;
	private String isUse;
	private String retrieveCode;

	// Constructors

	/** default constructor */
	public HrCTechnologyTitlesType() {
	}

	/** minimal constructor */
	public HrCTechnologyTitlesType(Long technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}

	/** full constructor */
	public HrCTechnologyTitlesType(Long technologyTitlesTypeId,
			String technologyTitlesTypeName, String isUse, String retrieveCode) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
		this.technologyTitlesTypeName = technologyTitlesTypeName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "TECHNOLOGY_TITLES_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTechnologyTitlesTypeId() {
		return this.technologyTitlesTypeId;
	}

	public void setTechnologyTitlesTypeId(Long technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}

	@Column(name = "TECHNOLOGY_TITLES_TYPE_NAME", length = 100)
	public String getTechnologyTitlesTypeName() {
		return this.technologyTitlesTypeName;
	}

	public void setTechnologyTitlesTypeName(String technologyTitlesTypeName) {
		this.technologyTitlesTypeName = technologyTitlesTypeName;
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