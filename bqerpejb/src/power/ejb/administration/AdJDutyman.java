package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJDutyman entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_DUTYMAN")
public class AdJDutyman implements java.io.Serializable {

	// Fields

	private Long id;
	private String dutyno;
	private Date dutytime;
	private String worktypeCode;
	private String subWorktypeCode;
	private String dutyman;
	private String position;
	private String dutytype;
	private String replaceman;
	private String leaveman;
	private String reason;
	private String isUse;
	private String crtUser;
	private String dcmStatus;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJDutyman() {
	}

	/** minimal constructor */
	public AdJDutyman(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJDutyman(Long id, String dutyno, Date dutytime,
			String worktypeCode, String subWorktypeCode, String dutyman,
			String position, String dutytype, String replaceman,
			String leaveman, String reason, String isUse, String crtUser,
			String dcmStatus, String updateUser, Date updateTime,
			String enterpriseCode) {
		this.id = id;
		this.dutyno = dutyno;
		this.dutytime = dutytime;
		this.worktypeCode = worktypeCode;
		this.subWorktypeCode = subWorktypeCode;
		this.dutyman = dutyman;
		this.position = position;
		this.dutytype = dutytype;
		this.replaceman = replaceman;
		this.leaveman = leaveman;
		this.reason = reason;
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

	@Column(name = "DUTYNO", length = 8)
	public String getDutyno() {
		return this.dutyno;
	}

	public void setDutyno(String dutyno) {
		this.dutyno = dutyno;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUTYTIME", length = 7)
	public Date getDutytime() {
		return this.dutytime;
	}

	public void setDutytime(Date dutytime) {
		this.dutytime = dutytime;
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

	@Column(name = "DUTYMAN", length = 20)
	public String getDutyman() {
		return this.dutyman;
	}

	public void setDutyman(String dutyman) {
		this.dutyman = dutyman;
	}

	@Column(name = "POSITION", length = 30)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "DUTYTYPE", length = 2)
	public String getDutytype() {
		return this.dutytype;
	}

	public void setDutytype(String dutytype) {
		this.dutytype = dutytype;
	}

	@Column(name = "REPLACEMAN", length = 20)
	public String getReplaceman() {
		return this.replaceman;
	}

	public void setReplaceman(String replaceman) {
		this.replaceman = replaceman;
	}

	@Column(name = "LEAVEMAN", length = 20)
	public String getLeaveman() {
		return this.leaveman;
	}

	public void setLeaveman(String leaveman) {
		this.leaveman = leaveman;
	}

	@Column(name = "REASON", length = 100)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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