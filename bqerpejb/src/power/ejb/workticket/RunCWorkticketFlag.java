package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketFlag entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_FLAG")
public class RunCWorkticketFlag implements java.io.Serializable {

	// Fields

	private Long flagId;
	private String flagName;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;
	private Long orderBy;

	// Constructors

	/** default constructor */
	public RunCWorkticketFlag() {
	}

	/** minimal constructor */
	public RunCWorkticketFlag(Long flagId) {
		this.flagId = flagId;
	} 
	@Id
	@Column(name = "FLAG_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFlagId() {
		return this.flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
		public Long getOrderBy() {
			return this.orderBy;
		}

		public void setOrderBy(Long orderBy) {
			this.orderBy = orderBy;
		}

	@Column(name = "FLAG_NAME", length = 30)
	public String getFlagName() {
		return this.flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
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