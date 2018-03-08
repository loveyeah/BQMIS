package power.ejb.equ.planrepair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJPlanRepairMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_PLAN_REPAIR_MAIN")
public class EquJPlanRepairMain implements java.io.Serializable {

	// Fields

	private Long repairId;
	private String repairCode;
	private String repairName;
	private Long planTypeId;
	private Date planStartTime;
	private Date planEndTime;
	private Double fare;
	private Long fareSoruce;
	private String content;
	private String remark;
	private String fillBy;
	private Date fillDate;
	private Long workFlowNo;
	private String status;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquJPlanRepairMain() {
	}

	/** minimal constructor */
	public EquJPlanRepairMain(Long repairId) {
		this.repairId = repairId;
	}

	/** full constructor */
	public EquJPlanRepairMain(Long repairId, String repairCode,
			String repairName, Long planTypeId, Date planStartTime,
			Date planEndTime, Double fare, Long fareSoruce, String content,
			String remark, String fillBy, Date fillDate, Long workFlowNo,
			String status, String isUse, String enterpriseCode) {
		this.repairId = repairId;
		this.repairCode = repairCode;
		this.repairName = repairName;
		this.planTypeId = planTypeId;
		this.planStartTime = planStartTime;
		this.planEndTime = planEndTime;
		this.fare = fare;
		this.fareSoruce = fareSoruce;
		this.content = content;
		this.remark = remark;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.workFlowNo = workFlowNo;
		this.status = status;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPAIR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRepairId() {
		return this.repairId;
	}

	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	@Column(name = "REPAIR_CODE", length = 30)
	public String getRepairCode() {
		return this.repairCode;
	}

	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}

	@Column(name = "REPAIR_NAME", length = 100)
	public String getRepairName() {
		return this.repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}

	@Column(name = "PLAN_TYPE_ID", precision = 10, scale = 0)
	public Long getPlanTypeId() {
		return this.planTypeId;
	}

	public void setPlanTypeId(Long planTypeId) {
		this.planTypeId = planTypeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_START_TIME", length = 7)
	public Date getPlanStartTime() {
		return this.planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_END_TIME", length = 7)
	public Date getPlanEndTime() {
		return this.planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	@Column(name = "FARE", precision = 15, scale = 4)
	public Double getFare() {
		return this.fare;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	@Column(name = "FARE_SORUCE", precision = 10, scale = 0)
	public Long getFareSoruce() {
		return this.fareSoruce;
	}

	public void setFareSoruce(Long fareSoruce) {
		this.fareSoruce = fareSoruce;
	}

	@Column(name = "CONTENT", length = 500)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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