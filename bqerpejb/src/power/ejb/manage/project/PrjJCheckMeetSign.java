package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJCheckMeetSign entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_CHECK_MEET_SIGN")
public class PrjJCheckMeetSign implements java.io.Serializable {

	// Fields

	private Long checkSignId;
	private Long conId;
	private String conName;
	private String reportCode;
	private Double budgetCost;
	private Long contractorId;
	private String contractorName;
	private String owner;
	private Date startDate;
	private Date endDate;
	private String changeInfo;
	private String devolveOnInfo;
	private String memo;
	private String applyBy;
	private Date applyDate;
	private String equCheckText;
	private String safetyCheckText;
	private String manageCheckText;
	private String financeCheckText;
	private String auditCheckText;
	private String equCheckBy;
	private String safetyCheckBy;
	private String manageCheckBy;
	private String financeCheckBy;
	private String auditCheckBy;
	private Date equCheckDate;
	private Date safetyCheckDate;
	private Date manageCheckDate;
	private Date financeCheckDate;
	private Date auditCheckDate;
	private String backEntryBy;
	private Date backEnrtyDate;
	private String isBackEntry;
	private String enterpriseCode;
	private String isUse;
	private String prjId;
	

	// Constructors

	/** default constructor */
	public PrjJCheckMeetSign() {
	}

	/** minimal constructor */
	public PrjJCheckMeetSign(Long checkSignId) {
		this.checkSignId = checkSignId;
	}

