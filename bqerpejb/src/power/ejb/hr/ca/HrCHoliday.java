package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCHoliday entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_HOLIDAY")
public class HrCHoliday implements java.io.Serializable {

	// Fields

	private Long holidayId;
	private Date holidayDate;
	private String holidayType;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCHoliday() {
	}

	/** minimal constructor */
	public HrCHoliday(Long holidayId, Date holidayDate, String holidayType) {
		this.holidayId = holidayId;
		this.holidayDate = holidayDate;
		this.holidayType = holidayType;
	}

	/** full constructor */
	public HrCHoliday(Long holidayId, Date holidayDate, String holidayType,
			String lastModifiyBy, Date lastModifiyDate, String isUse,
			String enterpriseCode) {
		this.holidayId = holidayId;
		this.holidayDate = holidayDate;
		this.holidayType = holidayType;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "HOLIDAY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getHolidayId() {
		return this.holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "HOLIDAY_DATE", nullable = false, length = 7)
	public Date getHolidayDate() {
		return this.holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	@Column(name = "HOLIDAY_TYPE", nullable = false, length = 1)
	public String getHolidayType() {
		return this.holidayType;
	}

	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}