package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJOnduty entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_ONDUTY")
public class AdJOnduty implements java.io.Serializable {

/**值班日志登记表**/
	
	/**序号**/
	private Long id;
	/**值班记事时间**/
	private Date regTime;
	/**值班记事内容**/
	private String regText;
	/**工作类别**/
	private String worktypeCode;
	/**子工作类别**/
	private String subWorktypeCode;
	/**值别**/
	private String dutyType;
	/**值班人**/ 
	private String dutyman;
	/**是否使用**/
	private String isUse;
	/**登记人**/
	private String crtUser;
	/**单据状态**/
	private String dcmStatus;
	/**修改人**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**企业代码**/
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJOnduty() {
	}

	/** minimal constructor */
	public AdJOnduty(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJOnduty(Long id, Date regTime, String regText,
			String worktypeCode, String subWorktypeCode, String dutyType,
			String dutyman, String isUse, String crtUser, String dcmStatus,
			String updateUser, Date updateTime, String enterpriseCode) {
		this.id = id;
		this.regTime = regTime;
		this.regText = regText;
		this.worktypeCode = worktypeCode;
		this.subWorktypeCode = subWorktypeCode;
		this.dutyType = dutyType;
		this.dutyman = dutyman;
		this.isUse = isUse;
		this.crtUser = crtUser;
		this.dcmStatus = dcmStatus;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_TIME", length = 7)
	public Date getRegTime() {
		return this.regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	@Column(name = "REG_TEXT", length = 1400)
	public String getRegText() {
		return this.regText;
	}

	public void setRegText(String regText) {
		this.regText = regText;
	}

	@Column(name = "WORKTYPE_CODE", length = 2)
	public String getWorktypeCode() {
		return this.worktypeCode;
	}

	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}

	@Column(name = "SUB_WORKTYPE_CODE", length = 4)
	public String getSubWorktypeCode() {
		return this.subWorktypeCode;
	}

	public void setSubWorktypeCode(String subWorktypeCode) {
		this.subWorktypeCode = subWorktypeCode;
	}

	@Column(name = "DUTY_TYPE", length = 2)
	public String getDutyType() {
		return this.dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	@Column(name = "DUTYMAN", length = 20)
	public String getDutyman() {
		return this.dutyman;
	}

	public void setDutyman(String dutyman) {
		this.dutyman = dutyman;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "CRT_USER", length = 20)
	public String getCrtUser() {
		return this.crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}