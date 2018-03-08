package power.ejb.manage.budget;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CbmJBudgetMake entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_BUDGET_MAKE")
public class CbmJBudgetMake implements java.io.Serializable {

	// Fields

	private Long budgetMakeId;
	private Long centerId;
	private Long topicId;
	private String budgetTime;
	//private Long budgetGatherId;
	private String makeBy;
	private Date makeDate;
	private Long workFlowNo;
	private String makeStatus;
	private String enterpriseCode;
	private String sendBy;
	private Date sendDate;
	private String isUse;

	// Constructors

	/** default constructor */
	public CbmJBudgetMake() {
	}

	/** minimal constructor */
	public CbmJBudgetMake(Long budgetMakeId, Long centerTopicId,
			String budgetTime) {
		this.budgetMakeId = budgetMakeId;
		this.budgetTime = budgetTime;
	}

	/** full constructor */
	public CbmJBudgetMake(Long budgetMakeId, Long centerId, Long topicId,
			String budgetTime, String makeBy,
			Date makeDate, Long workFlowNo, String makeStatus,
			String enterpriseCode, String sendBy, Date sendDate,String isUse) {
		this.budgetMakeId = budgetMakeId;
		this.centerId = centerId;
		this.topicId = topicId;
		this.budgetTime = budgetTime;
		
		this.makeBy = makeBy;
		this.makeDate = makeDate;
		this.workFlowNo = workFlowNo;
		this.makeStatus = makeStatus;
		this.enterpriseCode = enterpriseCode;
		this.sendBy = sendBy;
		this.sendDate = sendDate;
		this.isUse=isUse;
	}

	// Property accessors
	@Id
	@Column(name = "BUDGET_MAKE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBudgetMakeId() {
		return this.budgetMakeId;
	}

	public void setBudgetMakeId(Long budgetMakeId) {
		this.budgetMakeId = budgetMakeId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "TOPIC_ID", precision = 10, scale = 0)
	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	@Column(name = "BUDGET_TIME", nullable = false, length = 10)
	public String getBudgetTime() {
		return this.budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
	}

	
	@Column(name = "MAKE_BY", length = 16)
	public String getMakeBy() {
		return this.makeBy;
	}

	public void setMakeBy(String makeBy) {
		this.makeBy = makeBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MAKE_DATE", length = 7)
	public Date getMakeDate() {
		return this.makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "MAKE_STATUS", length = 1)
	public String getMakeStatus() {
		return this.makeStatus;
	}

	public void setMakeStatus(String makeStatus) {
		this.makeStatus = makeStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "SEND_BY", length = 30)
	public String getSendBy() {
		return sendBy;
	}

	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SEND_DATE", length = 7)
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}