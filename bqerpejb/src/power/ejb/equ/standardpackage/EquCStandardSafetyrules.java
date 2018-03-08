package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardSafetyrules entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_SAFETYRULES")
public class EquCStandardSafetyrules implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String opstepCode;
	private String content;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardSafetyrules() {
	}

	/** minimal constructor */
	public EquCStandardSafetyrules(Long id, String opstepCode,
			String enterprisecode) {
		this.id = id;
		this.opstepCode = opstepCode;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardSafetyrules(Long id, String woCode, String opstepCode,
			String content, String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.opstepCode = opstepCode;
		this.content = content;
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

	@Column(name = "WO_CODE", length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "OPSTEP_CODE", nullable = false, length = 20)
	public String getOpstepCode() {
		return this.opstepCode;
	}

	public void setOpstepCode(String opstepCode) {
		this.opstepCode = opstepCode;
	}

	@Column(name = "CONTENT", length = 1000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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