package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJWoPenotion entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_WO_PENOTION", schema = "POWER")
public class EquJWoPenotion implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String peNotion;

	// Constructors

	/** default constructor */
	public EquJWoPenotion() {
	}

	/** minimal constructor */
	public EquJWoPenotion(Long id, String woCode) {
		this.id = id;
		this.woCode = woCode;
	}

	/** full constructor */
	public EquJWoPenotion(Long id, String woCode, String peNotion) {
		this.id = id;
		this.woCode = woCode;
		this.peNotion = peNotion;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
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

	@Column(name = "PE_NOTION", length = 4000)
	public String getPeNotion() {
		return this.peNotion;
	}

	public void setPeNotion(String peNotion) {
		this.peNotion = peNotion;
	}

}