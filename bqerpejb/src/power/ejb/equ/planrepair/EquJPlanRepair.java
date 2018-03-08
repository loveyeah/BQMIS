package power.ejb.equ.planrepair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJPlanRepair entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_PLAN_REPAIR")
public class EquJPlanRepair implements java.io.Serializable {

	// Fields

	private Long repairDetailId;
	private Long repairId;
	private Long contentId;
	private String detailNo;
	private String detailName;
	private String equCode;
	private String specialityCode;
	private Date planStartTime;
	private Date planEndTime;
	private Double workingdays;
	private Double fare;
	private String chargeBy;
	private String content;
	private String memo;
	private String annex;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquJPlanRepair() {
	}

	/** minimal constructor */
	public EquJPlanRepair(Long repairDetailId, Long repairId) {
		this.repairDetailId = repairDetailId;
		this.repairId = repairId;
	}

	/** full constructor */
	public EquJPlanRepair(Long repairDetailId, Long repairId, Long contentId,
			String detailNo, String detailName, String equCode,
			String specialityCode, Date planStartTime, Date planEndTime,
			Double workingdays, Double fare, String chargeBy, String content,
			String memo, String annex, String isUse, String enterpriseCode) {
		this.repairDetailId = repairDetailId;
		this.repairId = repairId;
		this.contentId = contentId;
		this.detailNo = detailNo;
		this.detailName = detailName;
		this.equCode = equCode;
		this.specialityCode = specialityCode;
		this.planStartTime = planStartTime;
		this.planEndTime = planEndTime;
		this.workingdays = workingdays;
		this.fare = fare;
		this.chargeBy = chargeBy;
		this.content = content;
		this.memo = memo;
		this.annex = annex;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPAIR_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRepairDetailId() {
		return this.repairDetailId;
	}

	public void setRepairDetailId(Long repairDetailId) {
		this.repairDetailId = repairDetailId;
	}

	@Column(name = "REPAIR_ID", nullable = false, precision = 10, scale = 0)
	public Long getRepairId() {
		return this.repairId;
	}

	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	@Column(name = "CONTENT_ID", precision = 10, scale = 0)
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "DETAIL_NO", length = 30)
	public String getDetailNo() {
		return this.detailNo;
	}

	public void setDetailNo(String detailNo) {
		this.detailNo = detailNo;
	}

	@Column(name = "DETAIL_NAME", length = 500)
	public String getDetailName() {
		return this.detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return this.equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	@Column(name = "SPECIALITY_CODE", length = 30)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_START_TIME")
	public Date getPlanStartTime() {
		return this.planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_END_TIME")
	public Date getPlanEndTime() {
		return this.planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	@Column(name = "WORKINGDAYS", precision = 8)
	public Double getWorkingdays() {
		return this.workingdays;
	}

	public void setWorkingdays(Double workingdays) {
		this.workingdays = workingdays;
	}

	@Column(name = "FARE", precision = 15, scale = 4)
	public Double getFare() {
		return this.fare;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "CONTENT", length = 500)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ANNEX",length = 255)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
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