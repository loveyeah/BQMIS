package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCAlternateMaterial entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_ALTERNATE_MATERIAL")
public class InvCAlternateMaterial implements java.io.Serializable {

	// Fields

	private Long alternateMaterialId;
	private Long materialId;
	private Long alterMaterialId;
	private Double qty;
	private Long priority;
	private Date effectiveDate;
	private Date discontinueDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCAlternateMaterial() {
	}

	/** minimal constructor */
	public InvCAlternateMaterial(Long alternateMaterialId, Double qty,
			Long priority, Date effectiveDate, Date discontinueDate,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.alternateMaterialId = alternateMaterialId;
		this.qty = qty;
		this.priority = priority;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvCAlternateMaterial(Long alternateMaterialId, Long materialId,
			Long alterMaterialId, Double qty, Long priority,
			Date effectiveDate, Date discontinueDate, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.alternateMaterialId = alternateMaterialId;
		this.materialId = materialId;
		this.alterMaterialId = alterMaterialId;
		this.qty = qty;
		this.priority = priority;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ALTERNATE_MATERIAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAlternateMaterialId() {
		return this.alternateMaterialId;
	}

	public void setAlternateMaterialId(Long alternateMaterialId) {
		this.alternateMaterialId = alternateMaterialId;
	}

	@Column(name = "MATERIAL_ID", precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "ALTER_MATERIAL_ID", precision = 10, scale = 0)
	public Long getAlterMaterialId() {
		return this.alterMaterialId;
	}

	public void setAlterMaterialId(Long alterMaterialId) {
		this.alterMaterialId = alterMaterialId;
	}

	@Column(name = "QTY", nullable = false, precision = 15, scale = 4)
	public Double getQty() {
		return this.qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	@Column(name = "PRIORITY", nullable = false, precision = 10, scale = 0)
	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_DATE", nullable = false, length = 7)
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DISCONTINUE_DATE", nullable = false, length = 7)
	public Date getDiscontinueDate() {
		return this.discontinueDate;
	}

	public void setDiscontinueDate(Date discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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