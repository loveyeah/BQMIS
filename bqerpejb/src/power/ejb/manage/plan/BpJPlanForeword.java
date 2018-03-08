package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanForeword entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_FOREWORD")
public class BpJPlanForeword implements java.io.Serializable {

	// Fields

	private Date planTime;
	private String planForeword;
	private Long planStatus;
	private String editBy;
	private Date editDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanForeword() {
	}

	/** minimal constructor */
	public BpJPlanForeword(Date planTime) {
		this.planTime = planTime;
	}

	/** full constructor */
	public BpJPlanForeword(Date planTime, String planForeword, Long planStatus,
			String editBy, Date editDate, String enterpriseCode) {
		this.planTime = planTime;
		this.planForeword = planForeword;
		this.planStatus = planStatus;
		this.editBy = editBy;
		this.editDate = editDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_TIME", unique = true, nullable = false, length = 7)
	public Date getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	@Column(name = "PLAN_FOREWORD", length = 2000)
	public String getPlanForeword() {
		return this.planForeword;
	}

	public void setPlanForeword(String planForeword) {
		this.planForeword = planForeword;
	}

	@Column(name = "PLAN_STATUS", precision = 1, scale = 0)
	public Long getPlanStatus() {
		return this.planStatus;
	}

	public void setPlanStatus(Long planStatus) {
		this.planStatus = planStatus;
	}

	@Column(name = "EDIT_BY", length = 16)
	public String getEditBy() {
		return this.editBy;
	}

	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EDIT_DATE", length = 7)
	public Date getEditDate() {
		return this.editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}