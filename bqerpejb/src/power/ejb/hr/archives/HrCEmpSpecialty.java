package power.ejb.hr.archives;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCEmpSpecialty entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_EMP_SPECIALTY")
public class HrCEmpSpecialty implements java.io.Serializable {

	// Fields

	private Long specialtyId;
	private Long empId;
	private String specialtyName;
	private String specialtyLevel;
	private String awardUnit;
	private Date awardDate;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCEmpSpecialty() {
	}

	/** minimal constructor */
	public HrCEmpSpecialty(Long specialtyId) {
		this.specialtyId = specialtyId;
	}

	/** full constructor */
	public HrCEmpSpecialty(Long specialtyId, Long empId, String specialtyName,
			String specialtyLevel, String awardUnit, Date awardDate,
			String memo, String isUse, String enterpriseCode) {
		this.specialtyId = specialtyId;
		this.empId = empId;
		this.specialtyName = specialtyName;
		this.specialtyLevel = specialtyLevel;
		this.awardUnit = awardUnit;
		this.awardDate = awardDate;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SPECIALTY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(Long specialtyId) {
		this.specialtyId = specialtyId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "SPECIALTY_NAME", length = 50)
	public String getSpecialtyName() {
		return this.specialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	@Column(name = "SPECIALTY_LEVEL", length = 10)
	public String getSpecialtyLevel() {
		return this.specialtyLevel;
	}

	public void setSpecialtyLevel(String specialtyLevel) {
		this.specialtyLevel = specialtyLevel;
	}

	@Column(name = "AWARD_UNIT", length = 100)
	public String getAwardUnit() {
		return this.awardUnit;
	}

	public void setAwardUnit(String awardUnit) {
		this.awardUnit = awardUnit;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AWARD_DATE", length = 7)
	public Date getAwardDate() {
		return this.awardDate;
	}

	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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