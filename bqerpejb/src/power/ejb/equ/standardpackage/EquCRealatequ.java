package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCRealatequ entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_REALATEQU")
public class EquCRealatequ implements java.io.Serializable {

	// Fields

	private Long equId;
	private String code;
	private String attributeCode;
	private String status;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCRealatequ() {
	}

	/** minimal constructor */
	public EquCRealatequ(Long equId) {
		this.equId = equId;
	}

	/** full constructor */
	public EquCRealatequ(Long equId, String code, String attributeCode,
			String status, String enterprisecode, String isUse) {
		this.equId = equId;
		this.code = code;
		this.attributeCode = attributeCode;
		this.status = status;
		this.enterprisecode = enterprisecode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EQU_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEquId() {
		return this.equId;
	}

	public void setEquId(Long equId) {
		this.equId = equId;
	}

	@Column(name = "CODE", length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}