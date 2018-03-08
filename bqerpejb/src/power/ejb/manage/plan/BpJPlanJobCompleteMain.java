package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanJobCompleteMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_JOB_COMPLETE_MAIN", schema = "POWER")
public class BpJPlanJobCompleteMain implements java.io.Serializable {

	// Fields

	private Long depMainId;
	private Date planTime;
	private String editDepcode;
	private String finishEditBy;
	private Date finishEditDate;
	private Long finishWorkFlowNo;
	private Long finishSignStatus;
	private String ifDiversify;
	private String ifHeating;
	private String finishIfRead;
	private String ifEquApproval;
	private String ifDirectorApproval;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanJobCompleteMain() {
	}

	/** minimal constructor */
	public BpJPlanJobCompleteMain(Long depMainId) {
		this.depMainId = depMainId;
	}

	/** full constructor */
	public BpJPlanJobCompleteMain(Long depMainId, Date planTime,
			String editDepcode, String finishEditBy, Date finishEditDate,
			Long finishWorkFlowNo, Long finishSignStatus, String ifDiversify,
			String ifHeating, String finishIfRead, String ifEquApproval,
			String ifDirectorApproval, String enterpriseCode) {
		this.depMainId = depMainId;
		this.planTime = planTime;
		this.editDepcode = editDepcode;
		this.finishEditBy = finishEditBy;
		this.finishEditDate = finishEditDate;
		this.finishWorkFlowNo = finishWorkFlowNo;
		this.finishSignStatus = finishSignStatus;
		this.ifDiversify = ifDiversify;
		this.ifHeating = ifHeating;
		this.finishIfRead = finishIfRead;
		this.ifEquApproval = ifEquApproval;
		this.ifDirectorApproval = ifDirectorApproval;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "FINISH_IF_READ", length = 1)
	public String getFinishIfRead() {
		return this.finishIfRead;
	}

	public void setFinishIfRead(String finishIfRead) {
		this.finishIfRead = finishIfRead;
	}

	@Column(name = "IF_EQU_APPROVAL", length = 1)
	public String getIfEquApproval() {
		return this.ifEquApproval;
	}

	public void setIfEquApproval(String ifEquApproval) {
		this.ifEquApproval = ifEquApproval;
	}

	@Column(name = "IF_DIRECTOR_APPROVAL", length = 1)
	public String getIfDirectorApproval() {
		return this.ifDirectorApproval;
	}

	public void setIfDirectorApproval(String ifDirectorApproval) {
		this.ifDirectorApproval = ifDirectorApproval;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}