package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJCooperateunit entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_COOPERATEUNIT")
public class HrJCooperateunit implements java.io.Serializable {

	// Fields

	private Long cooperateUnitId;
	private String cooperateUnit;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long orderBy;

	// Constructors

	/** default constructor */
	public HrJCooperateunit() {
	}

	/** minimal constructor */
	public HrJCooperateunit(Long cooperateUnitId) {
		this.cooperateUnitId = cooperateUnitId;
	}

	/** full constructor */
	public HrJCooperateunit(Long cooperateUnitId, String cooperateUnit,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, Long orderBy) {
		this.cooperateUnitId = cooperateUnitId;
		this.cooperateUnit = cooperateUnit;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.orderBy = orderBy;
	}

	// Property accessors
	@Id
	@Column(name = "COOPERATE_UNIT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCooperateUnitId() {
		return this.cooperateUnitId;
	}

	public void setCooperateUnitId(Long cooperateUnitId) {
		this.cooperateUnitId = cooperateUnitId;
	}

	@Column(name = "COOPERATE_UNIT", length = 100)
	public String getCooperateUnit() {
		return this.cooperateUnit;
	}

	public void setCooperateUnit(String cooperateUnit) {
		this.cooperateUnit = cooperateUnit;
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

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

}