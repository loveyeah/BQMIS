package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 标识牌实体 
 */
@Entity
@Table(name = "RUN_C_MARKCARD_TYPE")
public class RunCMarkcardType implements java.io.Serializable { 
	private Long markcardTypeId;
	private String markcardTypeName;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse; 
	public RunCMarkcardType() {
	} 
	public RunCMarkcardType(Long markcardTypeId) {
		this.markcardTypeId = markcardTypeId;
	} 
	public RunCMarkcardType(Long markcardTypeId, String markcardTypeName,
			String modifyBy, Date modifyDate, String enterpriseCode,
			String isUse) {
		this.markcardTypeId = markcardTypeId;
		this.markcardTypeName = markcardTypeName;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MARKCARD_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMarkcardTypeId() {
		return this.markcardTypeId;
	}

	public void setMarkcardTypeId(Long markcardTypeId) {
		this.markcardTypeId = markcardTypeId;
	}

	@Column(name = "MARKCARD_TYPE_NAME", length = 30)
	public String getMarkcardTypeName() {
		return this.markcardTypeName;
	}

	public void setMarkcardTypeName(String markcardTypeName) {
		this.markcardTypeName = markcardTypeName;
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