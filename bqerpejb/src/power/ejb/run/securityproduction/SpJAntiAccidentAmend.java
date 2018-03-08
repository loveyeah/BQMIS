package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJAntiAccidentAmend entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_ANTI_ACCIDENT_AMEND", schema = "POWER")
public class SpJAntiAccidentAmend implements java.io.Serializable {

	// Fields

	private Long amendId;
	private Long checkupId;
	private String existProblem;
	private String amendMeasure;
	private String beforeAmendMeasure;
	private Date planFinishDate;
	private Date amendFinishDate;
	private String chargeDept;
	private String chargeBy;
	private String superviseDept;
	private String superviseBy;
	private String noAmendReason;
	private String problemKind;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public SpJAntiAccidentAmend() {
	}

	/** minimal constructor */
	public SpJAntiAccidentAmend(Long amendId) {
		this.amendId = amendId;
	}

	/** full constructor */
	public SpJAntiAccidentAmend(Long amendId, Long checkupId,
			String existProblem, String amendMeasure,
			String beforeAmendMeasure, Date planFinishDate,
			Date amendFinishDate, String chargeDept, String chargeBy,
			String superviseDept, String superviseBy, String noAmendReason,
			String problemKind, String modifyBy, Date modifyDate,
			String enterpriseCode, String isUse) {
		this.amendId = amendId;
		this.checkupId = checkupId;
		this.existProblem = existProblem;
		this.amendMeasure = amendMeasure;
		this.beforeAmendMeasure = beforeAmendMeasure;
		this.planFinishDate = planFinishDate;
		this.amendFinishDate = amendFinishDate;
		this.chargeDept = chargeDept;
		this.chargeBy = chargeBy;
		this.superviseDept = superviseDept;
		this.superviseBy = superviseBy;
		this.noAmendReason = noAmendReason;
		this.problemKind = problemKind;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "AMEND_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAmendId() {
		return this.amendId;
	}

	public void setAmendId(Long amendId) {
		this.amendId = amendId;
	}

	@Column(name = "CHECKUP_ID", precision = 10, scale = 0)
	public Long getCheckupId() {
		return this.checkupId;
	}

	public void setCheckupId(Long checkupId) {
		this.checkupId = checkupId;
	}

	@Column(name = "EXIST_PROBLEM", length = 100)
	public String getExistProblem() {
		return this.existProblem;
	}

	public void setExistProblem(String existProblem) {
		this.existProblem = existProblem;
	}

	@Column(name = "AMEND_MEASURE", length = 100)
	public String getAmendMeasure() {
		return this.amendMeasure;
	}

	public void setAmendMeasure(String amendMeasure) {
		this.amendMeasure = amendMeasure;
	}

	@Column(name = "BEFORE_AMEND_MEASURE", length = 100)
	public String getBeforeAmendMeasure() {
		return this.beforeAmendMeasure;
	}

	public void setBeforeAmendMeasure(String beforeAmendMeasure) {
		this.beforeAmendMeasure = beforeAmendMeasure;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_FINISH_DATE", length = 7)
	public Date getPlanFinishDate() {
		return this.planFinishDate;
	}

	public void setPlanFinishDate(Date planFinishDate) {
		this.planFinishDate = planFinishDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AMEND_FINISH_DATE", length = 7)
	public Date getAmendFinishDate() {
		return this.amendFinishDate;
	}

	public void setAmendFinishDate(Date amendFinishDate) {
		this.amendFinishDate = amendFinishDate;
	}

	@Column(name = "CHARGE_DEPT", length = 20)
	public String getChargeDept() {
		return this.chargeDept;
	}

	public void setChargeDept(String chargeDept) {
		this.chargeDept = chargeDept;
	}

	@Column(name = "CHARGE_BY", length = 30)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "SUPERVISE_DEPT", length = 20)
	public String getSuperviseDept() {
		return this.superviseDept;
	}

	public void setSuperviseDept(String superviseDept) {
		this.superviseDept = superviseDept;
	}

	@Column(name = "SUPERVISE_BY", length = 30)
	public String getSuperviseBy() {
		return this.superviseBy;
	}

	public void setSuperviseBy(String superviseBy) {
		this.superviseBy = superviseBy;
	}

	@Column(name = "NO_AMEND_REASON", length = 100)
	public String getNoAmendReason() {
		return this.noAmendReason;
	}

	public void setNoAmendReason(String noAmendReason) {
		this.noAmendReason = noAmendReason;
	}

	@Column(name = "PROBLEM_KIND", length = 1)
	public String getProblemKind() {
		return this.problemKind;
	}

	public void setProblemKind(String problemKind) {
		this.problemKind = problemKind;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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