package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJCheck entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_CHECK", schema = "POWER")
public class PrjJCheck implements java.io.Serializable {

	// Fields

	private Long checkId;
	private String contractName;
	private Long conId;
	private String reportCode;
	private Date startDate;
	private Date endDate;
	private String contractorName;
	private Long contractorId;
	private String chargeBy;
	private String deptChargeBy;
	private String checkAppraise;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;
	private Long workFlowNo;
	private Long statusId;
	private String checkApply;
	private String applyBy;
	private Date applyDate;
	private String approveText;
	private String approveBy;
	private Date approveDate;
	private String checkText;
	private String checkBy;
	private Date checkDate;
	private String backEntryBy;
	private Date backEnrtyDate;
	private String isBackEntry;
	private String prjId;

	// Constructors

	/** default constructor */
	public PrjJCheck() {
	}

	/** minimal constructor */
	public PrjJCheck(Long checkId) {
		this.checkId = checkId;
	}

	/** full constructor */
	public PrjJCheck(Long checkId, String contractName, Long conId,
			String reportCode, Date startDate, Date endDate,
			String contractorName, Long contractorId, String chargeBy,
			String deptChargeBy, String checkAppraise, String entryBy,
			Date entryDate, String enterpriseCode, String isUse,
			Long workFlowNo, Long statusId, String checkApply, String applyBy,
			Date applyDate, String approveText, String approveBy,
			Date approveDate, String checkText, String checkBy, Date checkDate,
			String backEntryBy, Date backEnrtyDate, String isBackEntry) {
		this.checkId = checkId;
		this.contractName = contractName;
		this.conId = conId;
		this.reportCode = reportCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractorName = contractorName;
		this.contractorId = contractorId;
		this.chargeBy = chargeBy;
		this.deptChargeBy = deptChargeBy;
		this.checkAppraise = checkAppraise;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.workFlowNo = workFlowNo;
		this.statusId = statusId;
		this.checkApply = checkApply;
		this.applyBy = applyBy;
		this.applyDate = applyDate;
		this.approveText = approveText;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.checkText = checkText;
		this.checkBy = checkBy;
		this.checkDate = checkDate;
		this.backEntryBy = backEntryBy;
		this.backEnrtyDate = backEnrtyDate;
		this.isBackEntry = isBackEntry;
	}

	// Property accessors
	@Id
	@Column(name = "CHECK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCheckId() {
		return this.checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	@Column(name = "CONTRACT_NAME", length = 100)
	public String getContractName() {
		return this.contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Column(name = "CON_ID", precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "REPORT_CODE", length = 20)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CONTRACTOR_NAME", length = 100)
	public String getContractorName() {
		return this.contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	@Column(name = "CONTRACTOR_ID", precision = 10, scale = 0)
	public Long getContractorId() {
		return this.contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	@Column(name = "CHARGE_BY", length = 20)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "DEPT_CHARGE_BY", length = 20)
	public String getDeptChargeBy() {
		return this.deptChargeBy;
	}

	public void setDeptChargeBy(String deptChargeBy) {
		this.deptChargeBy = deptChargeBy;
	}

	@Column(name = "CHECK_APPRAISE", length = 100)
	public String getCheckAppraise() {
		return this.checkAppraise;
	}

	public void setCheckAppraise(String checkAppraise) {
		this.checkAppraise = checkAppraise;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
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

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "STATUS_ID", precision = 2, scale = 0)
	public Long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "CHECK_APPLY", length = 100)
	public String getCheckApply() {
		return this.checkApply;
	}

	public void setCheckApply(String checkApply) {
		this.checkApply = checkApply;
	}

	@Column(name = "APPLY_BY", length = 30)
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

	@Column(name = "APPROVE_TEXT", length = 100)
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "CHECK_TEXT", length = 100)
	public String getCheckText() {
		return this.checkText;
	}

	public void setCheckText(String checkText) {
		this.checkText = checkText;
	}

	@Column(name = "CHECK_BY", length = 30)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "BACK_ENTRY_BY", length = 30)
	public String getBackEntryBy() {
		return this.backEntryBy;
	}

	public void setBackEntryBy(String backEntryBy) {
		this.backEntryBy = backEntryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
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
	@Column(name = "PRJ_ID", length = 10)
	public String getPrjId() {
		return prjId;
	}

	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}

}