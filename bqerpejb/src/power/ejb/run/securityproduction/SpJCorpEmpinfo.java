package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJCorpEmpinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_CORP_EMPINFO", schema = "POWER")
public class SpJCorpEmpinfo implements java.io.Serializable {

	// Fields

	private Long empId;
	private String empName;
	private String empDuty;
	private String modifyby;
	private Date modifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJCorpEmpinfo() {
	}

	/** minimal constructor */
	public SpJCorpEmpinfo(Long empId) {
		this.empId = empId;
	}

	/** full constructor */
	public SpJCorpEmpinfo(Long empId, String empName, String empDuty,
			String modifyby, Date modifyDate, String isUse,
			String enterpriseCode) {
		this.empId = empId;
		this.empName = empName;
		this.empDuty = empDuty;
		this.modifyby = modifyby;
		this.modifyDate = modifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "EMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "EMP_NAME", length = 20)
	public String getEmpName() {
		return this.empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	@Column(name = "EMP_DUTY", length = 50)
	public String getEmpDuty() {
		return this.empDuty;
	}

	public void setEmpDuty(String empDuty) {
		this.empDuty = empDuty;
	}

	@Column(name = "MODIFYBY", length = 30)
	public String getModifyby() {
		return this.modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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