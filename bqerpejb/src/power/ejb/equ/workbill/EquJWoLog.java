package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJWoLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_WO_LOG", schema = "POWER")
public class EquJWoLog implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String logContent;

	// Constructors

	/** default constructor */
	public EquJWoLog() {
	}

	/** minimal constructor */
	public EquJWoLog(Long id, String woCode) {
		this.id = id;
		this.woCode = woCode;
	}

	/** full constructor */
	public EquJWoLog(Long id, String woCode, String logContent) {
		this.id = id;
		this.woCode = woCode;
		this.logContent = logContent;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "LOG_CONTENT", length = 1000)
	public String getLogContent() {
		return this.logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

}