package power.ejb.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurCPlaner entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_C_PLANER")
public class PurCPlaner implements java.io.Serializable {

	// Fields

	private Long planerId;
	private String planer;
	private String planerName;
	private String materialOrClassNo;
	private String isMaterialClass;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public PurCPlaner() {
	}

	/** full constructor */
	public PurCPlaner(Long planerId, String planer, String planerName,
			String materialOrClassNo, String isMaterialClass,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.planerId = planerId;
		this.planer = planer;
		this.planerName = planerName;
		this.materialOrClassNo = materialOrClassNo;
		this.isMaterialClass = isMaterialClass;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "PLANER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPlanerId() {
		return this.planerId;
	}

	public void setPlanerId(Long planerId) {
		this.planerId = planerId;
	}

	@Column(name = "PLANER", nullable = false, length = 16)
	public String getPlaner() {
		return this.planer;
	}

	public void setPlaner(String planer) {
		this.planer = planer;
	}

	@Column(name = "PLANER_NAME", nullable = false, length = 30)
	public String getPlanerName() {
		return this.planerName;
	}

	public void setPlanerName(String planerName) {
		this.planerName = planerName;
	}

	@Column(name = "MATERIAL_OR_CLASS_NO", nullable = false, length = 30)
	public String getMaterialOrClassNo() {
		return this.materialOrClassNo;
	}

	public void setMaterialOrClassNo(String materialOrClassNo) {
		this.materialOrClassNo = materialOrClassNo;
	}

	@Column(name = "IS_MATERIAL_CLASS", nullable = false, length = 1)
	public String getIsMaterialClass() {
		return this.isMaterialClass;
	}

	public void setIsMaterialClass(String isMaterialClass) {
		this.isMaterialClass = isMaterialClass;
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