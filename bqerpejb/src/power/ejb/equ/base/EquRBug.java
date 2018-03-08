package power.ejb.equ.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquRBug entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_R_BUG")
public class EquRBug implements java.io.Serializable {

	// Fields

	private Long id;
	private String bugCode;
	private String attributeCode;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquRBug() {
	}

	/** minimal constructor */
	public EquRBug(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquRBug(Long id, String bugCode, String attributeCode,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.bugCode = bugCode;
		this.attributeCode = attributeCode;
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

	@Column(name = "BUG_CODE", length = 30)
	public String getBugCode() {
		return this.bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
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