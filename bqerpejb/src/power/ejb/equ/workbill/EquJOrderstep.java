package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJOrderstep entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_ORDERSTEP", schema = "POWER")
public class EquJOrderstep implements java.io.Serializable {

	// Fields

	private Long id;
	private String woticketCode;
	private String woCode;
	private String operationStep;
	private String factOperationStepTitle;
	private String factDescription;
	private Double factOpDuration;
	private String planOperationStepTitle;
	private String planDescription;
	private Double planOpDuration;
	private String operationStatus;
	private String factPointName;
	private String planPointName;
	private String runWorktichettypeid;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquJOrderstep() {
	}

	/** minimal constructor */
	public EquJOrderstep(Long id, String woCode, String operationStep,
			String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquJOrderstep(Long id, String woticketCode, String woCode,
			String operationStep, String factOperationStepTitle,
			String factDescription, Double factOpDuration,
			String planOperationStepTitle, String planDescription,
			Double planOpDuration, String operationStatus,
			String factPointName, String planPointName,
			String runWorktichettypeid, Long orderby, String enterprisecode,
			String ifUse) {
		this.id = id;
		this.woticketCode = woticketCode;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.factOperationStepTitle = factOperationStepTitle;
		this.factDescription = factDescription;
		this.factOpDuration = factOpDuration;
		this.planOperationStepTitle = planOperationStepTitle;
		this.planDescription = planDescription;
		this.planOpDuration = planOpDuration;
		this.operationStatus = operationStatus;
		this.factPointName = factPointName;
		this.planPointName = planPointName;
		this.runWorktichettypeid = runWorktichettypeid;
		this.orderby = orderby;
		this.enterprisecode = enterprisecode;
		this.ifUse = ifUse;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "WOTICKET_CODE", length = 30)
	public String getWoticketCode() {
		return this.woticketCode;
	}

	public void setWoticketCode(String woticketCode) {
		this.woticketCode = woticketCode;
	}

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "OPERATION_STEP", nullable = false, length = 20)
	public String getOperationStep() {
		return this.operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

	@Column(name = "FACT_OPERATION_STEP_TITLE", length = 100)
	public String getFactOperationStepTitle() {
		return this.factOperationStepTitle;
	}

	public void setFactOperationStepTitle(String factOperationStepTitle) {
		this.factOperationStepTitle = factOperationStepTitle;
	}

	@Column(name = "FACT_DESCRIPTION", length = 1000)
	public String getFactDescription() {
		return this.factDescription;
	}

	public void setFactDescription(String factDescription) {
		this.factDescription = factDescription;
	}

	@Column(name = "FACT_OP_DURATION", precision = 15, scale = 4)
	public Double getFactOpDuration() {
		return this.factOpDuration;
	}

	public void setFactOpDuration(Double factOpDuration) {
		this.factOpDuration = factOpDuration;
	}

	@Column(name = "PLAN_OPERATION_STEP_TITLE", length = 100)
	public String getPlanOperationStepTitle() {
		return this.planOperationStepTitle;
	}

	public void setPlanOperationStepTitle(String planOperationStepTitle) {
		this.planOperationStepTitle = planOperationStepTitle;
	}

	@Column(name = "PLAN_DESCRIPTION", length = 1000)
	public String getPlanDescription() {
		return this.planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	@Column(name = "PLAN_OP_DURATION", precision = 15, scale = 4)
	public Double getPlanOpDuration() {
		return this.planOpDuration;
	}

	public void setPlanOpDuration(Double planOpDuration) {
		this.planOpDuration = planOpDuration;
	}

	@Column(name = "OPERATION_STATUS", length = 1)
	public String getOperationStatus() {
		return this.operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

	@Column(name = "FACT_POINT_NAME", length = 30)
	public String getFactPointName() {
		return this.factPointName;
	}

	public void setFactPointName(String factPointName) {
		this.factPointName = factPointName;
	}

	@Column(name = "PLAN_POINT_NAME", length = 30)
	public String getPlanPointName() {
		return this.planPointName;
	}

	public void setPlanPointName(String planPointName) {
		this.planPointName = planPointName;
	}

	@Column(name = "RUN_WORKTICHETTYPEID", length = 1)
	public String getRunWorktichettypeid() {
		return this.runWorktichettypeid;
	}

	public void setRunWorktichettypeid(String runWorktichettypeid) {
		this.runWorktichettypeid = runWorktichettypeid;
	}

	@Column(name = "ORDERBY", precision = 10, scale = 0)
	public Long getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Long orderby) {
		this.orderby = orderby;
	}

	@Column(name = "ENTERPRISECODE", nullable = false, length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IF_USE", length = 1)
	public String getIfUse() {
		return this.ifUse;
	}

	public void setIfUse(String ifUse) {
		this.ifUse = ifUse;
	}

}