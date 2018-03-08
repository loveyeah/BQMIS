package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCSsMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_SS_MAIN")
public class HrCSsMain implements java.io.Serializable {

	// Fields

	private Long mainId;
	private String mainYear;
	private String yearType;
	private String insuranceType;
	private String importBy;
	private Date importDate;
	private String isUse;
	private String enterpriseCode;
	private String annex;

	// Constructors
	

	/** default constructor */
	public HrCSsMain() {
	}

	/** minimal constructor */
	public HrCSsMain(Long mainId) {
		this.mainId = mainId;
	}

	/** full constructor */
	public HrCSsMain(Long mainId, String mainYear, String yearType,
			String insuranceType, String importBy, Date importDate,
			String isUse, String enterpriseCode,String annex) {
		this.mainId = mainId;
		this.mainYear = mainYear;
		this.yearType = yearType;
		this.insuranceType = insuranceType;
		this.importBy = importBy;
		this.importDate = importDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.annex=annex;
	}

	// Property accessors
	@Id
	@Column(name = "MAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMainId() {
		return this.mainId;
	}

	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}

	@Column(name = "MAIN_YEAR", length = 10)
	public String getMainYear() {
		return this.mainYear;
	}

	public void setMainYear(String mainYear) {
		this.mainYear = mainYear;
	}

	@Column(name = "YEAR_TYPE", length = 1)
	public String getYearType() {
		return this.yearType;
	}

	public void setYearType(String yearType) {
		this.yearType = yearType;
	}

	@Column(name = "INSURANCE_TYPE", length = 1)
	public String getInsuranceType() {
		return this.insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	@Column(name = "IMPORT_BY", length = 20)
	public String getImportBy() {
		return this.importBy;
	}

	public void setImportBy(String importBy) {
		this.importBy = importBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "IMPORT_DATE", length = 7)
	public Date getImportDate() {
		return this.importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
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
	@Column(name = "ANNEX", length = 200)
	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

}