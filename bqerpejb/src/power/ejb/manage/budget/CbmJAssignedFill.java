package power.ejb.manage.budget;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CbmJAssignedFill entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_ASSIGNED_FILL")
public class CbmJAssignedFill implements java.io.Serializable {

	// Fields

	private Long assignId;
	private String assignName;
	private Long itemId;
	private Double estimateMoney;
	private String assignFunction;
	private String memo;
	private String applyBy;
	private String applyDept;
	private Date applyDate;
	private Long workFlowNo;
	private String workFlowStatus;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJAssignedFill() {
	}

	/** minimal constructor */
	public CbmJAssignedFill(Long assignId) {
		this.assignId = assignId;
	}

	/** full constructor */
	public CbmJAssignedFill(Long assignId, String assignName, Long itemId,
			Double estimateMoney, String assignFunction, String memo,
			String applyBy, String applyDept, Date applyDate, Long workFlowNo,
			String workFlowStatus, String isUse, String enterpriseCode) {
		this.assignId = assignId;
		this.assignName = assignName;
		this.itemId = itemId;
		this.estimateMoney = estimateMoney;
		this.assignFunction = assignFunction;
		this.memo = memo;
		this.applyBy = applyBy;
		this.applyDept = applyDept;
		this.applyDate = applyDate;
		this.workFlowNo = workFlowNo;
		this.workFlowStatus = workFlowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ASSIGN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAssignId() {
		return this.assignId;
	}

	public void setAssignId(Long assignId) {
		this.assignId = assignId;
	}

	@Column(name = "ASSIGN_NAME", length = 50)
	public String getAssignName() {
		return this.assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "ESTIMATE_MONEY", precision = 15, scale = 4)
	public Double getEstimateMoney() {
		return this.estimateMoney;
	}

	public void setEstimateMoney(Double estimateMoney) {
		this.estimateMoney = estimateMoney;
	}

	@Column(name = "ASSIGN_FUNCTION", length = 500)
	public String getAssignFunction() {
		return this.assignFunction;
	}

	public void setAssignFunction(String assignFunction) {
		this.assignFunction = assignFunction;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "APPLY_BY", length = 30)
	public String getApplyBy() {
		return this.applyBy;
	}

	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}

	@Column(name = "APPLY_DEPT", length = 20)
	public String getApplyDept() {
		return this.applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_DATE", length = 7)
	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WORK_FLOW_STATUS", length = 1)
	public String getWorkFlowStatus() {
		return this.workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
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

}