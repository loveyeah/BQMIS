package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJOutreg entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_OUTREG")
public class AdJOutreg implements java.io.Serializable {

	// Fields

	private Long id;
	private String agent;
	private String firm;
	private Date outDate;
	private String wpName;
	private String standard;
	private String unit;
	private Double num;
	private String note;
	private String reason;
	private String onduty;
	private String signState;
	private String workFlowNo;
	private String isUse;
	private String crtUser;
	private String dcmStatus;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJOutreg() {
	}

	/** minimal constructor */
	public AdJOutreg(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJOutreg(Long id, String agent, String firm, Date outDate,
			String wpName, String standard, String unit, Double num, String note,
			String reason, String onduty, String signState, String workFlowNo,
			String isUse, String crtUser, String dcmStatus, String updateUser,
			Date updateTime,String enterpriseCode) {
		this.id = id;
		this.agent = agent;
		this.firm = firm;
		this.outDate = outDate;
		this.wpName = wpName;
		this.standard = standard;
		this.unit = unit;
		this.num = num;
		this.note = note;
		this.reason = reason;
		this.onduty = onduty;
		this.signState = signState;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.crtUser = crtUser;
		this.dcmStatus = dcmStatus;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "AGENT", length = 20)
	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@Column(name = "FIRM", length = 40)
	public String getFirm() {
		return this.firm;
	}

	public void setFirm(String firm) {
		this.firm = firm;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OUT_DATE", length = 7)
	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	@Column(name = "WP_NAME", length = 50)
	public String getWpName() {
		return this.wpName;
	}

	public void setWpName(String wpName) {
		this.wpName = wpName;
	}

	@Column(name = "STANDARD", length = 25)
	public String getStandard() {
		return this.standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	@Column(name = "UNIT", length = 8)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "NUM", precision = 10, scale = 0)
	public Double getNum() {
		return this.num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	@Column(name = "NOTE", length = 1000)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "REASON", length = 100)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "ONDUTY", length = 10)
	public String getOnduty() {
		return this.onduty;
	}

	public void setOnduty(String onduty) {
		this.onduty = onduty;
	}

	@Column(name = "SIGN_STATE", length = 1)
	public String getSignState() {
		return this.signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "CRT_USER", length = 10)
	public String getCrtUser() {
		return this.crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}