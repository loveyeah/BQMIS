package power.ejb.manage.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "CON_C_STATUS")
public class ConCStatus implements java.io.Serializable {

	// Fields

	private Long id;
	private Long workFlowNo;
	private String deptCodes;

	// Constructors

	/** default constructor */
	public ConCStatus() {
	}

	/** minimal constructor */
	public ConCStatus(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ConCStatus(Long id, Long workFlowNo, String deptCodes) {
		this.id = id;
		this.workFlowNo = workFlowNo;
		this.deptCodes = deptCodes;
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

	@Column(name = "WORK_FLOW_NO", precision = 10, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "DEPT_CODES", length = 70)
	public String getDeptCodes() {
		return this.deptCodes;
	}

	public void setDeptCodes(String deptCodes) {
		this.deptCodes = deptCodes;
	}

}