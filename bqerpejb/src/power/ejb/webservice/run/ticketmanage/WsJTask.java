package power.ejb.webservice.run.ticketmanage;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * WsJTask entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "WS_J_TASK")
public class WsJTask implements java.io.Serializable {

	// Fields

	private Long id;
	private Long taskType;
	private String taskNo;
	private String taskName;
	private String receiver;
	private Date receiveDate;
	private String taskStatus;

	// Constructors

	/** default constructor */
	public WsJTask() {
	}

	/** minimal constructor */
	public WsJTask(Long id) {
		this.id = id;
	}

	/** full constructor */
	public WsJTask(Long id, Long taskType, String taskNo, String taskName,
			String receiver, Date receiveDate, String taskStatus) {
		this.id = id;
		this.taskType = taskType;
		this.taskNo = taskNo;
		this.taskName = taskName;
		this.receiver = receiver;
		this.receiveDate = receiveDate;
		this.taskStatus = taskStatus;
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

	@Column(name = "TASK_TYPE", precision = 1, scale = 0)
	public Long getTaskType() {
		return this.taskType;
	}

	public void setTaskType(Long taskType) {
		this.taskType = taskType;
	}

	@Column(name = "TASK_NO", length = 20)
	public String getTaskNo() {
		return this.taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	@Column(name = "TASK_NAME", length = 256)
	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Column(name = "RECEIVER", length = 16)
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DATE", length = 7)
	public Date getReceiveDate() {
		return this.receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	@Column(name = "TASK_STATUS", length = 1)
	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

}