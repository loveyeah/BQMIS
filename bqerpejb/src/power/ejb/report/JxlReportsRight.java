package power.ejb.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JxlReportsRight entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "JXL_REPORTS_RIGHT", schema = "POWER")
public class JxlReportsRight implements java.io.Serializable {

	// Fields

	private Long id;
	private String code;
	private String workerCode;

	// Constructors

	/** default constructor */
	public JxlReportsRight() {
	}

	/** minimal constructor */
	public JxlReportsRight(Long id) {
		this.id = id;
	}

	/** full constructor */
	public JxlReportsRight(Long id, String code, String workerCode) {
		this.id = id;
		this.code = code;
		this.workerCode = workerCode;
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

	@Column(name = "CODE", length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "WORKER_CODE", length = 20)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

}