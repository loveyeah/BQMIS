package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketSafetyKey entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_SAFETY_KEY")
public class RunCWorkticketSafetyKey implements java.io.Serializable {

	// Fields

	private Long safetyKeyId;
	private String safetyKeyName;
	private String workticketTypeCode;
	private String keyType;
	private Long reverseKeyId;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketSafetyKey() {
	}

	/** minimal constructor */
	public RunCWorkticketSafetyKey(Long safetyKeyId) {
		this.safetyKeyId = safetyKeyId;
	}

	/** full constructor */
	public RunCWorkticketSafetyKey(Long safetyKeyId, String safetyKeyName,
			String workticketTypeCode, String keyType, Long reverseKeyId,
			Long orderBy, String modifyBy, Date modifyDate,
			String enterpriseCode, String isUse) {
		this.safetyKeyId = safetyKeyId;
		this.safetyKeyName = safetyKeyName;
		this.workticketTypeCode = workticketTypeCode;
		this.keyType = keyType;
		this.reverseKeyId = reverseKeyId;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "SAFETY_KEY_ID", nullable = false, precision = 10, scale = 0)
	public Long getSafetyKeyId() {
		return this.safetyKeyId;
	}

	public void setSafetyKeyId(Long safetyKeyId) {
		this.safetyKeyId = safetyKeyId;
	}

	@Column(name = "SAFETY_KEY_NAME", length = 100)
	public String getSafetyKeyName() {
		return this.safetyKeyName;
	}

	public void setSafetyKeyName(String safetyKeyName) {
		this.safetyKeyName = safetyKeyName;
	}

	@Column(name = "WORKTICKET_TYPE_CODE", length = 1)
	public String getWorkticketTypeCode() {
		return this.workticketTypeCode;
	}

	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

	@Column(name = "KEY_TYPE", length = 1)
	public String getKeyType() {
		return this.keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	@Column(name = "REVERSE_KEY_ID", precision = 10, scale = 0)
	public Long getReverseKeyId() {
		return this.reverseKeyId;
	}

	public void setReverseKeyId(Long reverseKeyId) {
		this.reverseKeyId = reverseKeyId;
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

}