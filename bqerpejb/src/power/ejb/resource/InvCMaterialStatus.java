package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCMaterialStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_MATERIAL_STATUS")
public class InvCMaterialStatus implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long materialStatusId;
	private String statusNo;
	private String statusName;
	private String statusDesc;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCMaterialStatus() {
	}

	/** minimal constructor */
	public InvCMaterialStatus(Long materialStatusId) {
		this.materialStatusId = materialStatusId;
	}

	/** full constructor */
	public InvCMaterialStatus(Long materialStatusId, String statusNo,
			String statusName, String statusDesc, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.materialStatusId = materialStatusId;
		this.statusNo = statusNo;
		this.statusName = statusName;
		this.statusDesc = statusDesc;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MATERIAL_STATUS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMaterialStatusId() {
		return this.materialStatusId;
	}

	public void setMaterialStatusId(Long materialStatusId) {
		this.materialStatusId = materialStatusId;
	}

	@Column(name = "STATUS_NO", length = 10)
	public String getStatusNo() {
		return this.statusNo;
	}

	public void setStatusNo(String statusNo) {
		this.statusNo = statusNo;
	}

	@Column(name = "STATUS_NAME", length = 100)
	public String getStatusName() {
		return this.statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Column(name = "STATUS_DESC", length = 200)
	public String getStatusDesc() {
		return this.statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
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