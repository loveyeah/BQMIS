package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCSalaryType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_SALARY_TYPE")
public class HrCSalaryType implements java.io.Serializable {

	// Fields

	private Long salaryTypeId;
	private String salaryTypeName;
	private String isInput;
	private String isBasicData;
	private String isNeed;
	private String modifyBy;
	private Date modifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCSalaryType() {
	}

	/** minimal constructor */
	public HrCSalaryType(Long salaryTypeId) {
		this.salaryTypeId = salaryTypeId;
	}

	/** full constructor */
	public HrCSalaryType(Long salaryTypeId, String salaryTypeName,
			String isInput, String isBasicData, String isNeed, String modifyBy,
			Date modifyDate, String isUse, String enterpriseCode) {
		this.salaryTypeId = salaryTypeId;
		this.salaryTypeName = salaryTypeName;
		this.isInput = isInput;
		this.isBasicData = isBasicData;
		this.isNeed = isNeed;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SALARY_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalaryTypeId() {
		return this.salaryTypeId;
	}

	public void setSalaryTypeId(Long salaryTypeId) {
		this.salaryTypeId = salaryTypeId;
	}

	@Column(name = "SALARY_TYPE_NAME", length = 20)
	public String getSalaryTypeName() {
		return this.salaryTypeName;
	}

	public void setSalaryTypeName(String salaryTypeName) {
		this.salaryTypeName = salaryTypeName;
	}

	@Column(name = "IS_INPUT", length = 1)
	public String getIsInput() {
		return this.isInput;
	}

	public void setIsInput(String isInput) {
		this.isInput = isInput;
	}

	@Column(name = "IS_BASIC_DATA", length = 1)
	public String getIsBasicData() {
		return this.isBasicData;
	}

	public void setIsBasicData(String isBasicData) {
		this.isBasicData = isBasicData;
	}

	@Column(name = "IS_NEED", length = 1)
	public String getIsNeed() {
		return this.isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}