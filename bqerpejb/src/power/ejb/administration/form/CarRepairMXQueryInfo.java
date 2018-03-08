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
public class CarRepairMXQueryInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 序号 */
	private String whId;
	/** 项目名称 */
	private String proName;
	/** 零件名称 */
	private String partName;
	/** 单位 */
	private String unit;
	/** 预算数量 */
	private String preNum;
	/** 预算单价 */
	private String preUnitPrice;
	/** 预算金额 */
	private String prePrice;
	/** 实际数量 */
	private String realNum;
	/** 实际单价 */
	private String realUnitPrice;
	/** 实际金额 */
	private String realPrice;
	/** 备注 */
	private String note;

	/**
	 * @return 车辆维护序号
	 */
	public String getWhId() {
		return whId;
	}

	/**
	 * @param 车辆维护序号
	 *            设置车辆维护序号
	 */
	public void setWhId(String whId) {
		this.whId = whId;
	}

	/**
	 * @return 项目名称
	 */
	public String getProName() {
		return proName;
	}

	/**
	 * @param 项目名称
	 *            设置项目名称
	 */
	public void setProName(String proName) {
		this.proName = proName;
	}

	/**
	 * @return 零件名称
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * @param 零件名称
	 *            设置零件名称
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/**
	 * @return 单位
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param 单位
	 *            设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return 实际数量
	 */
	public String getRealNum() {
		return realNum;
	}

	/**
	 * @param 实际数量
	 *            设置实际数量
	 */
	public void setRealNum(String realNum) {
		this.realNum = realNum;
	}

	/**
	 * @return 实际单价
	 */
	public String getRealUnitPrice() {
		return realUnitPrice;
	}

	/**
	 * @param 实际单价
	 *            设置实际单价
	 */
	public void setRealUnitPrice(String realUnitPrice) {
		this.realUnitPrice = realUnitPrice;
	}

	/**
	 * @return 备注
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param 备注
	 *            设置备注
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return 预算数量
	 */
	public String getPreNum() {
		return preNum;
	}

	/**
	 * @param 预算数量
	 *            设置预算数量
	 */
	public void setPreNum(String preNum) {
		this.preNum = preNum;
	}

	/**
	 * @return 预算单价
	 */
	public String getPreUnitPrice() {
		return preUnitPrice;
	}

	/**
	 * @param 预算单价
	 *            设置预算单价
	 */
	public void setPreUnitPrice(String preUnitPrice) {
		this.preUnitPrice = preUnitPrice;
	}

	/**
	 * @return 预算金额
	 */
	public String getPrePrice() {
		return prePrice;
	}

	/**
	 * @param 预算金额
	 *            设置预算金额
	 */
	public void setPrePrice(String prePrice) {
		this.prePrice = prePrice;
	}

	/**
	 * @return 实际金额
	 */
	public String getRealPrice() {
		return realPrice;
	}

	/**
	 * @param 实际金额
	 *            设置实际金额
	 */
	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}
}
