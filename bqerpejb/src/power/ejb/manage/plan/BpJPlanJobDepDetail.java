package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJPlanJobDepDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_JOB_DEP_DETAIL", schema = "POWER")
public class BpJPlanJobDepDetail implements java.io.Serializable {

	// Fields

	private Long jobId;
	private Long depMainId;
	private String jobContent;
	private String ifComplete;
	private String completeDesc;
	private String completeData;
	private String enterpriseCode;
	private String chargeBy;
	private String orderBy;

	// Constructors

	/** default constructor */
	public BpJPlanJobDepDetail() {
	}

	/** minimal constructor */
	public BpJPlanJobDepDetail(Long jobId) {
		this.jobId = jobId;
	}

	/** full constructor */
	public BpJPlanJobDepDetail(Long jobId, Long depMainId, String jobContent,
			String ifComplete, String completeDesc, String completeData,
			String enterpriseCode, String chargeBy,String orderBy) {
		this.jobId = jobId;
		this.depMainId = depMainId;
		this.jobContent = jobContent;
		this.ifComplete = ifComplete;
		this.completeDesc = completeDesc;
		this.completeData = completeData;
		this.enterpriseCode = enterpriseCode;
		this.chargeBy = chargeBy;
		this.orderBy = orderBy;
	}

	// Property accessors
	@Id
	@Column(name = "JOB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "DEP_MAIN_ID", precision = 10, scale = 0)
	public Long getDepMainId() {
		return this.depMainId;
	}

	public void setDepMainId(Long depMainId) {
		this.depMainId = depMainId;
	}

	@Column(name = "JOB_CONTENT", length = 500)
	public String getJobContent() {
		return this.jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	@Column(name = "IF_COMPLETE", length = 1)
	public String getIfComplete() {
		return this.ifComplete;
	}

	public void setIfComplete(String ifComplete) {
		this.ifComplete = ifComplete;
	}

	@Column(name = "COMPLETE_DESC", length = 200)
	public String getCompleteDesc() {
		return this.completeDesc;
	}

	public void setCompleteDesc(String completeDesc) {
		this.completeDesc = completeDesc;
	}

	@Column(name = "COMPLETE_DATA", length = 1)
	public String getCompleteData() {
		return this.completeData;
	}

	public void setCompleteData(String completeData) {
		this.completeData = completeData;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}
	@Column(name = "ORDER_BY", length = 20)
	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}