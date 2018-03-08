package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCDynamicCheckMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_C_DYNAMIC_CHECK_MAIN")
public class SpCDynamicCheckMain implements java.io.Serializable {

	// Fields

	private Long mainId;
	private String year;
	private String season;
	private String isUse;
	private String enterpriseCode;
	private String entryBy;
	private Date entryDate;
	private String status;

	// Constructors

	/** default constructor */
	public SpCDynamicCheckMain() {
	}

	/** minimal constructor */
	public SpCDynamicCheckMain(Long mainId) {
		this.mainId = mainId;
	}

	/** full constructor */
	public SpCDynamicCheckMain(Long mainId, String year, String season,
			String isUse, String enterpriseCode, String entryBy,
			Date entryDate, String status) {
		this.mainId = mainId;
		this.year = year;
		this.season = season;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.status = status;
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

	@Column(name = "YEAR", length = 4)
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "SEASON", length = 1)
	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
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

	@Column(name = "ENTRY_BY", length = 20)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}