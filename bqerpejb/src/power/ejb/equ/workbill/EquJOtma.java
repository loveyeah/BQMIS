package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJOtma entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_OTMA", schema = "POWER")
public class EquJOtma implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String matCode;
	private String enterprisecode;

	// Constructors

	/** default constructor */
	public EquJOtma() {
	}

	/** minimal constructor */
	public EquJOtma(Long id, String woCode, String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquJOtma(Long id, String woCode, String matCode,
			String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.matCode = matCode;
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

	@Column(name = "MAT_CODE", length = 30)
	public String getMatCode() {
		return this.matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	@Column(name = "ENTERPRISECODE", nullable = false, length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

}