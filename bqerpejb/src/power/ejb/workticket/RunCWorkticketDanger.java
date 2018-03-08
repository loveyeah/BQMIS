package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketDanger entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_DANGER")
public class RunCWorkticketDanger implements java.io.Serializable {

	// Fields

	private Long dangerId;
	private String dangerName;
	private Long dangerTypeId;
	private Long PDangerId;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketDanger() {
	}

	/** minimal constructor */
	public RunCWorkticketDanger(Long dangerId) {
		this.dangerId = dangerId;
	}

	/** full constructor */
	public RunCWorkticketDanger(Long dangerId, String dangerName,
			Long dangerTypeId, Long PDangerId, Long orderBy, String modifyBy,
			Date modifyDate, String enterpriseCode, String isUse) {
		this.dangerId = dangerId;
		this.dangerName = dangerName;
		this.dangerTypeId = dangerTypeId;
		this.PDangerId = PDangerId;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "DANGER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDangerId() {
		return this.dangerId;
	}

	public void setDangerId(Long dangerId) {
		this.dangerId = dangerId;
	}

	@Column(name = "DANGER_NAME", length = 500)
	public String getDangerName() {
		return this.dangerName;
	}

	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
	}

	@Column(name = "DANGER_TYPE_ID", precision = 10, scale = 0)
	public Long getDangerTypeId() {
		return this.dangerTypeId;
	}

	public void setDangerTypeId(Long dangerTypeId) {
		this.dangerTypeId = dangerTypeId;
	}

	@Column(name = "P_DANGER_ID", precision = 10, scale = 0)
	public Long getPDangerId() {
		return this.PDangerId;
	}

	public void setPDangerId(Long PDangerId) {
		this.PDangerId = PDangerId;
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