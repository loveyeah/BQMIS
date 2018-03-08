package power.ejb.manage.plan.itemplan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJItemplanTecMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_ITEMPLAN_TEC_MAIN")
public class BpJItemplanTecMain implements java.io.Serializable {

	// Fields

	private Long tecMainId;
	private Date month;
	private Long workflowNoPlan;
	private Long workflowStatusPlan;
	private Long workflowNoFact;
	private Long workflowStatusFact;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJItemplanTecMain() {
	}

	/** minimal constructor */
	public BpJItemplanTecMain(Long tecMainId) {
		this.tecMainId = tecMainId;
	}

	/** full constructor */
	public BpJItemplanTecMain(Long tecMainId, Date month, Long workflowNoPlan,
			Long workflowStatusPlan, Long workflowNoFact,
			Long workflowStatusFact, String isUse, String enterpriseCode) {
		this.tecMainId = tecMainId;
		this.month = month;
		this.workflowNoPlan = workflowNoPlan;
		this.workflowStatusPlan = workflowStatusPlan;
		this.workflowNoFact = workflowNoFact;
		this.workflowStatusFact = workflowStatusFact;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TEC_MAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTecMainId() {
		return this.tecMainId;
	}

	public void setTecMainId(Long tecMainId) {
		this.tecMainId = tecMainId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH", length = 7)
	public Date getMonth() {
		return this.month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	@Column(name = "WORKFLOW_NO_PLAN", precision = 22, scale = 0)
	public Long getWorkflowNoPlan() {
		return this.workflowNoPlan;
	}

	public void setWorkflowNoPlan(Long workflowNoPlan) {
		this.workflowNoPlan = workflowNoPlan;
	}

	@Column(name = "WORKFLOW_STATUS_PLAN", precision = 1, scale = 0)
	public Long getWorkflowStatusPlan() {
		return this.workflowStatusPlan;
	}

	public void setWorkflowStatusPlan(Long workflowStatusPlan) {
		this.workflowStatusPlan = workflowStatusPlan;
	}

	@Column(name = "WORKFLOW_NO_FACT", precision = 22, scale = 0)
	public Long getWorkflowNoFact() {
		return this.workflowNoFact;
	}

	public void setWorkflowNoFact(Long workflowNoFact) {
		this.workflowNoFact = workflowNoFact;
	}

	@Column(name = "WORKFLOW_STATUS_FACT", precision = 1, scale = 0)
	public Long getWorkflowStatusFact() {
		return this.workflowStatusFact;
	}

	public void setWorkflowStatusFact(Long workflowStatusFact) {
		this.workflowStatusFact = workflowStatusFact;
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

}