package power.ejb.manage.budget;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CbmJMasterItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_MASTER_ITEM")
public class CbmJMasterItem implements java.io.Serializable {

	// Fields

	private Long happenId;
	private Long budgetItemId;
	private Long centerId;
	private String budgetTime;
	private Long itemId;
	private Long happenSerial;
	private Double happenValue;
	private String happenExplain;
	private String fillBy;
	private Date fillTime;
	private Long workFlowNo;
	private String happenStatus;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJMasterItem() {
	}

	/** minimal constructor */
	public CbmJMasterItem(Long happenId, Long budgetItemId, Long happenSerial) {
		this.happenId = happenId;
		this.budgetItemId = budgetItemId;
		this.happenSerial = happenSerial;
	}

	/** full constructor */
	public CbmJMasterItem(Long happenId, Long budgetItemId, Long centerId,
			String budgetTime, Long itemId, Long happenSerial,
			Double happenValue, String happenExplain, String fillBy,
			Date fillTime, Long workFlowNo, String happenStatus, String isUse,
			String enterpriseCode) {
		this.happenId = happenId;
		this.budgetItemId = budgetItemId;
		this.centerId = centerId;
		this.budgetTime = budgetTime;
		this.itemId = itemId;
		this.happenSerial = happenSerial;
		this.happenValue = happenValue;
		this.happenExplain = happenExplain;
		this.fillBy = fillBy;
		this.fillTime = fillTime;
		this.workFlowNo = workFlowNo;
		this.happenStatus = happenStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "HAPPEN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getHappenId() {
		return this.happenId;
	}

	public void setHappenId(Long happenId) {
		this.happenId = happenId;
	}

	@Column(name = "BUDGET_ITEM_ID", nullable = false, precision = 10, scale = 0)
	public Long getBudgetItemId() {
		return this.budgetItemId;
	}

	public void setBudgetItemId(Long budgetItemId) {
		this.budgetItemId = budgetItemId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "BUDGET_TIME", length = 10)
	public String getBudgetTime() {
		return this.budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "HAPPEN_SERIAL", nullable = false, precision = 4, scale = 0)
	public Long getHappenSerial() {
		return this.happenSerial;
	}

	public void setHappenSerial(Long happenSerial) {
		this.happenSerial = happenSerial;
	}

	@Column(name = "HAPPEN_VALUE", precision = 18, scale = 6)
	public Double getHappenValue() {
		return this.happenValue;
	}

	public void setHappenValue(Double happenValue) {
		this.happenValue = happenValue;
	}

	@Column(name = "HAPPEN_EXPLAIN", length = 500)
	public String getHappenExplain() {
		return this.happenExplain;
	}

	public void setHappenExplain(String happenExplain) {
		this.happenExplain = happenExplain;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_TIME", length = 7)
	public Date getFillTime() {
		return this.fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "HAPPEN_STATUS", length = 1)
	public String getHappenStatus() {
		return this.happenStatus;
	}

	public void setHappenStatus(String happenStatus) {
		this.happenStatus = happenStatus;
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