package power.ejb.equ.technology;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCTechnoAttribute entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_TECHNO_ATTRIBUTE")
public class EquCTechnoAttribute implements java.io.Serializable {

	// Fields

	private Long attrId;
	private String attrDesc;
	private String dataType;
	private Long unitCode;
	private Long attrtypeId;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCTechnoAttribute() {
	}

	/** minimal constructor */
	public EquCTechnoAttribute(Long attrId, String dataType) {
		this.attrId = attrId;
		this.dataType = dataType;
	}

	/** full constructor */
	public EquCTechnoAttribute(Long attrId, String attrDesc, String dataType,
			Long unitCode, Long attrtypeId, String memo, String enterpriseCode,
			String isUse) {
		this.attrId = attrId;
		this.attrDesc = attrDesc;
		this.dataType = dataType;
		this.unitCode = unitCode;
		this.attrtypeId = attrtypeId;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ATTR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAttrId() {
		return this.attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	@Column(name = "ATTR_DESC", length = 60)
	public String getAttrDesc() {
		return this.attrDesc;
	}

	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}

	@Column(name = "DATA_TYPE", nullable = false, length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "UNIT_CODE", precision = 10, scale = 0)
	public Long getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(Long unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "ATTRTYPE_ID", precision = 10, scale = 0)
	public Long getAttrtypeId() {
		return this.attrtypeId;
	}

	public void setAttrtypeId(Long attrtypeId) {
		this.attrtypeId = attrtypeId;
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