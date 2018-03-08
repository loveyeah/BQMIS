package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJWoer entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_WOER", schema = "POWER")
public class EquJWoer implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String failureCode;
	private String enterprisecode;

	// Constructors

	/** default constructor */
	public EquJWoer() {
	}

	/** minimal constructor */
	public EquJWoer(Long id, String woCode, String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquJWoer(Long id, String woCode, String failureCode,
			String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.failureCode = failureCode;
		this.enterprisecode = enterprisecode;
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

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "FAILURE_CODE", length = 15)
	public String getFailureCode() {
		return this.failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	@Column(name = "ENTERPRISECODE", nullable = false, length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

}