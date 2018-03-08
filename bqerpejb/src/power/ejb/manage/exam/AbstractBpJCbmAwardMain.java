package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractBpJCbmAwardMain entity provides the base persistence definition of
 * the BpJCbmAwardMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractBpJCbmAwardMain implements java.io.Serializable {

	// Fields

	private Long declareId;
	private String month;
	private Long workflowNo;
	private String status;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AbstractBpJCbmAwardMain() {
	}

	/** minimal constructor */
	public AbstractBpJCbmAwardMain(Long declareId) {
		this.declareId = declareId;
	}

	/** full constructor */
	public AbstractBpJCbmAwardMain(Long declareId, String month,
			Long workflowNo, String status, String isUse, String enterpriseCode) {
		this.declareId = declareId;
		this.month = month;
		this.workflowNo = workflowNo;
		this.status = status;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DECLARE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDeclareId() {
		return this.declareId;
	}

	public void setDeclareId(Long declareId) {
		this.declareId = declareId;
	}

	@Column(name = "MONTH", length = 10)
	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "WORKFLOW_NO", precision = 22, scale = 0)
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