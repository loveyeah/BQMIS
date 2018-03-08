package power.ejb.message.form;

import java.util.Date;

public class Message {
	private Long id;
	private Long messageId;
	private String sendById;
	private String receiveById;
	private String sendDate;
	private Long messageStatus;
	private String senderName;
	private String receiverName;
	private String statusFlag;
	private String title;
	private String text;
	private Long docTypeId;
	private String docName;
	private String zbbmtxName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public String getSendById() {
		return sendById;
	}
	public void setSendById(String sendById) {
		this.sendById = sendById;
	}
	public String getReceiveById() {
		return receiveById;
	}
	public void setReceiveById(String receiveById) {
		this.receiveById = receiveById;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public Long getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(Long messageStatus) {
		this.messageStatus = messageStatus;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getDocTypeId() {
		return docTypeId;
	}
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getZbbmtxName() {
		return zbbmtxName;
	}
	public void setZbbmtxName(String zbbmtxName) {
		this.zbbmtxName = zbbmtxName;
	}
}
