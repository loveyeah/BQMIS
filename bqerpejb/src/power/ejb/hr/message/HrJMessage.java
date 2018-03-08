package power.ejb.hr.message;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJMessage entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_MESSAGE", schema = "POWER")
public class HrJMessage implements java.io.Serializable {

	// Fields

	private Long messageId;
	private String userCode;
	private String userPsw;
	private String msgPerson;
	private String msgContent;
	private String isSend;
	private String entryBy;
	private Date entryDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJMessage() {
	}

	/** minimal constructor */
	public HrJMessage(Long messageId) {
		this.messageId = messageId;
	}

	/** full constructor */
	public HrJMessage(Long messageId, String userCode, String userPsw,
			String msgPerson, String msgContent, String isSend, String entryBy,
			Date entryDate, String isUse, String enterpriseCode) {
		this.messageId = messageId;
		this.userCode = userCode;
		this.userPsw = userPsw;
		this.msgPerson = msgPerson;
		this.msgContent = msgContent;
		this.isSend = isSend;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "MESSAGE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	@Column(name = "USER_CODE", length = 20)
	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "USER_PSW", length = 20)
	public String getUserPsw() {
		return this.userPsw;
	}

	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}

	@Column(name = "MSG_PERSON", length = 1000)
	public String getMsgPerson() {
		return this.msgPerson;
	}

	public void setMsgPerson(String msgPerson) {
		this.msgPerson = msgPerson;
	}

	@Column(name = "MSG_CONTENT", length = 300)
	public String getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	@Column(name = "IS_SEND", length = 1)
	public String getIsSend() {
		return this.isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
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