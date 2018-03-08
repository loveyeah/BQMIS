package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCOuttype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_OUTTYPE")
public class HrCOuttype implements java.io.Serializable {

	// Fields

	private Long outTypeId;
	private String outTypeType;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String insertby;
	private Date insertdate;
	private Long orderBy;

	// Constructors

	/** default constructor */
	public HrCOuttype() {
	}

	/** minimal constructor */
	public HrCOuttype(Long outTypeId) {
		this.outTypeId = outTypeId;
	}

	/** full constructor */
	public HrCOuttype(Long outTypeId, String outTypeType,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, String insertby, Date insertdate,
			Long orderBy) {
		this.outTypeId = outTypeId;
		this.outTypeType = outTypeType;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.orderBy = orderBy;
	}

	// Property accessors
	@Id
	@Column(name = "OUT_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOutTypeId() {
		return this.outTypeId;
	}

	public void setOutTypeId(Long outTypeId) {
		this.outTypeId = outTypeId;
	}

	@Column(name = "OUT_TYPE_TYPE", length = 40)
	public String getOutTypeType() {
		return this.outTypeType;
	}

	public void setOutTypeType(String outTypeType) {
		this.outTypeType = outTypeType;
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

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

}