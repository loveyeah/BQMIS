package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@SuppressWarnings("serial")
@Entity
@Table(name = "BP_J_PLAN_JOB_MAIN")
public class BpJPlanJobMain implements java.io.Serializable {

	// Fields

	private Date planTime;
	private String editBy;
	private Date editDate;
	private String finishEditBy;
	private Date finishEditDate;
	private Long workFlowNo;
	private Long signStatus;
	private Long finishWorkFlowNo;
	private Long finishSignStatus;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanJobMain() {
	}

	/** minimal constructor */
	public BpJPlanJobMain(Date planTime) {
		this.planTime = planTime;
	}

	/** full constructor */
	public BpJPlanJobMain(Date planTime, String editBy, Date editDate,
			String finishEditBy, Date finishEditDate, Long workFlowNo,
			Long signStatus, Long finishWorkFlowNo, Long finishSignStatus,
			String enterpriseCode) {
		this.planTime = planTime;
		this.editBy = editBy;
		this.editDate = editDate;
		this.finishEditBy = finishEditBy;
		this.finishEditDate = finishEditDate;
		this.workFlowNo = workFlowNo;
		this.signStatus = signStatus;
		this.finishWorkFlowNo = finishWorkFlowNo;
		this.finishSignStatus = finishSignStatus;
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

	@Column(name = "FINISH_EDIT_BY", length = 16)
	public String getFinishEditBy() {
		return this.finishEditBy;
	}

	public void setFinishEditBy(String finishEditBy) {
		this.finishEditBy = finishEditBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FINISH_EDIT_DATE", length = 7)
	public Date getFinishEditDate() {
		return this.finishEditDate;
	}

	public void setFinishEditDate(Date finishEditDate) {
		this.finishEditDate = finishEditDate;
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

	@Column(name = "FINISH_WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getFinishWorkFlowNo() {
		return this.finishWorkFlowNo;
	}

	public void setFinishWorkFlowNo(Long finishWorkFlowNo) {
		this.finishWorkFlowNo = finishWorkFlowNo;
	}

	@Column(name = "FINISH_SIGN_STATUS", precision = 1, scale = 0)
	public Long getFinishSignStatus() {
		return this.finishSignStatus;
	}

	public void setFinishSignStatus(Long finishSignStatus) {
		this.finishSignStatus = finishSignStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}