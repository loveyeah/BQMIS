/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.form;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chaihao
 * 
 */
public class RegularWorkRightSetInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 序号 */
	private String id;
	/** 人员编码 */
	private String userCode;
	/** 姓名 */
	private String name;
	/** 部门名称 */
	private String depName;
	/** 修改时间 */
	private String updateTime;

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
	 * @return 人员编码
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @param 人员编码
	 *            设置人员编码
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return 修改时间
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param 修改时间
	 *            设置修改时间
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
