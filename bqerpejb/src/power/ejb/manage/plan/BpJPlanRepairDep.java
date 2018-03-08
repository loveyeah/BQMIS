package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanRepairDep entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_REPAIR_DEP", schema = "POWER")
public class BpJPlanRepairDep implements java.io.Serializable {

	// Fields

	private Long planDepId;
	private String planTime;
	private String editDep;
	private String editBy;
	private Date editDate;
	private Long workFlowNo;
	private Long status;
	private String enterpriseCode;
	private Date weekStartTime;
	private Date weekEndTime;

	// Constructors

	/** default constructor */
	public BpJPlanRepairDep() {
	}

	/** minimal constructor */
	public BpJPlanRepairDep(Long planDepId) {
		this.planDepId = planDepId;
	}

	/** full constructor */
	public BpJPlanRepairDep(Long planDepId, String planTime, String editDep,
			String editBy, Date editDate, Long workFlowNo, Long status,
			String enterpriseCode, Date weekStartTime, Date weekEndTime) {
		this.planDepId = planDepId;
		this.planTime = planTime;
		this.editDep = editDep;
		this.editBy = editBy;
		this.editDate = editDate;
		this.workFlowNo = workFlowNo;
		this.status = status;
		this.enterpriseCode = enterpriseCode;
		this.weekStartTime = weekStartTime;
		this.weekEndTime = weekEndTime;
	}

	// Property accessors
	@Id
	@Column(name = "PLAN_DEP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPlanDepId() {
		return this.planDepId;
	}

	public void setPlanDepId(Long planDepId) {
		this.planDepId = planDepId;
	}

	@Column(name = "PLAN_TIME", length = 10)
	public String getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	@Column(name = "EDIT_DEP", length = 20)
	public String getEditDep() {
		return this.editDep;
	}

	public void setEditDep(String editDep) {
		this.editDep = editDep;
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

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WEEK_START_TIME", length = 7)
	public Date getWeekStartTime() {
		return this.weekStartTime;
	}

	public void setWeekStartTime(Date weekStartTime) {
		this.weekStartTime = weekStartTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WEEK_END_TIME", length = 7)
	public Date getWeekEndTime() {
		return this.weekEndTime;
	}

	public void setWeekEndTime(Date weekEndTime) {
		this.weekEndTime = weekEndTime;
	}

}