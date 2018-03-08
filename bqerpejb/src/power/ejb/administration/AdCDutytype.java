/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdCDutytype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_C_DUTYTYPE")
public class AdCDutytype implements java.io.Serializable {

	// Fields

	/** 序号 */
	private Long id;
	/** 工作类别 */
	private String worktypeCode;
	/** 值别 */
	private String dutyType;
	/** 值别名称 */
	private String dutyTypeName;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 是否使用 */
	private String isUse;
	/** 修改人 */
	private String updateUser;
	/** 修改时间 */
	private Date updateTime;
	/** 企业代码 */
	private String enterpriseCode;

	// Constructors

	/**
	 * 默认构造函数
	 */
	public AdCDutytype() {
	}

	/**
	 * 单参数构造函数
	 * 
	 * @param id 序号
	 */
	public AdCDutytype(Long id) {
		this.id = id;
	}

	/**
	 * 全参数构造函数
	 * @param id 序号
	 * @param worktypeCode 工作类别
	 * @param dutyType 值别
	 * @param dutyTypeName 值别名称
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param isUse 是否使用
	 * @param updateUser 修改人
	 * @param updateTime 修改时间
	 * @param enterpriseCode 企业代码
	 */
	public AdCDutytype(Long id, String worktypeCode, String dutyType,
			String dutyTypeName, Date startTime, Date endTime, String isUse,
			String updateUser, Date updateTime, String enterpriseCode) {
		this.id = id;
		this.worktypeCode = worktypeCode;
		this.dutyType = dutyType;
		this.dutyTypeName = dutyTypeName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	/**
	 * @return 序号
	 */
	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	/**
	 * @param 序号
	 *            设置序号
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 工作类别
	 */
	@Column(name = "WORKTYPE_CODE", length = 2)
	public String getWorktypeCode() {
		return this.worktypeCode;
	}

	/**
	 * @param 工作类别
	 *            设置工作类别
	 */
	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}

	/**
	 * @return 值别
	 */
	@Column(name = "DUTY_TYPE", length = 2)
	public String getDutyType() {
		return this.dutyType;
	}

	/**
	 * @param 工作类别
	 *            设置值别
	 */
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	/**
	 * @return 值别名称
	 */
	@Column(name = "DUTY_TYPE_NAME", length = 40)
	public String getDutyTypeName() {
		return this.dutyTypeName;
	}

	/**
	 * @param 工作类别
	 *            设置值别名称
	 */
	public void setDutyTypeName(String dutyTypeName) {
		this.dutyTypeName = dutyTypeName;
	}

	/**
	 * @return 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	/**
	 * @param 开始时间
	 *            设置开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	/**
	 * @param 开始时间
	 *            设置结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return 是否使用
	 */
	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	/**
	 * @param 开始时间
	 *            设置是否使用
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	/**
	 * @return 修改人
	 */
	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	/**
	 * @param 修改人
	 *            设置修改人
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * @param 修改时间
	 *            设置修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return 企业代码
	 */
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	/**
	 * @param 企业代码
	 *            设置企业代码
	 */
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}