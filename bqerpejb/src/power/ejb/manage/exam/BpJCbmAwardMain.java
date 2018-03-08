package power.ejb.manage.exam;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJCbmAwardMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_J_CBM_AWARD_MAIN")
public class BpJCbmAwardMain implements java.io.Serializable {

	// Fields

	private Long declareId;
	private String yearMonth;
	private Long workflowNo;
	private String status;
	private String isUse;
	private String enterpriseCode;
	//add by drdu 091130
	private String lastModifyBy;
	private Date lastModifyDate;
	
	// Constructors

	/** default constructor */
	public BpJCbmAwardMain() {
	}

	/** minimal constructor */
	public BpJCbmAwardMain(Long declareId) {
		this.declareId = declareId;
	}
 

	// Property accessors
	@Id
	@Column(name = "DECLARE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDeclareId() {
		return this.declareId;
	}

	public void setDeclareId(Object object) {
		this.declareId = (Long) object;
	}

	 

	@Column(name = "WORKFLOW_NO", precision = 22, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Object object) {
		this.workflowNo = (Long) object;}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	@Column(name = "YEAR_MONTH", length = 10)
	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "LAST_MODIFY_BY", length = 30)
	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFY_DATE", length = 7)
	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

}