package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanGuideMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_GUIDE_MAIN")
public class BpJPlanGuideMain implements java.io.Serializable {

	// Fields

	private Date planTime;
	private String editBy;
	private Date editDate;
	private Long planStatus;
	private Long workFlowNo;
	private Long signStatus;
	private Date releaseDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanGuideMain() {
	}

	/** minimal constructor */
	public BpJPlanGuideMain(Date planTime) {
		this.planTime = planTime;
	}

	/** full constructor */
	public BpJPlanGuideMain(Date planTime, String editBy, Date editDate,
			Long planStatus, Long workFlowNo, Long signStatus,
			Date releaseDate, String enterpriseCode) {
		this.planTime = planTime;
		this.editBy = editBy;
		this.editDate = editDate;
		this.planStatus = planStatus;
		this.workFlowNo = workFlowNo;
		this.signStatus = signStatus;
		this.releaseDate = releaseDate;
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

	@Column(name = "PLAN_STATUS", precision = 1, scale = 0)
	public Long getPlanStatus() {
		return this.planStatus;
	}

	public void setPlanStatus(Long planStatus) {
		this.planStatus = planStatus;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "SIGN_STATUS", precision = 1, scale = 0)
	public Long getSignStatus() {
		return this.signStatus;
	}

	public void setSignStatus(Long signStatus) {
		this.signStatus = signStatus;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RELEASE_DATE", length = 7)
	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}