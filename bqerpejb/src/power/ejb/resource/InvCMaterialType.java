package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCMaterialType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_MATERIAL_TYPE")
public class InvCMaterialType implements java.io.Serializable {

	// Fields

	private Long materialTypeId;
	private String typeNo;
	private String typeName;
	private String typeDesc;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCMaterialType() {
	}

	/** minimal constructor */
	public InvCMaterialType(Long materialTypeId, String typeNo,
			String typeName, Date lastModifiedDate, String lastModifiedBy,
			String enterpriseCode, String isUse) {
		this.materialTypeId = materialTypeId;
		this.typeNo = typeNo;
		this.typeName = typeName;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedBy = lastModifiedBy;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvCMaterialType(Long materialTypeId, String typeNo,
			String typeName, String typeDesc, Date lastModifiedDate,
			String lastModifiedBy, String enterpriseCode, String isUse) {
		this.materialTypeId = materialTypeId;
		this.typeNo = typeNo;
		this.typeName = typeName;
		this.typeDesc = typeDesc;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedBy = lastModifiedBy;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MATERIAL_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMaterialTypeId() {
		return this.materialTypeId;
	}

	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	@Column(name = "TYPE_NO", nullable = false, length = 10)
	public String getTypeNo() {
		return this.typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	@Column(name = "TYPE_NAME", nullable = false, length = 100)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "TYPE_DESC", length = 200)
	public String getTypeDesc() {
		return this.typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}