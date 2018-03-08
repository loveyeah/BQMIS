package power.ejb.equ.change;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJEquchang entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_EQUCHANG")
public class EquJEquchang implements java.io.Serializable {

	// Fields

	private Long equChangeId;
	private String equChangeNo;
	private String assetnum;
	private String equOldCode;
	private String equNewCode;
	private String equName;
	private String specialCode;
	private String changeTitle;
	private String sourceCode;
	private String changeReason;
	private String changeType;
	private Date changePlanDate;
	private String frontThing;
	private String backThing;
	private String applyMan;
	private Date applyDate;
	private String workFlowNo;
	private String wfState;
	private String enterpriseCode;
	private String isUse;
	private String deptCode;
	private String fileName;
	private String annex ;

	// Constructors

	/** default constructor */
	public EquJEquchang() {
	}

	/** minimal constructor */
	public EquJEquchang(Long equChangeId) {
		this.equChangeId = equChangeId;
	}

	/** full constructor */
	public EquJEquchang(Long equChangeId, String equChangeNo, String assetnum,
			String equOldCode, String equNewCode, String equName,
			String specialCode, String changeTitle, String sourceCode,
			String changeReason, String changeType, Date changePlanDate,
			String frontThing, String backThing, String applyMan,
			Date applyDate, String workFlowNo, String wfState,
			String enterpriseCode, String isUse, String deptCode) {
		this.equChangeId = equChangeId;
		this.equChangeNo = equChangeNo;
		this.assetnum = assetnum;
		this.equOldCode = equOldCode;
		this.equNewCode = equNewCode;
		this.equName = equName;
		this.specialCode = specialCode;
		this.changeTitle = changeTitle;
		this.sourceCode = sourceCode;
		this.changeReason = changeReason;
		this.changeType = changeType;
		this.changePlanDate = changePlanDate;
		this.frontThing = frontThing;
		this.backThing = backThing;
		this.applyMan = applyMan;
		this.applyDate = applyDate;
		this.workFlowNo = workFlowNo;
		this.wfState = wfState;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.deptCode = deptCode;
	}

	// Property accessors
	@Id
	@Column(name = "EQU_CHANGE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEquChangeId() {
		return this.equChangeId;
	}

	public void setEquChangeId(Long equChangeId) {
		this.equChangeId = equChangeId;
	}

	@Column(name = "EQU_CHANGE_NO", length = 20)
	public String getEquChangeNo() {
		return this.equChangeNo;
	}

	public void setEquChangeNo(String equChangeNo) {
		this.equChangeNo = equChangeNo;
	}

	@Column(name = "ASSETNUM", length = 20)
	public String getAssetnum() {
		return this.assetnum;
	}

	public void setAssetnum(String assetnum) {
		this.assetnum = assetnum;
	}

	@Column(name = "EQU_OLD_CODE", length = 30)
	public String getEquOldCode() {
		return this.equOldCode;
	}

	public void setEquOldCode(String equOldCode) {
		this.equOldCode = equOldCode;
	}

	@Column(name = "EQU_NEW_CODE", length = 30)
	public String getEquNewCode() {
		return this.equNewCode;
	}

	public void setEquNewCode(String equNewCode) {
		this.equNewCode = equNewCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "SPECIAL_CODE", length = 10)
	public String getSpecialCode() {
		return this.specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}

	@Column(name = "CHANGE_TITLE", length = 200)
	public String getChangeTitle() {
		return this.changeTitle;
	}

	public void setChangeTitle(String changeTitle) {
		this.changeTitle = changeTitle;
	}

	@Column(name = "SOURCE_CODE", length = 10)
	public String getSourceCode() {
		return this.sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	@Column(name = "CHANGE_REASON", length = 1000)
	public String getChangeReason() {
		return this.changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	@Column(name = "CHANGE_TYPE", length = 10)
	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHANGE_PLAN_DATE", length = 7)
	public Date getChangePlanDate() {
		return this.changePlanDate;
	}

	public void setChangePlanDate(Date changePlanDate) {
		this.changePlanDate = changePlanDate;
	}

	@Column(name = "FRONT_THING", length = 600)
	public String getFrontThing() {
		return this.frontThing;
	}

	public void setFrontThing(String frontThing) {
		this.frontThing = frontThing;
	}

	@Column(name = "BACK_THING", length = 600)
	public String getBackThing() {
		return this.backThing;
	}

	public void setBackThing(String backThing) {
		this.backThing = backThing;
	}

	@Column(name = "APPLY_MAN", length = 30)
	public String getApplyMan() {
		return this.applyMan;
	}

	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_DATE", length = 7)
	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "WORK_FLOW_NO", length = 36)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WF_STATE", length = 30)
	public String getWfState() {
		return this.wfState;
	}

	public void setWfState(String wfState) {
		this.wfState = wfState;
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

	@Column(name = "DEPT_CODE", length = 200)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Column(name = "ANNEX", length = 400)
	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

}