package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketWorkcondition entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_WORKCONDITION")
public class RunCWorkticketWorkcondition implements java.io.Serializable {

	// Fields

	private Long conditionId;
	private String conditionName;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;
	private Long orderBy;

	// Constructors

	/** default constructor */
	public RunCWorkticketWorkcondition() {
	}

	/** minimal constructor */
	public RunCWorkticketWorkcondition(Long conditionId) {
		this.conditionId = conditionId;
	} 

	// Property accessors
	@Id
	@Column(name = "CONDITION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getConditionId() {
		return this.conditionId;
	}

	public void setConditionId(Long conditionId) {
		this.conditionId = conditionId;
	}

	@Column(name = "CONDITION_NAME", length = 30)
	public String getConditionName() {
		return this.conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
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