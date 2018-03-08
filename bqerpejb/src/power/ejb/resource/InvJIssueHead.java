package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJIssueHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_ISSUE_HEAD")
public class InvJIssueHead implements java.io.Serializable {

	// Fields

	private Long issueHeadId;
	private String issueNo;
	private String projectCode;
	private String woNo;
	private Long costItemId;
	private String mrNo;
	private String receiptBy;
	private String receiptDep;
	private Date dueDate;
	private String feeByDep;
	private String feeBySpecial;
	private String memo;
	private String getRealPerson;
	private String checkedBy;
	private Date checkedDate;
	private Long workFlowNo;
	private String issueStatus;
	private String isEmergency;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private Long planOriginalId;
	private String itemCode;
	private String refIssueNo;

	// Constructors

	/** default constructor */
	public InvJIssueHead() {
	}

	/** minimal constructor */
	public InvJIssueHead(Long issueHeadId, String issueNo, String receiptBy,
			String receiptDep, Date dueDate, String issueStatus,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.issueHeadId = issueHeadId;
		this.issueNo = issueNo;
		this.receiptBy = receiptBy;
		this.receiptDep = receiptDep;
		this.dueDate = dueDate;
		this.issueStatus = issueStatus;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvJIssueHead(Long issueHeadId, String issueNo, String projectCode,
			String woNo, Long costItemId, String mrNo, String receiptBy,
			String receiptDep, Date dueDate, String feeByDep,
			String feeBySpecial, String memo, String getRealPerson,
			String checkedBy, Date checkedDate, Long workFlowNo,
			String issueStatus, String isEmergency, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse,
			Long planOriginalId, String itemCode,String refIssueNo) {
		this.issueHeadId = issueHeadId;
		this.issueNo = issueNo;
		this.projectCode = projectCode;
		this.woNo = woNo;
		this.costItemId = costItemId;
		this.mrNo = mrNo;
		this.receiptBy = receiptBy;
		this.receiptDep = receiptDep;
		this.dueDate = dueDate;
		this.feeByDep = feeByDep;
		this.feeBySpecial = feeBySpecial;
		this.memo = memo;
		this.getRealPerson = getRealPerson;
		this.checkedBy = checkedBy;
		this.checkedDate = checkedDate;
		this.workFlowNo = workFlowNo;
		this.issueStatus = issueStatus;
		this.isEmergency = isEmergency;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.planOriginalId = planOriginalId;
		this.itemCode = itemCode;
		this.refIssueNo=refIssueNo;
	}

	// Property accessors
	@Id
	@Column(name = "ISSUE_HEAD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getIssueHeadId() {
		return this.issueHeadId;
	}

	public void setIssueHeadId(Long issueHeadId) {
		this.issueHeadId = issueHeadId;
	}

	@Column(name = "ISSUE_NO", nullable = false, length = 30)
	public String getIssueNo() {
		return this.issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	@Column(name = "PROJECT_CODE", length = 30)
	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Column(name = "WO_NO", length = 15)
	public String getWoNo() {
		return this.woNo;
	}

	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}

	@Column(name = "COST_ITEM_ID", precision = 10, scale = 0)
	public Long getCostItemId() {
		return this.costItemId;
	}

	public void setCostItemId(Long costItemId) {
		this.costItemId = costItemId;
	}

	@Column(name = "MR_NO", length = 20)
	public String getMrNo() {
		return this.mrNo;
	}

	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}

	@Column(name = "RECEIPT_BY", nullable = false, length = 16)
	public String getReceiptBy() {
		return this.receiptBy;
	}

	public void setReceiptBy(String receiptBy) {
		this.receiptBy = receiptBy;
	}

	@Column(name = "RECEIPT_DEP", nullable = false, length = 20)
	public String getReceiptDep() {
		return this.receiptDep;
	}

	public void setReceiptDep(String receiptDep) {
		this.receiptDep = receiptDep;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUE_DATE", nullable = false, length = 7)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "FEE_BY_DEP", length = 10)
	public String getFeeByDep() {
		return this.feeByDep;
	}

	public void setFeeByDep(String feeByDep) {
		this.feeByDep = feeByDep;
	}

	@Column(name = "FEE_BY_SPECIAL", length = 10)
	public String getFeeBySpecial() {
		return this.feeBySpecial;
	}

	public void setFeeBySpecial(String feeBySpecial) {
		this.feeBySpecial = feeBySpecial;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "GET_REAL_PERSON", length = 16)
	public String getGetRealPerson() {
		return this.getRealPerson;
	}

	public void setGetRealPerson(String getRealPerson) {
		this.getRealPerson = getRealPerson;
	}

	@Column(name = "CHECKED_BY", length = 16)
	public String getCheckedBy() {
		return this.checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKED_DATE", length = 7)
	public Date getCheckedDate() {
		return this.checkedDate;
	}

	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "ISSUE_STATUS", nullable = false, length = 1)
	public String getIssueStatus() {
		return this.issueStatus;
	}

	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}

	@Column(name = "IS_EMERGENCY", length = 1)
	public String getIsEmergency() {
		return this.isEmergency;
	}

	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
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

	@Column(name = "PLAN_ORIGINAL_ID", precision = 10, scale = 0)
	public Long getPlanOriginalId() {
		return this.planOriginalId;
	}

	public void setPlanOriginalId(Long planOriginalId) {
		this.planOriginalId = planOriginalId;
	}

	@Column(name = "ITEM_CODE", length = 20)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "REF_ISSUE_NO", length = 30)
	public String getRefIssueNo() {
		return refIssueNo;
	}

	public void setRefIssueNo(String refIssueNo) {
		this.refIssueNo = refIssueNo;
	}

}