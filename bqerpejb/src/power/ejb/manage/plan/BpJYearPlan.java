package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJYearPlan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_YEAR_PLAN")
public class BpJYearPlan implements java.io.Serializable {

	// Fields

	private Long yearPlanId;
	private String strYear;
	private String title;
	private String contentPath;
	private String memo;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public BpJYearPlan() {
	}

	/** minimal constructor */
	public BpJYearPlan(Long yearPlanId) {
		this.yearPlanId = yearPlanId;
	}

	/** full constructor */
	public BpJYearPlan(Long yearPlanId, String strYear, String title,
			String contentPath, String memo, String entryBy, Date entryDate,
			String enterpriseCode, String isUse) {
		this.yearPlanId = yearPlanId;
		this.strYear = strYear;
		this.title = title;
		this.contentPath = contentPath;
		this.memo = memo;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "YEAR_PLAN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getYearPlanId() {
		return this.yearPlanId;
	}

	public void setYearPlanId(Long yearPlanId) {
		this.yearPlanId = yearPlanId;
	}

	@Column(name = "STR_YEAR", length = 4)
	public String getStrYear() {
		return this.strYear;
	}

	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}

	@Column(name = "TITLE", length = 20)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT_PATH", length = 100)
	public String getContentPath() {
		return this.contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTRY_BY", length = 30)
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}