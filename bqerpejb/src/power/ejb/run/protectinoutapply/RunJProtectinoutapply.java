package power.ejb.run.protectinoutapply;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJProtectinoutapply entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_PROTECTINOUTAPPLY")
public class RunJProtectinoutapply implements java.io.Serializable {

	// Fields

	private Long applyId;
	private String protectNo;
	private String applyBy;
	private String applyDept;
	private Date applyDate;
	private String equCode;
	private String protectName;
	private String protectReason;
	private String equEffect;
	private String outSafety;
	private Date applyStartDate;
	private Date applyEndDate;
	private String executeBy;
	private String keeper;
	private String permitBy;
	private String checkupBy;
	private Date protectInDate;
	private Date protectOutDate;
	private String memo;
	private Long workFlowNo;
	private Long statusId;
	private String lastModifyBy;
	private Date lastModifyDate;
	private String enterpriseCode;
	private String isUse;
	
	private Date approveStartDate;
	private Date approveEndDate;
	private String isIn;
	private String isSelect;
	private String relativeNo;

	// Constructors
	@Column(name = "IS_SELECT")
	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
	@Column(name = "RELATIVE_NO")
	public String getRelativeNo() {
		return relativeNo;
	}

	public void setRelativeNo(String relativeNo) {
		this.relativeNo = relativeNo;
	}

	/** default constructor */
	public RunJProtectinoutapply() {
	}

	/** minimal constructor */
	public RunJProtectinoutapply(Long applyId) {
		this.applyId = applyId;
	}

	/** full constructor */
	public RunJProtectinoutapply(Long applyId, String protectNo,
			String applyBy, String applyDept, Date applyDate, String equCode,
			String protectName, String protectReason, String equEffect,
			String outSafety, Date applyStartDate, Date applyEndDate,
			String executeBy, String keeper, String permitBy, String checkupBy,
			Date protectInDate, Date protectOutDate, String memo,
			Long workFlowNo, Long statusId, String lastModifyBy,
			Date lastModifyDate, String enterpriseCode, String isUse,Date approveStartDate,Date approveEndDate) {
		this.applyId = applyId;
		this.protectNo = protectNo;
		this.applyBy = applyBy;
		this.applyDept = applyDept;
		this.applyDate = applyDate;
		this.equCode = equCode;
		this.protectName = protectName;
		this.protectReason = protectReason;
		this.equEffect = equEffect;
		this.outSafety = outSafety;
		this.applyStartDate = applyStartDate;
		this.applyEndDate = applyEndDate;
		this.executeBy = executeBy;
		this.keeper = keeper;
		this.permitBy = permitBy;
		this.checkupBy = checkupBy;
		this.protectInDate = protectInDate;
		this.protectOutDate = protectOutDate;
		this.memo = memo;
		this.workFlowNo = workFlowNo;
		this.statusId = statusId;
		this.lastModifyBy = lastModifyBy;
		this.lastModifyDate = lastModifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.approveStartDate=approveStartDate;
		this.approveEndDate=approveEndDate;
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

	@Column(name = "PROTECT_NO", length = 15)
	public String getProtectNo() {
		return this.protectNo;
	}

	public void setProtectNo(String protectNo) {
		this.protectNo = protectNo;
	}

	@Column(name = "APPLY_BY", length = 30)
	public String getApplyBy() {
		return this.applyBy;
	}

	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}

	@Column(name = "APPLY_DEPT", length = 20)
	public String getApplyDept() {
		return this.applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_DATE", length = 7)
	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return this.equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	@Column(name = "PROTECT_NAME", length = 200)
	public String getProtectName() {
		return this.protectName;
	}

	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}

	@Column(name = "PROTECT_REASON", length = 200)
	public String getProtectReason() {
		return this.protectReason;
	}

	public void setProtectReason(String protectReason) {
		this.protectReason = protectReason;
	}

	@Column(name = "EQU_EFFECT", length = 200)
	public String getEquEffect() {
		return this.equEffect;
	}

	public void setEquEffect(String equEffect) {
		this.equEffect = equEffect;
	}

	@Column(name = "OUT_SAFETY", length = 200)
	public String getOutSafety() {
		return this.outSafety;
	}

	public void setOutSafety(String outSafety) {
		this.outSafety = outSafety;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_START_DATE", length = 7)
	public Date getApplyStartDate() {
		return this.applyStartDate;
	}

	public void setApplyStartDate(Date applyStartDate) {
		this.applyStartDate = applyStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_END_DATE", length = 7)
	public Date getApplyEndDate() {
		return this.applyEndDate;
	}

	public void setApplyEndDate(Date applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	@Column(name = "EXECUTE_BY", length = 30)
	public String getExecuteBy() {
		return this.executeBy;
	}

	public void setExecuteBy(String executeBy) {
		this.executeBy = executeBy;
	}

	@Column(name = "KEEPER", length = 30)
	public String getKeeper() {
		return this.keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}

	@Column(name = "PERMIT_BY", length = 30)
	public String getPermitBy() {
		return this.permitBy;
	}

	public void setPermitBy(String permitBy) {
		this.permitBy = permitBy;
	}

	@Column(name = "CHECKUP_BY", length = 30)
	public String getCheckupBy() {
		return this.checkupBy;
	}

	public void setCheckupBy(String checkupBy) {
		this.checkupBy = checkupBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROTECT_IN_DATE", length = 7)
	public Date getProtectInDate() {
		return this.protectInDate;
	}

	public void setProtectInDate(Date protectInDate) {
		this.protectInDate = protectInDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROTECT_OUT_DATE", length = 7)
	public Date getProtectOutDate() {
		return this.protectOutDate;
	}

	public void setProtectOutDate(Date protectOutDate) {
		this.protectOutDate = protectOutDate;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "STATUS_ID", precision = 10, scale = 0)
	public Long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "LAST_MODIFY_BY", length = 30)
	public String getLastModifyBy() {
		return this.lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFY_DATE", length = 7)
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_START_DATE", length = 7)
	public Date getApproveStartDate() {
		return approveStartDate;
	}

	public void setApproveStartDate(Date approveStartDate) {
		this.approveStartDate = approveStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_END_DATE", length = 7)
	public Date getApproveEndDate() {
		return approveEndDate;
	}
	
	public void setApproveEndDate(Date approveEndDate) {
		this.approveEndDate = approveEndDate;
	}

	@Column(name = "IS_IN")
	public String getIsIn() {
		return isIn;
	}

	public void setIsIn(String isIn) {
		this.isIn = isIn;
	}

}