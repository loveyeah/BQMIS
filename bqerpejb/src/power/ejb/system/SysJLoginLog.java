package power.ejb.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysJLoginLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_J_LOGIN_LOG")
public class SysJLoginLog implements java.io.Serializable {

	// Fields

	private Long id;
	private String workerCode;
	private String hostName;
	private String hostIp;
	private Date loginDate;
	private String enterpriseCode;
	private String loginFile; 

	// Constructors

	/** default constructor */
	public SysJLoginLog() {
	}

	/** minimal constructor */
	public SysJLoginLog(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SysJLoginLog(Long id, String workerCode, String hostName,
			String hostIp, Date loginDate, String enterpriseCode,String loginFile) {
		this.id = id;
		this.workerCode = workerCode;
		this.hostName = hostName;
		this.hostIp = hostIp;
		this.loginDate = loginDate;
		this.enterpriseCode = enterpriseCode;
		this.loginFile = loginFile;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "WORKER_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "HOST_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Column(name = "HOST_IP", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getHostIp() {
		return this.hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LOGIN_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	@Column(name = "LOGIN_FILE", length = 120) //modify by fyyang 090729 长度由100改为120
	public String getLoginFile() {
		return loginFile;
	}

	public void setLoginFile(String loginFile) {
		this.loginFile = loginFile;
	}
	
	 

}