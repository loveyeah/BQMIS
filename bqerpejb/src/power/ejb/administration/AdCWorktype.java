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
 * AdCWorktype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_C_WORKTYPE")
public class AdCWorktype implements java.io.Serializable {

	// Fields

	/** 序号 */
	private Long id;
	/** 类别编码 */
	private String worktypeCode;
	/** 类别名称 */
	private String worktypeName;
	/** 子类别编码 */
	private String subWorktypeCode;
	/** 子类别名称 */
	private String subWorktypeName;
	/** 是否使用 */
	private String isUse;
	/** 检索码 */
	private String retrieveCode;
	/** 修改人 */
	private String updateUser;
	/** 修改日期 */
	private Date updateTime;
	/** 企业代码 */
	private String enterpriseCode;

	// Constructors

	/**
	 * 默认构造函数
	 */
	public AdCWorktype() {
	}

	/**
	 * 单参数构造函数
	 * 
	 * @param id 序号
	 */
	public AdCWorktype(Long id) {
		this.id = id;
	}

	/**
	 * 全参数构造函数
	 * 
	 * @param id 序号
	 * @param worktypeCode 类别编码
	 * @param worktypeName 类别名称
	 * @param subWorktypeCode 子类别编码
	 * @param subWorktypeName 子类别名称
	 * @param isUse 是否使用
	 * @param retrieveCode 检索码
	 * @param useFlg 使用标志
	 * @param updateUser 修改人
	 * @param updateTime 修改时间
	 * @param enterpriseCode 企业代码
	 */
	public AdCWorktype(Long id, String worktypeCode, String worktypeName,
			String subWorktypeCode, String subWorktypeName, String isUse,
			String retrieveCode, String useFlg, String updateUser,
			Date updateTime, String enterpriseCode) {
		this.id = id;
		this.worktypeCode = worktypeCode;
		this.worktypeName = worktypeName;
		this.subWorktypeCode = subWorktypeCode;
		this.subWorktypeName = subWorktypeName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	/**
	 * @return 序号
	 */
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
	 * @return 类别编码
	 */
	@Column(name = "WORKTYPE_CODE", length = 2)
	public String getWorktypeCode() {
		return this.worktypeCode;
	}

	/**
	 * @param 类别编码
	 *            设置类别编码
	 */
	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}

	/**
	 * @return 类别名称
	 */
	@Column(name = "WORKTYPE_NAME", length = 40)
	public String getWorktypeName() {
		return this.worktypeName;
	}

	/**
	 * @param 类别名称
	 *            设置类别名称
	 */
	public void setWorktypeName(String worktypeName) {
		this.worktypeName = worktypeName;
	}

	/**
	 * @return 子类别编码
	 */
	@Column(name = "SUB_WORKTYPE_CODE", length = 4)
	public String getSubWorktypeCode() {
		return this.subWorktypeCode;
	}

	/**
	 * @param 子类别编码
	 *            设置子类别编码
	 */
	public void setSubWorktypeCode(String subWorktypeCode) {
		this.subWorktypeCode = subWorktypeCode;
	}

	/**
	 * @return 子类别名称
	 */
	@Column(name = "SUB_WORKTYPE_NAME", length = 40)
	public String getSubWorktypeName() {
		return this.subWorktypeName;
	}

	/**
	 * @param 子类别名称
	 *            设置子类别名称
	 */
	public void setSubWorktypeName(String subWorktypeName) {
		this.subWorktypeName = subWorktypeName;
	}

	/**
	 * @return 是否使用
	 */
	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	/**
	 * @param 是否使用
	 *            设置是否使用
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	/**
	 * @return 检索码
	 */
	@Column(name = "RETRIEVE_CODE", length = 8)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	/**
	 * @param 检索码
	 *            设置是检索码
	 */
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
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