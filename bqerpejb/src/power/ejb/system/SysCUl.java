package power.ejb.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysCUl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_C_UL")
public class SysCUl implements java.io.Serializable { 
	private static final long serialVersionUID = 3850197615646833988L;
	private Long workerId;
	private String enterpriseCode;
	private String workerCode;
	private String workerName;
	private String loginCode;
	private String loginPwd;
	private String isUse;
	private String style;
	private String email;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public SysCUl() {
	}

	/** minimal constructor */
	public SysCUl(Long workerId) {
		this.workerId = workerId;
	}

	/** full constructor */
	public SysCUl(Long workerId, String enterpriseCode, String workerCode,
			String workerName, String loginCode, String loginPwd, String isUse,
			String style, String email, String modifyBy, Date modifyDate) {
		this.workerId = workerId;
		this.enterpriseCode = enterpriseCode;
		this.workerCode = workerCode;
		this.workerName = workerName;
		this.loginCode = loginCode;
		this.loginPwd = loginPwd;
		this.isUse = isUse;
		this.style = style;
		this.email = email;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "WORKER_ID",  nullable = false, precision = 10, scale = 0)
	public Long getWorkerId() {
		return this.workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 30)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "WORKER_CODE", length = 30)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "WORKER_NAME", length = 20)
	public String getWorkerName() {
		return this.workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	@Column(name = "LOGIN_CODE", length = 20)
	public String getLoginCode() {
		return this.loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	@Column(name = "LOGIN_PWD", length = 32)
	public String getLoginPwd() {
		return this.loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "STYLE", length = 10)
	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}