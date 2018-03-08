package power.ejb.workticket.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJWorkticketMap entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_MAP")
public class RunJWorkticketMap implements java.io.Serializable {

	// Fields

	private Long id;
	private String workticketNo;
	private byte[] workticketMap;

	// Constructors

	/** default constructor */
	public RunJWorkticketMap() {
	}

	/** minimal constructor */
	public RunJWorkticketMap(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJWorkticketMap(Long id, String workticketNo, byte[] workticketMap) {
		this.id = id;
		this.workticketNo = workticketNo;
		this.workticketMap = workticketMap;
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

	@Column(name = "WORKTICKET_MAP")
	public byte[] getWorkticketMap() {
		return this.workticketMap;
	}

	public void setWorkticketMap(byte[] workticketMap) {
		this.workticketMap = workticketMap;
	}

}