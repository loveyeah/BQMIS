package power.ejb.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysJMessage entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_J_MESSAGE")
public class SysJMessage implements java.io.Serializable {

	// Fields

	private Long messageId;
	private String title;
	private String text;
	private Long docTypeId;
	private String docName;
	private byte[] docContent;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public SysJMessage() {
	}

	/** minimal constructor */
	public SysJMessage(Long messageId, String title, String enterpriseCode,
			String isUse) {
		this.messageId = messageId;
		this.title = title;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public SysJMessage(Long messageId, String title, String text,
			Long docTypeId, String docName, byte[] docContent,
			String enterpriseCode, String isUse) {
		this.messageId = messageId;
		this.title = title;
		this.text = text;
		this.docTypeId = docTypeId;
		this.docName = docName;
		this.docContent = docContent;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
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

	@Column(name = "TITLE", nullable = false, length = 80)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "TEXT", length = 2000)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "DOC_TYPE_ID", precision = 10, scale = 0)
	public Long getDocTypeId() {
		return this.docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	@Column(name = "DOC_NAME", length = 80)
	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	@Column(name = "DOC_CONTENT")
	public byte[] getDocContent() {
		return this.docContent;
	}

	public void setDocContent(byte[] docContent) {
		this.docContent = docContent;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}