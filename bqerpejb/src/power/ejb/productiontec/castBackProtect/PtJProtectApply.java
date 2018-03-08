package power.ejb.productiontec.castBackProtect;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJProtectApply entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_PROTECT_APPLY")
public class PtJProtectApply implements java.io.Serializable {

	// Fields

	private Long applyId;
	private String applyCode;
	private String inOut;
	private String protectionType;
	private String applyDep;
	private String applyBy;
	private Date applyTime;
	private String protectionName;
	private String protectionReason;
	private String measures;
	private Long workflowNo;
	private String status;
	private String classLeader;
	private String executor;
	private String guardian;
	private Date executeTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private String blockId;
	
	// Constructors

	/** default constructor */
	public PtJProtectApply() {
	}

	/** minimal constructor */
	public PtJProtectApply(Long applyId) {
		this.applyId = applyId;
	}

	/** full constructor */
	public PtJProtectApply(Long applyId, String applyCode, String inOut,
			String protectionType, String applyDep, String applyBy,
			Date applyTime, String protectionName, String protectionReason,
			String measures, Long workflowNo, String status,
			String classLeader, String executor, String guardian,
			Date executeTime, String memo, String isUse, String enterpriseCode,String blockId) {
		this.applyId = applyId;
		this.applyCode = applyCode;
		this.inOut = inOut;
		this.protectionType = protectionType;
		this.applyDep = applyDep;
		this.applyBy = applyBy;
		this.applyTime = applyTime;
		this.protectionName = protectionName;
		this.protectionReason = protectionReason;
		this.measures = measures;
		this.workflowNo = workflowNo;
		this.status = status;
		this.classLeader = classLeader;
		this.executor = executor;
		this.guardian = guardian;
		this.executeTime = executeTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.blockId = blockId;
	}

	// Property accessors
	@Id
	@Column(name = "APPLY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApplyId() {
		return this.applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	@Column(name = "APPLY_CODE", length = 100)
	public String getApplyCode() {
		return this.applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	@Column(name = "IN_OUT", length = 1)
	public String getInOut() {
		return this.inOut;
	}

	public void setInOut(String inOut) {
		this.inOut = inOut;
	}

	@Column(name = "PROTECTION_TYPE", length = 1)
	public String getProtectionType() {
		return this.protectionType;
	}

	public void setProtectionType(String protectionType) {
		this.protectionType = protectionType;
	}

	@Column(name = "APPLY_DEP", length = 20)
	public String getApplyDep() {
		return this.applyDep;
	}

	public void setApplyDep(String applyDep) {
		this.applyDep = applyDep;
	}

	@Column(name = "APPLY_BY", length = 20)
	public String getApplyBy() {
		return this.applyBy;
	}

	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_TIME", length = 7)
	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	@Column(name = "PROTECTION_NAME", length = 500)
	public String getProtectionName() {
		return this.protectionName;
	}

	public void setProtectionName(String protectionName) {
		this.protectionName = protectionName;
	}

	@Column(name = "PROTECTION_REASON", length = 1000)
	public String getProtectionReason() {
		return this.protectionReason;
	}

	public void setProtectionReason(String protectionReason) {
		this.protectionReason = protectionReason;
	}

	@Column(name = "MEASURES", length = 1000)
	public String getMeasures() {
		return this.measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	@Column(name = "WORKFLOW_NO", precision = 10, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CLASS_LEADER", length = 20)
	public String getClassLeader() {
		return this.classLeader;
	}

	public void setClassLeader(String classLeader) {
		this.classLeader = classLeader;
	}

	@Column(name = "EXECUTOR", length = 20)
	public String getExecutor() {
		return this.executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	@Column(name = "GUARDIAN", length = 20)
	public String getGuardian() {
		return this.guardian;
	}

	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXECUTE_TIME", length = 7)
	public Date getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	@Column(name = "MEMO", length = 1000)
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

	@Column(name = "BLOCK_ID", length = 1)
	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

}