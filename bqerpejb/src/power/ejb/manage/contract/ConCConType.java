package power.ejb.manage.contract;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConCConType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_CON_TYPE")
public class ConCConType implements java.io.Serializable {

	// Fields

	private Long contypeId;
	private Long PContypeId;
	private String conTypeDesc;
	private String markCode;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConCConType() {
	}

	/** minimal constructor */
	public ConCConType(Long contypeId, Long PContypeId, String conTypeDesc,
			String markCode, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.contypeId = contypeId;
		this.PContypeId = PContypeId;
		this.conTypeDesc = conTypeDesc;
		this.markCode = markCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConCConType(Long contypeId, Long PContypeId, String conTypeDesc,
			String markCode, String memo, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.contypeId = contypeId;
		this.PContypeId = PContypeId;
		this.conTypeDesc = conTypeDesc;
		this.markCode = markCode;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CONTYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContypeId() {
		return this.contypeId;
	}

	public void setContypeId(Long contypeId) {
		this.contypeId = contypeId;
	}

	@Column(name = "P_CONTYPE_ID", nullable = false, precision = 10, scale = 0)
	public Long getPContypeId() {
		return this.PContypeId;
	}

	public void setPContypeId(Long PContypeId) {
		this.PContypeId = PContypeId;
	}

	@Column(name = "CON_TYPE_DESC", nullable = false, length = 100)
	public String getConTypeDesc() {
		return this.conTypeDesc;
	}

	public void setConTypeDesc(String conTypeDesc) {
		this.conTypeDesc = conTypeDesc;
	}

	@Column(name = "MARK_CODE", nullable = false, length = 20)
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