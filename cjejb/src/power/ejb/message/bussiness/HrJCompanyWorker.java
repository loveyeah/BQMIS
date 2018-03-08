package power.ejb.message.bussiness;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJCompanyWorker entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_COMPANY_WORKER")
public class HrJCompanyWorker implements java.io.Serializable {

	// Fields

	private Long id;
	private String zbbmtxCode;
	private String workerCode;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJCompanyWorker() {
	}

	/** minimal constructor */
	public HrJCompanyWorker(Long id) {
		this.id = id;
	}

	/** full constructor */
	public HrJCompanyWorker(Long id, String zbbmtxCode, String workerCode,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.zbbmtxCode = zbbmtxCode;
		this.workerCode = workerCode;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
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

	@Column(name = "ZBBMTX_CODE", length = 3)
	public String getZbbmtxCode() {
		return this.zbbmtxCode;
	}

	public void setZbbmtxCode(String zbbmtxCode) {
		this.zbbmtxCode = zbbmtxCode;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
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

}