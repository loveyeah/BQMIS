package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJModify entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_MODIFY")
public class ConJModify implements java.io.Serializable {

	// Fields

	private Long conModifyId;
	private String conModifyNo;
	private Long conId;
	private String operateBy;
	private String operateDepCode;
	private String operateLeadBy;
	private Long conomodifyType;
	private Double actAmount;
	private Double modiyActAmount;
	private String conomodifyName;
	private String memo;
	private String fileStatus;
	private String fileNo;
	private String fileBy;
	private Date fileDate;
	private String fileMemo;
	private String pageCount;
	private Long workFlowNo;
	private Long workflowStatus;
	private String entryBy;
	private Date entryDate;
	private Date signStartDate;
	private Date signEndDate;
	private String enterpriseCode;
	private String isUse;
	private Long fileCount;

	//判断当前审批部门
	private String deptFlg;
	// Constructors

	/** default constructor */
	public ConJModify() {
	}

	/** minimal constructor */
	public ConJModify(Long conModifyId, String conModifyNo,
			Long conomodifyType, String fileStatus, String entryBy,
			Date entryDate, String enterpriseCode, String isUse) {
		this.conModifyId = conModifyId;
		this.conModifyNo = conModifyNo;
		this.conomodifyType = conomodifyType;
		this.fileStatus = fileStatus;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJModify(Long conModifyId, String conModifyNo, Long conId,
			String operateBy, String operateDepCode, String operateLeadBy,
			Long conomodifyType, Double actAmount, Double modiyActAmount,
			String conomodifyName, String memo, String fileStatus,
			String fileNo, String fileBy, Date fileDate, String fileMemo,
			String pageCount, Long workFlowNo, Long workflowStatus,
			String entryBy, Date entryDate, Date signStartDate,
			Date signEndDate, String enterpriseCode, String isUse,
			Long fileCount) {
		this.conModifyId = conModifyId;
		this.conModifyNo = conModifyNo;
		this.conId = conId;
		this.operateBy = operateBy;
		this.operateDepCode = operateDepCode;
		this.operateLeadBy = operateLeadBy;
		this.conomodifyType = conomodifyType;
		this.actAmount = actAmount;
		this.modiyActAmount = modiyActAmount;
		this.conomodifyName = conomodifyName;
		this.memo = memo;
		this.fileStatus = fileStatus;
		this.fileNo = fileNo;
		this.fileBy = fileBy;
		this.fileDate = fileDate;
		this.fileMemo = fileMemo;
		this.pageCount = pageCount;
		this.workFlowNo = workFlowNo;
		this.workflowStatus = workflowStatus;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.signStartDate = signStartDate;
		this.signEndDate = signEndDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.fileCount = fileCount;
	}

	// Property accessors
	@Id
	@Column(name = "CON_MODIFY_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getConModifyId() {
		return this.conModifyId;
	}

	public void setConModifyId(Long conModifyId) {
		this.conModifyId = conModifyId;
	}

	@Column(name = "CON_MODIFY_NO", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getConModifyNo() {
		return this.conModifyNo;
	}

	public void setConModifyNo(String conModifyNo) {
		this.conModifyNo = conModifyNo;
	}

	@Column(name = "CON_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "OPERATE_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getOperateBy() {
		return this.operateBy;
	}

	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}

	@Column(name = "OPERATE_DEP_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getOperateDepCode() {
		return this.operateDepCode;
	}

	public void setOperateDepCode(String operateDepCode) {
		this.operateDepCode = operateDepCode;
	}

	@Column(name = "OPERATE_LEAD_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getOperateLeadBy() {
		return this.operateLeadBy;
	}

	public void setOperateLeadBy(String operateLeadBy) {
		this.operateLeadBy = operateLeadBy;
	}

	@Column(name = "CONOMODIFY_TYPE", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public Long getConomodifyType() {
		return this.conomodifyType;
	}

	public void setConomodifyType(Long conomodifyType) {
		this.conomodifyType = conomodifyType;
	}

	@Column(name = "ACT_AMOUNT", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getActAmount() {
		return this.actAmount;
	}

	public void setActAmount(Double actAmount) {
		this.actAmount = actAmount;
	}

	@Column(name = "MODIY_ACT_AMOUNT", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getModiyActAmount() {
		return this.modiyActAmount;
	}

	public void setModiyActAmount(Double modiyActAmount) {
		this.modiyActAmount = modiyActAmount;
	}

	@Column(name = "CONOMODIFY_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
	public String getConomodifyName() {
		return this.conomodifyName;
	}

	public void setConomodifyName(String conomodifyName) {
		this.conomodifyName = conomodifyName;
	}

	@Column(name = "MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "FILE_STATUS", unique = false, nullable = false, insertable = true, updatable = true, length = 3)
	public String getFileStatus() {
		return this.fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	@Column(name = "FILE_NO", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getFileNo() {
		return this.fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	@Column(name = "FILE_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getFileBy() {
		return this.fileBy;
	}

	public void setFileBy(String fileBy) {
		this.fileBy = fileBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getFileDate() {
		return this.fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	@Column(name = "FILE_MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getFileMemo() {
		return this.fileMemo;
	}

	public void setFileMemo(String fileMemo) {
		this.fileMemo = fileMemo;
	}

	@Column(name = "PAGE_COUNT", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	@Column(name = "WORK_FLOW_NO", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WORKFLOW_STATUS", unique = false, nullable = true, insertable = true, updatable = true, precision = 11, scale = 0)
	public Long getWorkflowStatus() {
		return this.workflowStatus;
	}

	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Column(name = "ENTRY_BY", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SIGN_START_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getSignStartDate() {
		return this.signStartDate;
	}

	public void setSignStartDate(Date signStartDate) {
		this.signStartDate = signStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SIGN_END_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getSignEndDate() {
		return this.signEndDate;
	}

	public void setSignEndDate(Date signEndDate) {
		this.signEndDate = signEndDate;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "FILE_COUNT", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
	public Long getFileCount() {
		return this.fileCount;
	}

	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
	}

	@Column(name = "DEPT_FLG",length = 20)
	public String getDeptFlg() {
		return deptFlg;
	}

	public void setDeptFlg(String deptFlg) {
		this.deptFlg = deptFlg;
	}

}