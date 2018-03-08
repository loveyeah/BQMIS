package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanJobDepMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_JOB_DEP_MAIN", schema = "POWER")
public class BpJPlanJobDepMain implements java.io.Serializable {

	// Fields

	private Long depMainId;
	private Date planTime;
	private String editBy;
	private Date editDate;
	private String editDepcode;
	private String finishEditBy;
	private Date finishEditDate;
	private Long workFlowNo;
	private Long signStatus;
	private Long finishWorkFlowNo;
	private Long finishSignStatus;
	private String ifEquApproval;
	private String enterpriseCode;
	private String ifDiversify;
	private String ifHeating;
	private String ifDirectorApproval;
	private String ifRead;
	private String finishIfRead;

	// Constructors

	/** default constructor */
	public BpJPlanJobDepMain() {
	}

	/** minimal constructor */
	public BpJPlanJobDepMain(Long depMainId) {
		this.depMainId = depMainId;
	}

	/** full constructor */
	public BpJPlanJobDepMain(Long depMainId, Date planTime, String editBy,
			Date editDate, String editDepcode, String finishEditBy,
			Date finishEditDate, Long workFlowNo, Long signStatus,
			Long finishWorkFlowNo, Long finishSignStatus, String ifEquApproval,
			String enterpriseCode, String ifDiversify, String ifHeating,
			String ifDirectorApproval, String ifRead, String finishIfRead) {
		this.depMainId = depMainId;
		this.planTime = planTime;
		this.editBy = editBy;
		this.editDate = editDate;
		this.editDepcode = editDepcode;
		this.finishEditBy = finishEditBy;
		this.finishEditDate = finishEditDate;
		this.workFlowNo = workFlowNo;
		this.signStatus = signStatus;
		this.finishWorkFlowNo = finishWorkFlowNo;
		this.finishSignStatus = finishSignStatus;
		this.ifEquApproval = ifEquApproval;
		this.enterpriseCode = enterpriseCode;
		this.ifDiversify = ifDiversify;
		this.ifHeating = ifHeating;
		this.ifDirectorApproval = ifDirectorApproval;
		this.ifRead = ifRead;
		this.finishIfRead = finishIfRead;
	}

	// Property accessors
	@Id
	@Column(name = "DEP_MAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDepMainId() {
		return this.depMainId;
	}

	public void setDepMainId(Long depMainId) {
		this.depMainId = depMainId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_TIME", length = 7)
	public Date getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	@Column(name = "EDIT_BY", length = 16)
	public String getEditBy() {
		return this.editBy;
	}

	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EDIT_DATE", length = 7)
	public Date getEditDate() {
		return this.editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	@Column(name = "EDIT_DEPCODE", length = 20)
	public String getEditDepcode() {
		return this.editDepcode;
	}

	public void setEditDepcode(String editDepcode) {
		this.editDepcode = editDepcode;
	}

	@Column(name = "FINISH_EDIT_BY", length = 16)
	public String getFinishEditBy() {
		return this.finishEditBy;
	}

	public void setFinishEditBy(String finishEditBy) {
		this.finishEditBy = finishEditBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FINISH_EDIT_DATE", length = 7)
	public Date getFinishEditDate() {
		return this.finishEditDate;
	}

	public void setFinishEditDate(Date finishEditDate) {
		this.finishEditDate = finishEditDate;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "SIGN_STATUS", precision = 2, scale = 0)
	public Long getSignStatus() {
		return this.signStatus;
	}

	public void setSignStatus(Long signStatus) {
		this.signStatus = signStatus;
	}

	@Column(name = "FINISH_WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getFinishWorkFlowNo() {
		return this.finishWorkFlowNo;
	}

	public void setFinishWorkFlowNo(Long finishWorkFlowNo) {
		this.finishWorkFlowNo = finishWorkFlowNo;
	}

	@Column(name = "FINISH_SIGN_STATUS", precision = 2, scale = 0)
	public Long getFinishSignStatus() {
		return this.finishSignStatus;
	}

	public void setFinishSignStatus(Long finishSignStatus) {
		this.finishSignStatus = finishSignStatus;
	}

	@Column(name = "IF_EQU_APPROVAL", length = 1)
	public String getIfEquApproval() {
		return this.ifEquApproval;
	}

	public void setIfEquApproval(String ifEquApproval) {
		this.ifEquApproval = ifEquApproval;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IF_DIVERSIFY", length = 1)
	public String getIfDiversify() {
		return this.ifDiversify;
	}

	public void setIfDiversify(String ifDiversify) {
		this.ifDiversify = ifDiversify;
	}

	@Column(name = "IF_HEATING", length = 1)
	public String getIfHeating() {
		return this.ifHeating;
	}

	public void setIfHeating(String ifHeating) {
		this.ifHeating = ifHeating;
	}

	@Column(name = "IF_DIRECTOR_APPROVAL", length = 1)
	public String getIfDirectorApproval() {
		return this.ifDirectorApproval;
	}

	public void setIfDirectorApproval(String ifDirectorApproval) {
		this.ifDirectorApproval = ifDirectorApproval;
	}

	@Column(name = "IF_READ", length = 1)
	public String getIfRead() {
		return this.ifRead;
	}

	public void setIfRead(String ifRead) {
		this.ifRead = ifRead;
	}

	@Column(name = "FINISH_IF_READ", length = 1)
	public String getFinishIfRead() {
		return this.finishIfRead;
	}

	public void setFinishIfRead(String finishIfRead) {
		this.finishIfRead = finishIfRead;
	}

}