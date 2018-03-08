package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLaborChange entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LABOR_CHANGE")
public class HrJLaborChange implements java.io.Serializable {

	// Fields

	private Long laborChangeId;
	private String workCode;
	private Date changeDate;
	private Date startDate;
	private Long oldLbWorkId;
	private Long newLbWorkId;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJLaborChange() {
	}

	/** minimal constructor */
	public HrJLaborChange(Long laborChangeId) {
		this.laborChangeId = laborChangeId;
	}

	/** full constructor */
	public HrJLaborChange(Long laborChangeId, String workCode, Date changeDate,
			Date startDate, Long oldLbWorkId, Long newLbWorkId,
			String enterpriseCode, String isUse) {
		this.laborChangeId = laborChangeId;
		this.workCode = workCode;
		this.changeDate = changeDate;
		this.startDate = startDate;
		this.oldLbWorkId = oldLbWorkId;
		this.newLbWorkId = newLbWorkId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LABOR_CHANGE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLaborChangeId() {
		return this.laborChangeId;
	}

	public void setLaborChangeId(Long laborChangeId) {
		this.laborChangeId = laborChangeId;
	}

	@Column(name = "WORK_CODE", length = 30)
	public String getWorkCode() {
		return this.workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHANGE_DATE", length = 7)
	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "OLD_LB_WORK_ID", precision = 10, scale = 0)
	public Long getOldLbWorkId() {
		return this.oldLbWorkId;
	}

	public void setOldLbWorkId(Long oldLbWorkId) {
		this.oldLbWorkId = oldLbWorkId;
	}

	@Column(name = "NEW_LB_WORK_ID", precision = 10, scale = 0)
	public Long getNewLbWorkId() {
		return this.newLbWorkId;
	}

	public void setNewLbWorkId(Long newLbWorkId) {
		this.newLbWorkId = newLbWorkId;
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