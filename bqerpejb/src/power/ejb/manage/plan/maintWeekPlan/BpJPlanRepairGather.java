package power.ejb.manage.plan.maintWeekPlan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanRepairGather entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_REPAIR_GATHER", schema = "POWER")
public class BpJPlanRepairGather implements java.io.Serializable {

	// Fields

	private Long gatherId;
	private String planTime;
	private String gatherDep;
	private String gatherBy;
	private Date gatherDate;
	private Long workFlowNo;
	private Long signStatus;
	private String memo;
	private String enterpriseCode;
	private Date weekStartTime;
	private Date weekEndTime;

	// Constructors

	/** default constructor */
	public BpJPlanRepairGather() {
	}

	/** minimal constructor */
	public BpJPlanRepairGather(Long gatherId) {
		this.gatherId = gatherId;
	}

	/** full constructor */
	public BpJPlanRepairGather(Long gatherId, String planTime,
			String gatherDep, String gatherBy, Date gatherDate,
			Long workFlowNo, Long signStatus, String memo,
			String enterpriseCode, Date weekStartTime, Date weekEndTime) {
		this.gatherId = gatherId;
		this.planTime = planTime;
		this.gatherDep = gatherDep;
		this.gatherBy = gatherBy;
		this.gatherDate = gatherDate;
		this.workFlowNo = workFlowNo;
		this.signStatus = signStatus;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.weekStartTime = weekStartTime;
		this.weekEndTime = weekEndTime;
	}

	// Property accessors
	@Id
	@Column(name = "GATHER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getGatherId() {
		return this.gatherId;
	}

	public void setGatherId(Long gatherId) {
		this.gatherId = gatherId;
	}

	@Column(name = "PLAN_TIME", length = 10)
	public String getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	@Column(name = "GATHER_DEP", length = 20)
	public String getGatherDep() {
		return this.gatherDep;
	}

	public void setGatherDep(String gatherDep) {
		this.gatherDep = gatherDep;
	}

	@Column(name = "GATHER_BY", length = 16)
	public String getGatherBy() {
		return this.gatherBy;
	}

	public void setGatherBy(String gatherBy) {
		this.gatherBy = gatherBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GATHER_DATE", length = 7)
	public Date getGatherDate() {
		return this.gatherDate;
	}

	public void setGatherDate(Date gatherDate) {
		this.gatherDate = gatherDate;
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

	@Column(name = "MEMO", length = 400)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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