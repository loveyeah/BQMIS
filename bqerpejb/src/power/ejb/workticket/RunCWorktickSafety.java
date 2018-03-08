package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorktickSafety entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICK_SAFETY")
public class RunCWorktickSafety implements java.io.Serializable {

	// Fields

	private Long safetyId;
	private String workticketTypeCode;
	private String safetyCode;
	private String safetyDesc;
	private Long orderBy;
	private Long markcardTypeId;
	private String safetyType; //modify isRunAdd
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorktickSafety() {
	}

	/** minimal constructor */
	public RunCWorktickSafety(Long safetyId) {
		this.safetyId = safetyId;
	}

	/** full constructor */
	public RunCWorktickSafety(Long safetyId, String workticketTypeCode,
			String safetyCode, String safetyDesc, Long orderBy,
			Long markcardTypeId, String safetyType, String modifyBy,
			Date modifyDate, String enterpriseCode, String isUse) {
		this.safetyId = safetyId;
		this.workticketTypeCode = workticketTypeCode;
		this.safetyCode = safetyCode;
		this.safetyDesc = safetyDesc;
		this.orderBy = orderBy;
		this.markcardTypeId = markcardTypeId;
		this.safetyType = safetyType;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "SAFETY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSafetyId() {
		return this.safetyId;
	}

	public void setSafetyId(Long safetyId) {
		this.safetyId = safetyId;
	}

	@Column(name = "WORKTICKET_TYPE_CODE", length = 1)
	public String getWorkticketTypeCode() {
		return this.workticketTypeCode;
	}

	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

	@Column(name = "SAFETY_CODE", length = 3)
	public String getSafetyCode() {
		return this.safetyCode;
	}

	public void setSafetyCode(String safetyCode) {
		this.safetyCode = safetyCode;
	}

	@Column(name = "SAFETY_DESC", length = 200)
	public String getSafetyDesc() {
		return this.safetyDesc;
	}

	public void setSafetyDesc(String safetyDesc) {
		this.safetyDesc = safetyDesc;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "MARKCARD_TYPE_ID", precision = 10, scale = 0)
	public Long getMarkcardTypeId() {
		return this.markcardTypeId;
	}

	public void setMarkcardTypeId(Long markcardTypeId) {
		this.markcardTypeId = markcardTypeId;
	}



	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
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

	@Column(name = "SAFETY_TYPE", length = 1)
	public String getSafetyType() {
		return safetyType;
	}
  
	public void setSafetyType(String safetyType) {
		this.safetyType = safetyType;
	}

}