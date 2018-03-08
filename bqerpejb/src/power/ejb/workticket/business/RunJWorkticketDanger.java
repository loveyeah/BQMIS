package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJWorkticketDanger entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_DANGER")
public class RunJWorkticketDanger implements java.io.Serializable {

	// Fields

	private Long dangerContentId;
	private String workticketNo;
	private Long PDangerId;
	private String dangerName;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;
	private String isRunadd;

	// Constructors

	/** default constructor */
	public RunJWorkticketDanger() {
	}

	/** minimal constructor */
	public RunJWorkticketDanger(Long dangerContentId, String workticketNo) {
		this.dangerContentId = dangerContentId;
		this.workticketNo = workticketNo;
	}

	/** full constructor */
	public RunJWorkticketDanger(Long dangerContentId, String workticketNo,
			Long PDangerId, String dangerName, Long orderBy, String modifyBy,
			Date modifyDate, String enterpriseCode, String isUse,String isRunadd) {
		this.dangerContentId = dangerContentId;
		this.workticketNo = workticketNo;
		this.PDangerId = PDangerId;
		this.dangerName = dangerName;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.isRunadd=isRunadd;
	}

	// Property accessors
	@Id
	@Column(name = "DANGER_CONTENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDangerContentId() {
		return this.dangerContentId;
	}

	public void setDangerContentId(Long dangerContentId) {
		this.dangerContentId = dangerContentId;
	}

	@Column(name = "WORKTICKET_NO", nullable = false, length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "P_DANGER_ID", precision = 10, scale = 0)
	public Long getPDangerId() {
		return this.PDangerId;
	}

	public void setPDangerId(Long PDangerId) {
		this.PDangerId = PDangerId;
	}

	@Column(name = "DANGER_NAME", length = 500)
	public String getDangerName() {
		return this.dangerName;
	}

	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
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

	@Column(name = "IS_RUNADD", length = 1)
	public String getIsRunadd() {
		return isRunadd;
	}

	public void setIsRunadd(String isRunadd) {
		this.isRunadd = isRunadd;
	}

}