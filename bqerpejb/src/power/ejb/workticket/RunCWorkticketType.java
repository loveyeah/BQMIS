package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_TYPE", schema = "POWER")
public class RunCWorkticketType implements java.io.Serializable {

	// Fields

	private Long workticketTypeId;
	private String workticketTypeCode;
	private String workticketTypeName;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketType() {
	}

	/** minimal constructor */
	public RunCWorkticketType(Long workticketTypeId) {
		this.workticketTypeId = workticketTypeId;
	}

	/** full constructor */
	public RunCWorkticketType(Long workticketTypeId, String workticketTypeCode,
			String workticketTypeName, String modifyBy, Date modifyDate,
			String enterpriseCode, String isUse) {
		this.workticketTypeId = workticketTypeId;
		this.workticketTypeCode = workticketTypeCode;
		this.workticketTypeName = workticketTypeName;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "WORKTICKET_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWorkticketTypeId() {
		return this.workticketTypeId;
	}

	public void setWorkticketTypeId(Long workticketTypeId) {
		this.workticketTypeId = workticketTypeId;
	}

	@Column(name = "WORKTICKET_TYPE_CODE", length = 1)
	public String getWorkticketTypeCode() {
		return this.workticketTypeCode;
	}

	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

	@Column(name = "WORKTICKET_TYPE_NAME", length = 20)
	public String getWorkticketTypeName() {
		return this.workticketTypeName;
	}

	public void setWorkticketTypeName(String workticketTypeName) {
		this.workticketTypeName = workticketTypeName;
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