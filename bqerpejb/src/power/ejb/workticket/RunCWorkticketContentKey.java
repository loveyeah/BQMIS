package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketContentKey entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_CONTENT_KEY")
public class RunCWorkticketContentKey implements java.io.Serializable {

	// Fields

	private Long contentKeyId;
	private String contentKeyName;
	private String workticketTypeCode;
	private String keyType;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketContentKey() {
	}

	/** minimal constructor */
	public RunCWorkticketContentKey(Long contentKeyId) {
		this.contentKeyId = contentKeyId;
	}

	/** full constructor */
	public RunCWorkticketContentKey(Long contentKeyId, String contentKeyName,
			String workticketTypeCode, String keyType, Long orderBy,
			String modifyBy, Date modifyDate, String enterpriseCode,
			String isUse) {
		this.contentKeyId = contentKeyId;
		this.contentKeyName = contentKeyName;
		this.workticketTypeCode = workticketTypeCode;
		this.keyType = keyType;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CONTENT_KEY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContentKeyId() {
		return this.contentKeyId;
	}

	public void setContentKeyId(Long contentKeyId) {
		this.contentKeyId = contentKeyId;
	}

	@Column(name = "CONTENT_KEY_NAME", length = 100)
	public String getContentKeyName() {
		return this.contentKeyName;
	}

	public void setContentKeyName(String contentKeyName) {
		this.contentKeyName = contentKeyName;
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