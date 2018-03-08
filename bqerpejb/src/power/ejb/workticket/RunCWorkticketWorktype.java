package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketWorktype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_WORKTYPE")
public class RunCWorkticketWorktype implements java.io.Serializable {

	// Fields

	private Long worktypeId;
	private String worktypeName;
	private String workticketTypeCode;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCWorkticketWorktype() {
	}

	/** minimal constructor */
	public RunCWorkticketWorktype(Long worktypeId) {
		this.worktypeId = worktypeId;
	}

	/** full constructor */
	public RunCWorkticketWorktype(Long worktypeId, String worktypeName,
			String workticketTypeCode, Long orderBy, String modifyBy,
			Date modifyDate, String enterpriseCode, String isUse) {
		this.worktypeId = worktypeId;
		this.worktypeName = worktypeName;
		this.workticketTypeCode = workticketTypeCode;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "worktype_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getworktypeId() {
		return this.worktypeId;
	}

	public void setworktypeId(Long worktypeId) {
		this.worktypeId = worktypeId;
	}

	@Column(name = "worktype_name", length = 100)
	public String getworktypeName() {
		return this.worktypeName;
	}

	public void setworktypeName(String worktypeName) {
		this.worktypeName = worktypeName;
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