package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLaborSendcard entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LABOR_SENDCARD")
public class HrJLaborSendcard implements java.io.Serializable {

	// Fields

	private Long sendcardId;
	private String sendYear;
	private String sendKind;
	private String costItem;
	private String entryBy;
	private Date entryDate;
	private String sendState;
	private Long workFlowNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJLaborSendcard() {
	}

	/** minimal constructor */
	public HrJLaborSendcard(Long sendcardId) {
		this.sendcardId = sendcardId;
	}

	/** full constructor */
	public HrJLaborSendcard(Long sendcardId, String sendYear, String sendKind,
			String costItem, String entryBy, Date entryDate, String sendState,
			Long workFlowNo, String isUse, String enterpriseCode) {
		this.sendcardId = sendcardId;
		this.sendYear = sendYear;
		this.sendKind = sendKind;
		this.costItem = costItem;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.sendState = sendState;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SENDCARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSendcardId() {
		return this.sendcardId;
	}

	public void setSendcardId(Long sendcardId) {
		this.sendcardId = sendcardId;
	}

	@Column(name = "SEND_YEAR", length = 4)
	public String getSendYear() {
		return this.sendYear;
	}

	public void setSendYear(String sendYear) {
		this.sendYear = sendYear;
	}

	@Column(name = "SEND_KIND", length = 1)
	public String getSendKind() {
		return this.sendKind;
	}

	public void setSendKind(String sendKind) {
		this.sendKind = sendKind;
	}

	@Column(name = "COST_ITEM", length = 20)
	public String getCostItem() {
		return this.costItem;
	}

	public void setCostItem(String costItem) {
		this.costItem = costItem;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "SEND_STATE", length = 1)
	public String getSendState() {
		return this.sendState;
	}

	public void setSendState(String sendState) {
		this.sendState = sendState;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
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