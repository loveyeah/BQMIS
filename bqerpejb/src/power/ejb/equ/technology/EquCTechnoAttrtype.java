package power.ejb.equ.technology;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCTechnoAttrtype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_TECHNO_ATTRTYPE")
public class EquCTechnoAttrtype implements java.io.Serializable {

	// Fields

	private Long attrtypeId;
	private String attrtypeDesc;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCTechnoAttrtype() {
	}

	/** minimal constructor */
	public EquCTechnoAttrtype(Long attrtypeId) {
		this.attrtypeId = attrtypeId;
	}

	/** full constructor */
	public EquCTechnoAttrtype(Long attrtypeId, String attrtypeDesc,
			String memo, String enterpriseCode, String isUse) {
		this.attrtypeId = attrtypeId;
		this.attrtypeDesc = attrtypeDesc;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ATTRTYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAttrtypeId() {
		return this.attrtypeId;
	}

	public void setAttrtypeId(Long attrtypeId) {
		this.attrtypeId = attrtypeId;
	}

	@Column(name = "ATTRTYPE_DESC", length = 60)
	public String getAttrtypeDesc() {
		return this.attrtypeDesc;
	}

	public void setAttrtypeDesc(String attrtypeDesc) {
		this.attrtypeDesc = attrtypeDesc;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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