package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 安全措施项目明细维护
 *
 * @author 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_WORKTICKET_SAFETY")
public class RunJWorkticketSafety implements java.io.Serializable {

	// Fields

	private Long id;
	private String workticketNo;
	private String safetyCode;
	private String attributeCode;
	private String equName;
	private String markcardCode;
	private String namecardContent;
	private Long frontKeyId;
	private String frontKeyword;
	private Long backKeyId;
	private String backKeyword;
	private Long flagId;
	private String flagDesc;
	private Long operationOrder;
	private String namecardPrintFlag;
	private String createBy;
	private Date createDate;
	private String isReturn;
	private String safetyExecuteCode;
	private String enterpriseCode;
	private String isUse;
	private String safetyExecuteBy;
	private Date safetyExecuteDate;
	private String safetyBreakoutBy;
	private Date safetyBreakoutDate;
	// Constructors

	/** default constructor */
	public RunJWorkticketSafety() {
	}

	/** minimal constructor */
	public RunJWorkticketSafety(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJWorkticketSafety(Long id, String workticketNo,
			String safetyCode, String attributeCode, String equName,
			String markcardCode, String namecardContent, Long frontKeyId,
			String frontKeyword, Long backKeyId, String backKeyword,
			Long flagId, Long operationOrder, String namecardPrintFlag,
			String createBy, Date createDate, String isReturn,
			String safetyExecuteCode, String enterpriseCode, String isUse,String safetyExecuteBy,
			Date safetyExecuteDate,String safetyBreakoutBy,Date safetyBreakoutDate) {
		this.id = id;
		this.workticketNo = workticketNo;
		this.safetyCode = safetyCode;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.markcardCode = markcardCode;
		this.namecardContent = namecardContent;
		this.frontKeyId = frontKeyId;
		this.frontKeyword = frontKeyword;
		this.backKeyId = backKeyId;
		this.backKeyword = backKeyword;
		this.flagId = flagId;
		this.operationOrder = operationOrder;
		this.namecardPrintFlag = namecardPrintFlag;
		this.createBy = createBy;
		this.createDate = createDate;
		this.isReturn = isReturn;
		this.safetyExecuteCode = safetyExecuteCode;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.safetyBreakoutBy=safetyBreakoutBy;
		this.safetyBreakoutDate=safetyBreakoutDate;
		this.safetyExecuteBy=safetyExecuteBy;
		this.safetyExecuteDate=safetyExecuteDate;
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

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "SAFETY_CODE", length = 3)
	public String getSafetyCode() {
		return this.safetyCode;
	}

	public void setSafetyCode(String safetyCode) {
		this.safetyCode = safetyCode;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "MARKCARD_CODE", length = 20)
	public String getMarkcardCode() {
		return this.markcardCode;
	}

	public void setMarkcardCode(String markcardCode) {
		this.markcardCode = markcardCode;
	}

	@Column(name = "NAMECARD_CONTENT", length = 30)
	public String getNamecardContent() {
		return this.namecardContent;
	}

	public void setNamecardContent(String namecardContent) {
		this.namecardContent = namecardContent;
	}

	@Column(name = "FRONT_KEY_ID", precision = 10, scale = 0)
	public Long getFrontKeyId() {
		return this.frontKeyId;
	}

	public void setFrontKeyId(Long frontKeyId) {
		this.frontKeyId = frontKeyId;
	}

	@Column(name = "FRONT_KEYWORD", length = 100)
	public String getFrontKeyword() {
		return this.frontKeyword;
	}

	public void setFrontKeyword(String frontKeyword) {
		this.frontKeyword = frontKeyword;
	}

	@Column(name = "BACK_KEY_ID", precision = 10, scale = 0)
	public Long getBackKeyId() {
		return this.backKeyId;
	}

	public void setBackKeyId(Long backKeyId) {
		this.backKeyId = backKeyId;
	}

	@Column(name = "BACK_KEYWORD", length = 100)
	public String getBackKeyword() {
		return this.backKeyword;
	}

	public void setBackKeyword(String backKeyword) {
		this.backKeyword = backKeyword;
	}

	@Column(name = "FLAG_ID", precision = 10, scale = 0)
	public Long getFlagId() {
		return this.flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}
	@Column(name = "FLAG_DESC",length = 2)
	public String getFlagDesc() { 
		return flagDesc;
	}

	public void setFlagDesc(String flagDesc) {
		this.flagDesc = flagDesc;
	}

	@Column(name = "OPERATION_ORDER", precision = 10, scale = 0)
	public Long getOperationOrder() {
		return this.operationOrder;
	}

	public void setOperationOrder(Long operationOrder) {
		this.operationOrder = operationOrder;
	}

	@Column(name = "NAMECARD_PRINT_FLAG", length = 2)
	public String getNamecardPrintFlag() {
		return this.namecardPrintFlag;
	}

	public void setNamecardPrintFlag(String namecardPrintFlag) {
		this.namecardPrintFlag = namecardPrintFlag;
	}

	@Column(name = "CREATE_BY", length = 30)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "IS_RETURN", length = 1)
	public String getIsReturn() {
		return this.isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}

	@Column(name = "SAFETY_EXECUTE_CODE", length = 3)
	public String getSafetyExecuteCode() {
		return this.safetyExecuteCode;
	}

	public void setSafetyExecuteCode(String safetyExecuteCode) {
		this.safetyExecuteCode = safetyExecuteCode;
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
	
	@Column(name = "safety_execute_by")
	public String getSafetyExecuteBy() {
		return safetyExecuteBy;
	}

	public void setSafetyExecuteBy(String safetyExecuteBy) {
		this.safetyExecuteBy = safetyExecuteBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "safety_execute_date")
	public Date getSafetyExecuteDate() {
		return safetyExecuteDate;
	}

	public void setSafetyExecuteDate(Date safetyExecuteDate) {
		this.safetyExecuteDate = safetyExecuteDate;
	}
	
	@Column(name = "safety_breakout_by")
	public String getSafetyBreakoutBy() {
		return safetyBreakoutBy;
	}

	public void setSafetyBreakoutBy(String safetyBreakoutBy) {
		this.safetyBreakoutBy = safetyBreakoutBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "safety_breakout_date")
	public Date getSafetyBreakoutDate() {
		return safetyBreakoutDate;
	}

	public void setSafetyBreakoutDate(Date safetyBreakoutDate) {
		this.safetyBreakoutDate = safetyBreakoutDate;
	} 
}