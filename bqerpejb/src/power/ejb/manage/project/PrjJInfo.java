package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PRJ_J_INFO")
public class PrjJInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String prjNo;
	private String prjNoShow;
	private String prjName;
	private Long prjYear;
	private Long prjTypeId;
	private String prjContent;
	private String constructionUnit;
	private Long prjStatus;
	private String itemId;
	private Double planAmount;
	private String chargeBy;
	private String chargeDep;
	private Date planStartDate;
	private Date planEndDate;
	private Long planTimeLimit;
	private Date factStartDate;
	private Date factEndDate;
	private Long factTimeLimit;
	private String annex;
	private String memo;
	private String entryBy;
	private Date entryDate;
	private String cmlAppraisal;
	private Long proWorkFlowNo;
	private Long accWorkFlowNo;
	private Date accSignStartDate;
	private Date proSignStartDate;
	private String enterpriseCode;
	private String isUse;
	private String constructionChargeBy;
	private String prjChangeMemo;
	private String prjDataMove;

	// Constructors

	/** default constructor */
	public PrjJInfo() {
	}

	/** minimal constructor */
	public PrjJInfo(Long id, String prjNo, Long prjTypeId, Long prjStatus,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.prjNo = prjNo;
		this.prjTypeId = prjTypeId;
		this.prjStatus = prjStatus;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public PrjJInfo(Long id, String prjNo, String prjNoShow, String prjName,
			Long prjYear, Long prjTypeId, String prjContent,
			String constructionUnit, Long prjStatus, String itemId,
			Double planAmount, String chargeBy, String chargeDep,
			Date planStartDate, Date planEndDate, Long planTimeLimit,
			Date factStartDate, Date factEndDate, Long factTimeLimit,
			String annex, String memo, String entryBy, Date entryDate,
			String cmlAppraisal, Long proWorkFlowNo, Long accWorkFlowNo,
			Date accSignStartDate, Date proSignStartDate,
			String enterpriseCode, String isUse, String constructionChargeBy,
			String prjChangeMemo, String prjDataMove) {
		this.id = id;
		this.prjNo = prjNo;
		this.prjNoShow = prjNoShow;
		this.prjName = prjName;
		this.prjYear = prjYear;
		this.prjTypeId = prjTypeId;
		this.prjContent = prjContent;
		this.constructionUnit = constructionUnit;
		this.prjStatus = prjStatus;
		this.itemId = itemId;
		this.planAmount = planAmount;
		this.chargeBy = chargeBy;
		this.chargeDep = chargeDep;
		this.planStartDate = planStartDate;
		this.planEndDate = planEndDate;
		this.planTimeLimit = planTimeLimit;
		this.factStartDate = factStartDate;
		this.factEndDate = factEndDate;
		this.factTimeLimit = factTimeLimit;
		this.annex = annex;
		this.memo = memo;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.cmlAppraisal = cmlAppraisal;
		this.proWorkFlowNo = proWorkFlowNo;
		this.accWorkFlowNo = accWorkFlowNo;
		this.accSignStartDate = accSignStartDate;
		this.proSignStartDate = proSignStartDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.constructionChargeBy = constructionChargeBy;
		this.prjChangeMemo = prjChangeMemo;
		this.prjDataMove = prjDataMove;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PRJ_NO", nullable = false, length = 50)
	public String getPrjNo() {
		return this.prjNo;
	}

	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	@Column(name = "PRJ_NO_SHOW", length = 30)
	public String getPrjNoShow() {
		return this.prjNoShow;
	}

	public void setPrjNoShow(String prjNoShow) {
		this.prjNoShow = prjNoShow;
	}

	@Column(name = "PRJ_NAME", length = 100)
	public String getPrjName() {
		return this.prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	@Column(name = "PRJ_YEAR", precision = 4, scale = 0)
	public Long getPrjYear() {
		return this.prjYear;
	}

	public void setPrjYear(Long prjYear) {
		this.prjYear = prjYear;
	}

	@Column(name = "PRJ_TYPE_ID", nullable = false, precision = 10, scale = 0)
	public Long getPrjTypeId() {
		return this.prjTypeId;
	}

	public void setPrjTypeId(Long prjTypeId) {
		this.prjTypeId = prjTypeId;
	}

	@Column(name = "PRJ_CONTENT", length = 1000)
	public String getPrjContent() {
		return this.prjContent;
	}

	public void setPrjContent(String prjContent) {
		this.prjContent = prjContent;
	}

	@Column(name = "CONSTRUCTION_UNIT", length = 100)
	public String getConstructionUnit() {
		return this.constructionUnit;
	}

	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}

	@Column(name = "PRJ_STATUS", nullable = false, precision = 2, scale = 0)
	public Long getPrjStatus() {
		return this.prjStatus;
	}

	public void setPrjStatus(Long prjStatus) {
		this.prjStatus = prjStatus;
	}

	@Column(name = "ITEM_ID", length = 40, scale = 0)
	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Column(name = "PLAN_AMOUNT", precision = 15, scale = 4)
	public Double getPlanAmount() {
		return this.planAmount;
	}

	public void setPlanAmount(Double planAmount) {
		this.planAmount = planAmount;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "CHARGE_DEP", length = 16)
	public String getChargeDep() {
		return this.chargeDep;
	}

	public void setChargeDep(String chargeDep) {
		this.chargeDep = chargeDep;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_START_DATE", length = 7)
	public Date getPlanStartDate() {
		return this.planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_END_DATE", length = 7)
	public Date getPlanEndDate() {
		return this.planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	@Column(name = "PLAN_TIME_LIMIT", precision = 4, scale = 0)
	public Long getPlanTimeLimit() {
		return this.planTimeLimit;
	}

	public void setPlanTimeLimit(Long planTimeLimit) {
		this.planTimeLimit = planTimeLimit;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FACT_START_DATE", length = 7)
	public Date getFactStartDate() {
		return this.factStartDate;
	}

	public void setFactStartDate(Date factStartDate) {
		this.factStartDate = factStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FACT_END_DATE", length = 7)
	public Date getFactEndDate() {
		return this.factEndDate;
	}

	public void setFactEndDate(Date factEndDate) {
		this.factEndDate = factEndDate;
	}

	@Column(name = "FACT_TIME_LIMIT", precision = 4, scale = 0)
	public Long getFactTimeLimit() {
		return this.factTimeLimit;
	}

	public void setFactTimeLimit(Long factTimeLimit) {
		this.factTimeLimit = factTimeLimit;
	}

	@Column(name = "ANNEX", length = 50)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTRY_BY", length = 16)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "CML_APPRAISAL", length = 1000)
	public String getCmlAppraisal() {
		return this.cmlAppraisal;
	}

	public void setCmlAppraisal(String cmlAppraisal) {
		this.cmlAppraisal = cmlAppraisal;
	}

	@Column(name = "PRO_WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getProWorkFlowNo() {
		return this.proWorkFlowNo;
	}

	public void setProWorkFlowNo(Long proWorkFlowNo) {
		this.proWorkFlowNo = proWorkFlowNo;
	}

	@Column(name = "ACC_WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getAccWorkFlowNo() {
		return this.accWorkFlowNo;
	}

	public void setAccWorkFlowNo(Long accWorkFlowNo) {
		this.accWorkFlowNo = accWorkFlowNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACC_SIGN_START_DATE", length = 7)
	public Date getAccSignStartDate() {
		return this.accSignStartDate;
	}

	public void setAccSignStartDate(Date accSignStartDate) {
		this.accSignStartDate = accSignStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PRO_SIGN_START_DATE", length = 7)
	public Date getProSignStartDate() {
		return this.proSignStartDate;
	}

	public void setProSignStartDate(Date proSignStartDate) {
		this.proSignStartDate = proSignStartDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "CONSTRUCTION_CHARGE_BY", length = 16)
	public String getConstructionChargeBy() {
		return this.constructionChargeBy;
	}

	public void setConstructionChargeBy(String constructionChargeBy) {
		this.constructionChargeBy = constructionChargeBy;
	}

	@Column(name = "PRJ_CHANGE_MEMO", length = 200)
	public String getPrjChangeMemo() {
		return prjChangeMemo;
	}

	public void setPrjChangeMemo(String prjChangeMemo) {
		this.prjChangeMemo = prjChangeMemo;
	}

	@Column(name = "PRJ_DATA_MOVE", length = 200)
	public String getPrjDataMove() {
		return prjDataMove;
	}

	public void setPrjDataMove(String prjDataMove) {
		this.prjDataMove = prjDataMove;
	}
}