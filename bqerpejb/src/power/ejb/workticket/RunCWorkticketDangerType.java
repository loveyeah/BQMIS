package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketDangerType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_DANGER_TYPE")
public class RunCWorkticketDangerType implements java.io.Serializable {

	// Fields

	private Long dangerTypeId;
	private String dangerTypeName;
	private String workticketTypeCode;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketDangerType() {
	}

	/** minimal constructor */
	public RunCWorkticketDangerType(Long dangerTypeId) {
		this.dangerTypeId = dangerTypeId;
	}

	/** full constructor */
	public RunCWorkticketDangerType(Long dangerTypeId, String dangerTypeName,
			String workticketTypeCode, Long orderBy, String modifyBy,
			Date modifyDate, String enterpriseCode, String isUse) {
		this.dangerTypeId = dangerTypeId;
		this.dangerTypeName = dangerTypeName;
		this.workticketTypeCode = workticketTypeCode;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "DANGER_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDangerTypeId() {
		return this.dangerTypeId;
	}

	public void setDangerTypeId(Long dangerTypeId) {
		this.dangerTypeId = dangerTypeId;
	}

	@Column(name = "DANGER_TYPE_NAME", length = 100)
	public String getDangerTypeName() {
		return this.dangerTypeName;
	}

	public void setDangerTypeName(String dangerTypeName) {
		this.dangerTypeName = dangerTypeName;
	}

	@Column(name = "WORKTICKET_TYPE_CODE", length = 1)
	public String getWorkticketTypeCode() {
		return this.workticketTypeCode;
	}

	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
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