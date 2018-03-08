package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjCType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PRJ_C_TYPE")
public class PrjCType implements java.io.Serializable {

	// Fields

	private Long prjTypeId;
	private Long prjPTypeId;
	private String prjTypeName;
	private String markCode;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public PrjCType() {
	}

	/** minimal constructor */
	public PrjCType(Long prjTypeId, String prjTypeName, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.prjTypeId = prjTypeId;
		this.prjTypeName = prjTypeName;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public PrjCType(Long prjTypeId, Long prjPTypeId, String prjTypeName,
			String markCode, String memo, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.prjTypeId = prjTypeId;
		this.prjPTypeId = prjPTypeId;
		this.prjTypeName = prjTypeName;
		this.markCode = markCode;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "PRJ_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrjTypeId() {
		return this.prjTypeId;
	}

	public void setPrjTypeId(Long prjTypeId) {
		this.prjTypeId = prjTypeId;
	}

	@Column(name = "PRJ_P_TYPE_ID", precision = 10, scale = 0)
	public Long getPrjPTypeId() {
		return this.prjPTypeId;
	}

	public void setPrjPTypeId(Long prjPTypeId) {
		this.prjPTypeId = prjPTypeId;
	}

	@Column(name = "PRJ_TYPE_NAME", nullable = false, length = 100)
	public String getPrjTypeName() {
		return this.prjTypeName;
	}

	public void setPrjTypeName(String prjTypeName) {
		this.prjTypeName = prjTypeName;
	}

	@Column(name = "MARK_CODE", length = 20)
	public String getMarkCode() {
		return this.markCode;
	}

	public void setMarkCode(String markCode) {
		this.markCode = markCode;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
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