	/** full constructor */
	public PrjJCheckMeetSign(Long checkSignId, Long conId, String conName,
			String reportCode, Double budgetCost, Long contractorId,
			String contractorName, String owner, Date startDate, Date endDate,
			String changeInfo, String devolveOnInfo, String memo,
			String applyBy, Date applyDate, String equCheckText,
			String safetyCheckText, String manageCheckText,
			String financeCheckText, String auditCheckText, String equCheckBy,
			String safetyCheckBy, String manageCheckBy, String financeCheckBy,
			String auditCheckBy, Date equCheckDate, Date safetyCheckDate,
			Date manageCheckDate, Date financeCheckDate, Date auditCheckDate,
			String backEntryBy, Date backEnrtyDate, String isBackEntry,
			String enterpriseCode, String isUse) {
		this.checkSignId = checkSignId;
		this.conId = conId;
		this.conName = conName;
		this.reportCode = reportCode;
		this.budgetCost = budgetCost;
		this.contractorId = contractorId;
		this.contractorName = contractorName;
		this.owner = owner;
		this.startDate = startDate;
		this.endDate = endDate;
		this.changeInfo = changeInfo;
		this.devolveOnInfo = devolveOnInfo;
		this.memo = memo;
		this.applyBy = applyBy;
		this.applyDate = applyDate;
		this.equCheckText = equCheckText;
		this.safetyCheckText = safetyCheckText;
		this.manageCheckText = manageCheckText;
		this.financeCheckText = financeCheckText;
		this.auditCheckText = auditCheckText;
		this.equCheckBy = equCheckBy;
		this.safetyCheckBy = safetyCheckBy;
		this.manageCheckBy = manageCheckBy;
		this.financeCheckBy = financeCheckBy;
		this.auditCheckBy = auditCheckBy;
		this.equCheckDate = equCheckDate;
		this.safetyCheckDate = safetyCheckDate;
		this.manageCheckDate = manageCheckDate;
		this.financeCheckDate = financeCheckDate;
		this.auditCheckDate = auditCheckDate;
		this.backEntryBy = backEntryBy;
		this.backEnrtyDate = backEnrtyDate;
		this.isBackEntry = isBackEntry;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CHECK_SIGN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCheckSignId() {
		return this.checkSignId;
	}

	public void setCheckSignId(Long checkSignId) {
		this.checkSignId = checkSignId;
	}

	@Column(name = "CON_ID", precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "CON_NAME", length = 100)
	public String getConName() {
		return this.conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	@Column(name = "REPORT_CODE", length = 20)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "BUDGET_COST", precision = 18, scale = 5)
	public Double getBudgetCost() {
		return this.budgetCost;
	}

	public void setBudgetCost(Double budgetCost) {
		this.budgetCost = budgetCost;
	}

	@Column(name = "CONTRACTOR_ID", precision = 10, scale = 0)
	public Long getContractorId() {
		return this.contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	@Column(name = "CONTRACTOR_NAME", length = 100)
	public String getContractorName() {
		return this.contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	@Column(name = "OWNER", length = 100)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CHANGE_INFO", length = 200)
	public String getChangeInfo() {
		return this.changeInfo;
	}

	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
	}

	@Column(name = "DEVOLVE_ON_INFO", length = 200)
	public String getDevolveOnInfo() {
		return this.devolveOnInfo;
	}

	public void setDevolveOnInfo(String devolveOnInfo) {
		this.devolveOnInfo = devolveOnInfo;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "APPLY_BY", length = 20)
	public String getApplyBy() {
		return this.applyBy;
	}

	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPLY_DATE", length = 7)
	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "EQU_CHECK_TEXT", length = 200)
	public String getEquCheckText() {
		return this.equCheckText;
	}

	public void setEquCheckText(String equCheckText) {
		this.equCheckText = equCheckText;
	}

	@Column(name = "SAFETY_CHECK_TEXT", length = 200)
	public String getSafetyCheckText() {
		return this.safetyCheckText;
	}

	public void setSafetyCheckText(String safetyCheckText) {
		this.safetyCheckText = safetyCheckText;
	}

	@Column(name = "MANAGE_CHECK_TEXT", length = 200)
	public String getManageCheckText() {
		return this.manageCheckText;
	}

	public void setManageCheckText(String manageCheckText) {
		this.manageCheckText = manageCheckText;
	}

	@Column(name = "FINANCE_CHECK_TEXT", length = 200)
	public String getFinanceCheckText() {
		return this.financeCheckText;
	}

	public void setFinanceCheckText(String financeCheckText) {
		this.financeCheckText = financeCheckText;
	}

	@Column(name = "AUDIT_CHECK_TEXT", length = 200)
	public String getAuditCheckText() {
		return this.auditCheckText;
	}

	public void setAuditCheckText(String auditCheckText) {
		this.auditCheckText = auditCheckText;
	}

	@Column(name = "EQU_CHECK_BY", length = 100)
	public String getEquCheckBy() {
		return this.equCheckBy;
	}

	public void setEquCheckBy(String equCheckBy) {
		this.equCheckBy = equCheckBy;
	}

	@Column(name = "SAFETY_CHECK_BY", length = 100)
	public String getSafetyCheckBy() {
		return this.safetyCheckBy;
	}

	public void setSafetyCheckBy(String safetyCheckBy) {
		this.safetyCheckBy = safetyCheckBy;
	}

	@Column(name = "MANAGE_CHECK_BY", length = 100)
	public String getManageCheckBy() {
		return this.manageCheckBy;
	}

	public void setManageCheckBy(String manageCheckBy) {
		this.manageCheckBy = manageCheckBy;
	}

	@Column(name = "FINANCE_CHECK_BY", length = 100)
	public String getFinanceCheckBy() {
		return this.financeCheckBy;
	}

	public void setFinanceCheckBy(String financeCheckBy) {
		this.financeCheckBy = financeCheckBy;
	}

	@Column(name = "AUDIT_CHECK_BY", length = 100)
	public String getAuditCheckBy() {
		return this.auditCheckBy;
	}

	public void setAuditCheckBy(String auditCheckBy) {
		this.auditCheckBy = auditCheckBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EQU_CHECK_DATE", length = 7)
	public Date getEquCheckDate() {
		return this.equCheckDate;
	}

	public void setEquCheckDate(Date equCheckDate) {
		this.equCheckDate = equCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SAFETY_CHECK_DATE", length = 7)
	public Date getSafetyCheckDate() {
		return this.safetyCheckDate;
	}

	public void setSafetyCheckDate(Date safetyCheckDate) {
		this.safetyCheckDate = safetyCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MANAGE_CHECK_DATE", length = 7)
	public Date getManageCheckDate() {
		return this.manageCheckDate;
	}

	public void setManageCheckDate(Date manageCheckDate) {
		this.manageCheckDate = manageCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FINANCE_CHECK_DATE", length = 7)
	public Date getFinanceCheckDate() {
		return this.financeCheckDate;
	}

	public void setFinanceCheckDate(Date financeCheckDate) {
		this.financeCheckDate = financeCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUDIT_CHECK_DATE", length = 7)
	public Date getAuditCheckDate() {
		return this.auditCheckDate;
	}

	public void setAuditCheckDate(Date auditCheckDate) {
		this.auditCheckDate = auditCheckDate;
	}

	@Column(name = "BACK_ENTRY_BY", length = 30)
	public String getBackEntryBy() {
		return this.backEntryBy;
	}

	public void setBackEntryBy(String backEntryBy) {
		this.backEntryBy = backEntryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BACK_ENRTY_DATE", length = 7)
	public Date getBackEnrtyDate() {
		return this.backEnrtyDate;
	}

	public void setBackEnrtyDate(Date backEnrtyDate) {
		this.backEnrtyDate = backEnrtyDate;
	}

	@Column(name = "IS_BACK_ENTRY", length = 1)
	public String getIsBackEntry() {
		return this.isBackEntry;
	}

	public void setIsBackEntry(String isBackEntry) {
		this.isBackEntry = isBackEntry;
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
	@Column(name = "PRJ_ID", length = 10)
	public String getPrjId() {
		return prjId;
	}

	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}

}