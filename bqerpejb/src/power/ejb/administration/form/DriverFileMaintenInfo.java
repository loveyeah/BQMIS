/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * @author chaihao
 * 
 */
public class DriverFileMaintenInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 序号 */
	private String id;
	/** 姓名 */
	private String name;
	/** 性别 */
	private String sex;
	/** 年龄 */
	private String ages;
	/** 所在部门 */
	private String depName;
	/** 驾照类型 */
	private String licence;
	/** 驾照号码 */
	private String licenceNo;
	/** 办照时间 */
	private String licenceDate;
	/** 年检时间 */
	private String checkDate;
	/** 手机号码 */
	private String mobileNo;
	/** 家庭电话 */
	private String telNo;
	/** 家庭住址 */
	private String homeAddr;
	/** 通讯地址 */
	private String comAddr;

	/**
	 * @return 序号
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param 序号
	 *            设置序号
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param 姓名
	 *            设置姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 性别
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param 性别
	 *            设置性别
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return 年龄
	 */
	public String getAges() {
		return ages;
	}

	/**
	 * @param 年龄
	 *            设置年龄
	 */
	public void setAges(String ages) {
		this.ages = ages;
	}

	/**
	 * @return 部门名称
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param 部门名称
	 *            设置部门名称
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * @return 驾照类型
	 */
	public String getLicence() {
		return licence;
	}

	/**
	 * @param 驾照类型
	 *            设置驾照类型
	 */
	public void setLicence(String licence) {
		this.licence = licence;
	}

	/**
	 * @return 驾照号码
	 */
	public String getLicenceNo() {
		return licenceNo;
	}

	/**
	 * @param 驾照号码
	 *            设置驾照号码
	 */
	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	/**
	 * @return 办照日期
	 */
	public String getLicenceDate() {
		return licenceDate;
	}

	/**
	 * @param 办照日期
	 *            设置办照日期
	 */
	public void setLicenceDate(String licenceDate) {
		this.licenceDate = licenceDate;
	}

	/**
	 * @return 年检时间
	 */
	public String getCheckDate() {
		return checkDate;
	}

	/**
	 * @param 年检时间
	 *            设置年检时间
	 */
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	/**
	 * @return 手机号码
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param 手机号码
	 *            设置手机号码
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * @return 家庭电话
	 */
	public String getTelNo() {
		return telNo;
	}

	/**
	 * @param 家庭电话
	 *            设置家庭电话
	 */
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	/**
	 * @return 家庭住址
	 */
	public String getHomeAddr() {
		return homeAddr;
	}

	/**
	 * @param 家庭住址
	 *            设置家庭住址
	 */
	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	/**
	 * @return 通讯地址
	 */
	public String getComAddr() {
		return comAddr;
	}

	/**
	 * @param 通讯地址
	 *            设置通讯地址
	 */
	public void setComAddr(String comAddr) {
		this.comAddr = comAddr;
	}
}
