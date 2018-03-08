package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJBaseAnnex entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_BASE_ANNEX")
public class EquJBaseAnnex implements java.io.Serializable {

	// Fields

	private Long annexId;
	private Long equBaseId;
	private String fileName;
	private String annex;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquJBaseAnnex() {
	}

	/** minimal constructor */
	public EquJBaseAnnex(Long annexId) {
		this.annexId = annexId;
	}

	/** full constructor */
	public EquJBaseAnnex(Long annexId, Long equBaseId, String fileName,
			String annex, String lastModifiedBy, Date lastModifiedDate,
			String isUse, String enterpriseCode) {
		this.annexId = annexId;
		this.equBaseId = equBaseId;
		this.fileName = fileName;
		this.annex = annex;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ANNEX_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAnnexId() {
		return this.annexId;
	}

	public void setAnnexId(Long annexId) {
		this.annexId = annexId;
	}

	@Column(name = "EQU_BASE_ID", precision = 10, scale = 0)
	public Long getEquBaseId() {
		return this.equBaseId;
	}

	public void setEquBaseId(Long equBaseId) {
		this.equBaseId = equBaseId;
	}

	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "ANNEX", length = 400)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 20)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}