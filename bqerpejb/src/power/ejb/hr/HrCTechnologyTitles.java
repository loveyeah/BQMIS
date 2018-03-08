package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCTechnologyTitles entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_TECHNOLOGY_TITLES")
public class HrCTechnologyTitles implements java.io.Serializable {

	// Fields

	private Long technologyTitlesId;
	private Long technologyTitlesTypeId;
	private String technologyTitlesName;
	private String technologyTitlesLevel;
	private String isUse;
	private String retrieveCode;

	// Constructors

	/** default constructor */
	public HrCTechnologyTitles() {
	}

	/** minimal constructor */
	public HrCTechnologyTitles(Long technologyTitlesId) {
		this.technologyTitlesId = technologyTitlesId;
	}

	/** full constructor */
	public HrCTechnologyTitles(Long technologyTitlesId,
			Long technologyTitlesTypeId, String technologyTitlesName,
			String technologyTitlesLevel, String isUse, String retrieveCode) {
		this.technologyTitlesId = technologyTitlesId;
		this.technologyTitlesTypeId = technologyTitlesTypeId;
		this.technologyTitlesName = technologyTitlesName;
		this.technologyTitlesLevel = technologyTitlesLevel;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "TECHNOLOGY_TITLES_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTechnologyTitlesId() {
		return this.technologyTitlesId;
	}

	public void setTechnologyTitlesId(Long technologyTitlesId) {
		this.technologyTitlesId = technologyTitlesId;
	}

	@Column(name = "TECHNOLOGY_TITLES_TYPE_ID", precision = 10, scale = 0)
	public Long getTechnologyTitlesTypeId() {
		return this.technologyTitlesTypeId;
	}

	public void setTechnologyTitlesTypeId(Long technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}

	@Column(name = "TECHNOLOGY_TITLES_NAME", length = 100)
	public String getTechnologyTitlesName() {
		return this.technologyTitlesName;
	}

	public void setTechnologyTitlesName(String technologyTitlesName) {
		this.technologyTitlesName = technologyTitlesName;
	}

	@Column(name = "TECHNOLOGY_TITLES_LEVEL", length = 1)
	public String getTechnologyTitlesLevel() {
		return this.technologyTitlesLevel;
	}

	public void setTechnologyTitlesLevel(String technologyTitlesLevel) {
		this.technologyTitlesLevel = technologyTitlesLevel;
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