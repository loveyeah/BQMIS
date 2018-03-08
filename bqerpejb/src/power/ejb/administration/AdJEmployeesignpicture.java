package power.ejb.administration;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJEmployeesignpicture entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_EMPLOYEESIGNPICTURE")
public class AdJEmployeesignpicture implements java.io.Serializable {

	// Fields

	private Long id;
	private String workerCode;
	private byte[] signPic;
	private String updateUser;
	private Date updateTime;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJEmployeesignpicture() {
	}

	/** minimal constructor */
	public AdJEmployeesignpicture(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJEmployeesignpicture(Long id, String workerCode, byte[] signPic,
			String updateUser, Date updateTime, String isUse, String enterpriseCode) {
		this.id = id;
		this.workerCode = workerCode;
		this.signPic = signPic;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	};
	
	// Property accessors
	@Id
	@Column(name = "ID", nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "WORKER_CODE", length = 20)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "SIGN_PIC")
	public byte[] getSignPic() {
		return this.signPic;
	}

	public void setSignPic(byte[] signPic) {
		this.signPic = signPic;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}