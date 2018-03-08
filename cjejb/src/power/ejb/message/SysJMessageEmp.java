package power.ejb.message;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysJMessageEmp entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_J_MESSAGE_EMP")
public class SysJMessageEmp implements java.io.Serializable {

	// Fields

	private Long id;
	private Long messageId;
	private String sendById;
	private String receiveById;
	private Date sendDate;
	private Long messageStatus;

	// Constructors

	/** default constructor */
	public SysJMessageEmp() {
	}

	/** full constructor */
	public SysJMessageEmp(Long id, Long messageId, String sendById,
			String receiveById, Date sendDate, Long messageStatus) {
		this.id = id;
		this.messageId = messageId;
		this.sendById = sendById;
		this.receiveById = receiveById;
		this.sendDate = sendDate;
		this.messageStatus = messageStatus;
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

	@Column(name = "MESSAGE_ID", nullable = false, precision = 10, scale = 0)
	public Long getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	@Column(name = "SEND_BY_ID", nullable = false, length = 16)
	public String getSendById() {
		return this.sendById;
	}

	public String setSendById(String sendById) {
		return this.sendById = sendById;
	}

	@Column(name = "RECEIVE_BY_ID", nullable = false, length = 16)
	public String getReceiveById() {
		return this.receiveById;
	}

	public void setReceiveById(String receiveById) {
		this.receiveById = receiveById;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_DATE", nullable = false)
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "MESSAGE_STATUS", nullable = false, precision = 1, scale = 0)
	public Long getMessageStatus() {
		return this.messageStatus;
	}

	public void setMessageStatus(Long messageStatus) {
		this.messageStatus = messageStatus;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		// TODO Auto-generated method stub
		
	}

}