package power.ejb.manage.plan.maintWeekPlan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanRepairGatherDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_REPAIR_GATHER_DETAIL")
public class BpJPlanRepairGatherDetail implements java.io.Serializable {

	// Fields

	private Long planDetailId;
	private Long gatherId;
	private String content;
	private String chargeDep;
	private String assortDep;
	private Date beginTime;
	private Date endTime;
	private Long days;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanRepairGatherDetail() {
	}

	/** minimal constructor */
	public BpJPlanRepairGatherDetail(Long planDetailId) {
		this.planDetailId = planDetailId;
	}

	/** full constructor */
	public BpJPlanRepairGatherDetail(Long planDetailId, Long gatherId,
			String content, String chargeDep, String assortDep, Date beginTime,
			Date endTime, Long days, String memo, String isUse,
			String enterpriseCode) {
		this.planDetailId = planDetailId;
		this.gatherId = gatherId;
		this.content = content;
		this.chargeDep = chargeDep;
		this.assortDep = assortDep;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.days = days;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PLAN_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPlanDetailId() {
		return this.planDetailId;
	}

	public void setPlanDetailId(Long planDetailId) {
		this.planDetailId = planDetailId;
	}

	@Column(name = "GATHER_ID", precision = 10, scale = 0)
	public Long getGatherId() {
		return this.gatherId;
	}

	public void setGatherId(Long gatherId) {
		this.gatherId = gatherId;
	}

	@Column(name = "CONTENT", length = 300)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "CHARGE_DEP", length = 20)
	public String getChargeDep() {
		return this.chargeDep;
	}

	public void setChargeDep(String chargeDep) {
		this.chargeDep = chargeDep;
	}

	@Column(name = "ASSORT_DEP", length = 20)
	public String getAssortDep() {
		return this.assortDep;
	}

	public void setAssortDep(String assortDep) {
		this.assortDep = assortDep;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BEGIN_TIME", length = 7)
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "DAYS", precision = 4, scale = 0)
	public Long getDays() {
		return this.days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

}