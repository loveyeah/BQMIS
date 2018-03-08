package power.ejb.productiontec.technologySupervise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJJdwcy entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_JDWCY")
public class PtJJdwcy implements java.io.Serializable {

	// Fields

	private Long jdwcyId;
	private String workerCode;
	private Long jdzyId;
	private String netDuty;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJJdwcy() {
	}

	/** minimal constructor */
	public PtJJdwcy(Long jdwcyId) {
		this.jdwcyId = jdwcyId;
	}

	/** full constructor */
	public PtJJdwcy(Long jdwcyId, String workerCode, Long jdzyId,
			String netDuty, String enterpriseCode) {
		this.jdwcyId = jdwcyId;
		this.workerCode = workerCode;
		this.jdzyId = jdzyId;
		this.netDuty = netDuty;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDWCY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdwcyId() {
		return this.jdwcyId;
	}

	public void setJdwcyId(Long jdwcyId) {
		this.jdwcyId = jdwcyId;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "JDZY_ID", precision = 2, scale = 0)
	public Long getJdzyId() {
		return this.jdzyId;
	}

	public void setJdzyId(Long jdzyId) {
		this.jdzyId = jdzyId;
	}

	@Column(name = "NET_DUTY", length = 20)
	public String getNetDuty() {
		return this.netDuty;
	}

	public void setNetDuty(String netDuty) {
		this.netDuty = netDuty;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}