package power.ejb.workticket;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCWorkticketBusiStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_C_WORKTICKET_BUSI_STATUS", schema = "POWER")
public class RunCWorkticketBusiStatus implements java.io.Serializable {

	// Fields

	private Long workticketStausId;
	private String workticketStatusName;

	// Constructors

	/** default constructor */
	public RunCWorkticketBusiStatus() {
	}

	/** minimal constructor */
	public RunCWorkticketBusiStatus(Long workticketStausId) {
		this.workticketStausId = workticketStausId;
	}

	/** full constructor */
	public RunCWorkticketBusiStatus(Long workticketStausId,
			String workticketStatusName) {
		this.workticketStausId = workticketStausId;
		this.workticketStatusName = workticketStatusName;
	}

	// Property accessors
	@Id
	@Column(name = "WORKTICKET_STAUS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWorkticketStausId() {
		return this.workticketStausId;
	}

	public void setWorkticketStausId(Long workticketStausId) {
		this.workticketStausId = workticketStausId;
	}

	@Column(name = "WORKTICKET_STATUS_NAME", length = 20)
	public String getWorkticketStatusName() {
		return this.workticketStatusName;
	}

	public void setWorkticketStatusName(String workticketStatusName) {
		this.workticketStatusName = workticketStatusName;
	}

}