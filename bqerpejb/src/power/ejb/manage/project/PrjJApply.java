package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJApply entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_APPLY")
public class PrjJApply implements java.io.Serializable {

	// Fields

	private Long applyId;
	private String contractName;
	private Long conId;
	private Date startDate;
	private Date endDate;
	private String contractorName;
	private Long contractorId;
	private String chargeBy;
	private String testResult;
	private String authorizeItem;
	private String personRegister;
	private String articleRegister;
	private String idCard;
	private String operateCard;
	private String cautionMoney;
	private String handInCard;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;
	private Long workFlowNo;
	private Long statusId;
	private Long prjId; //add by fyyang 20100909

	// Constructors

	/** default constructor */
	public PrjJApply() {
	}

	/** minimal constructor */
	public PrjJApply(Long applyId) {
		this.applyId = applyId;
	}

	/** full constructor */
	public PrjJApply(Long applyId, String contractName, Long conId,
			Date startDate, Date endDate, String contractorName,
			Long contractorId, String chargeBy, String testResult,
			String authorizeItem, String personRegister,
			String articleRegister, String idCard, String operateCard,
			String cautionMoney, String handInCard, String entryBy,
			Date entryDate, String enterpriseCode, String isUse,
			Long workFlowNo, Long statusId,Long prjId) {
		this.applyId = applyId;
		this.contractName = contractName;
		this.conId = conId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractorName = contractorName;
		this.contractorId = contractorId;
		this.chargeBy = chargeBy;
		this.testResult = testResult;
		this.authorizeItem = authorizeItem;
		this.personRegister = personRegister;
		this.articleRegister = articleRegister;
		this.idCard = idCard;
		this.operateCard = operateCard;
		this.cautionMoney = cautionMoney;
		this.handInCard = handInCard;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.workFlowNo = workFlowNo;
		this.statusId = statusId;
		this.prjId=prjId;
	}

	// Property accessors
	@Id
	@Column(name = "APPLY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApplyId() {
		return this.applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	@Column(name = "CONTRACT_NAME", length = 100)
	public String getContractName() {
		return this.contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Column(name = "CON_ID", precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CONTRACTOR_NAME", length = 100)
	public String getContractorName() {
		return this.contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	@Column(name = "CONTRACTOR_ID", precision = 10, scale = 0)
	public Long getContractorId() {
		return this.contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	@Column(name = "CHARGE_BY", length = 20)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "TEST_RESULT", length = 1)
	public String getTestResult() {
		return this.testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	@Column(name = "AUTHORIZE_ITEM", length = 1)
	public String getAuthorizeItem() {
		return this.authorizeItem;
	}

	public void setAuthorizeItem(String authorizeItem) {
		this.authorizeItem = authorizeItem;
	}

	@Column(name = "PERSON_REGISTER", length = 1)
	public String getPersonRegister() {
		return this.personRegister;
	}

	public void setPersonRegister(String personRegister) {
		this.personRegister = personRegister;
	}

	@Column(name = "ARTICLE_REGISTER", length = 1)
	public String getArticleRegister() {
		return this.articleRegister;
	}

	public void setArticleRegister(String articleRegister) {
		this.articleRegister = articleRegister;
	}

	@Column(name = "ID_CARD", length = 1)
	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "OPERATE_CARD", length = 1)
	public String getOperateCard() {
		return this.operateCard;
	}

	public void setOperateCard(String operateCard) {
		this.operateCard = operateCard;
	}

	@Column(name = "CAUTION_MONEY", length = 1)
	public String getCautionMoney() {
		return this.cautionMoney;
	}

	public void setCautionMoney(String cautionMoney) {
		this.cautionMoney = cautionMoney;
	}

	@Column(name = "HAND_IN_CARD", length = 1)
	public String getHandInCard() {
		return this.handInCard;
	}

	public void setHandInCard(String handInCard) {
		this.handInCard = handInCard;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
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

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "STATUS_ID", precision = 2, scale = 0)
	public Long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "PRJ_ID", precision = 10, scale = 0)
	public Long getPrjId() {
		return prjId;
	}

	public void setPrjId(Long prjId) {
		this.prjId = prjId;
	}

}