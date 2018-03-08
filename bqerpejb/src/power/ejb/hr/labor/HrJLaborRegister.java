package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLaborRegister entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_J_LABOR_REGISTER")
public class HrJLaborRegister implements java.io.Serializable {

	// Fields

	private Long laborRegisterId;
	private String workCode;
	private Date sendDate;
	private Long laborMaterialId;
	private Long sendNum;
	private Long actualNum;
	private String status;
	private String enterpriseCode;
	private String isUse;
	private String sendSeason;
	// Constructors

	/** default constructor */
	public HrJLaborRegister() {
	}

	/** minimal constructor */
	public HrJLaborRegister(Long laborRegisterId) {
		this.laborRegisterId = laborRegisterId;
	}

	/** full constructor */
	public HrJLaborRegister(Long laborRegisterId, String workCode,
			Date sendDate, Long laborMaterialId, Long sendNum, Long actualNum,
			String status, String enterpriseCode, String isUse) {
		this.laborRegisterId = laborRegisterId;
		this.workCode = workCode;
		this.sendDate = sendDate;
		this.laborMaterialId = laborMaterialId;
		this.sendNum = sendNum;
		this.actualNum = actualNum;
		this.status = status;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LABOR_REGISTER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLaborRegisterId() {
		return this.laborRegisterId;
	}

	public void setLaborRegisterId(Long laborRegisterId) {
		this.laborRegisterId = laborRegisterId;
	}

	@Column(name = "WORK_CODE", length = 30)
	public String getWorkCode() {
		return this.workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SEND_DATE", length = 7)
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "LABOR_MATERIAL_ID", precision = 10, scale = 0)
	public Long getLaborMaterialId() {
		return this.laborMaterialId;
	}

	public void setLaborMaterialId(Long laborMaterialId) {
		this.laborMaterialId = laborMaterialId;
	}

	@Column(name = "SEND_NUM", precision = 10, scale = 0)
	public Long getSendNum() {
		return this.sendNum;
	}

	public void setSendNum(Long sendNum) {
		this.sendNum = sendNum;
	}

	@Column(name = "ACTUAL_NUM", precision = 10, scale = 0)
	public Long getActualNum() {
		return this.actualNum;
	}

	public void setActualNum(Long actualNum) {
		this.actualNum = actualNum;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@Column(name = "SEND_SEASON", length = 10)
	public String getSendSeason() {
		return sendSeason;
	}

	public void setSendSeason(String sendSeason) {
		this.sendSeason = sendSeason;
	}

}