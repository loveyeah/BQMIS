package power.ejb.hr.message.form;

import java.util.Date;

@SuppressWarnings("serial")
public class HrJMessageForm implements java.io.Serializable{
	
	private String messageId;
	private String userCode;
	private String userPsw;
	private String msgPerson;
	private String msgName;
	private String msgContent;
	private String isSend;
	private String entryBy;
	private String entryName;
	private String entryDate;
	
	public HrJMessageForm()
	{
		this.messageId = "";
		this.userCode = "";
		this.userPsw = "";
		this.msgPerson = "";
		this.msgName = "";
		this.msgContent = "";
		this.isSend = "";
		this.entryBy = "";
		this.entryName = "";
		this.entryDate = "";
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserPsw() {
		return userPsw;
	}

	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}

	public String getMsgPerson() {
		return msgPerson;
	}

	public void setMsgPerson(String msgPerson) {
		this.msgPerson = msgPerson;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	
	
}
