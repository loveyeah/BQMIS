package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketMarkcard entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_C_WORKTICKET_MARKCARD", schema = "POWER")
public class RunCWorkticketMarkcard implements java.io.Serializable {

	// Fields

	private Long markcardId;
	private Long markcardTypeId;
	private String markcardCode;
	private String isOtherUse;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketMarkcard() {
	}

	/** minimal constructor */
	public RunCWorkticketMarkcard(Long markcardId) {
		this.markcardId = markcardId;
	}

	/** full constructor */
	public RunCWorkticketMarkcard(Long markcardId, Long markcardTypeId,
			String markcardCode, String isOtherUse, Long orderBy,
			String modifyBy, Date modifyDate, String enterpriseCode,
			String isUse) {
		this.markcardId = markcardId;
		this.markcardTypeId = markcardTypeId;
		this.markcardCode = markcardCode;
		this.isOtherUse = isOtherUse;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MARKCARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMarkcardId() {
		return this.markcardId;
	}

	public void setMarkcardId(Long markcardId) {
		this.markcardId = markcardId;
	}

	@Column(name = "MARKCARD_TYPE_ID", precision = 10, scale = 0)
	public Long getMarkcardTypeId() {
		return this.markcardTypeId;
	}

	public void setMarkcardTypeId(Long markcardTypeId) {
		this.markcardTypeId = markcardTypeId;
	}

	@Column(name = "MARKCARD_CODE", length = 20)
	public String getMarkcardCode() {
		return this.markcardCode;
	}

	public void setMarkcardCode(String markcardCode) {
		this.markcardCode = markcardCode;
	}

	@Column(name = "IS_OTHER_USE", length = 1)
	public String getIsOtherUse() {
		return this.isOtherUse;
	}

	public void setIsOtherUse(String isOtherUse) {
		this.isOtherUse = isOtherUse;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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