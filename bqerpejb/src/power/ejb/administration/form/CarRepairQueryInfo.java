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
public class CarRepairQueryInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 车辆维护序号隐藏域 */
	private String hdnWhId;
	/** 车辆维护序号 */
	private String whId;
	/** 单据状态 */
	private String dcmStatus;
	/** 车牌号 */
	private String carNo;
	/** 车名 */
	private String carName;
	/** 维修日期 */
	private String repairDate;
	/** 项目名称 */
	private String proName;
	/** 有无清单 */
	private String haveLise;
	/** 项目代码 */
	private String proCode;
	/** 预算费用 */
	private String price;
	/** 实际费用 */
	private String realPrice;
	/** 实际费用合计 */
	private String realSum;
	/** 单位名称 */
	private String cpName;
	/** 维修里程 */
	private String driveMile;
	/** 经办人 */
	private String chsName;
	/** 支出事由 */
	private String reason;
	/** 备注 */
	private String memo;

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
	 * @return 单据状态
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}

	/**
	 * @param 单据状态
	 *            设置单据状态
	 */
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	/**
	 * @return 车牌号
	 */
	public String getCarNo() {
		return carNo;
	}

	/**
	 * @param 车牌号
	 *            设置车牌号
	 */
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	/**
	 * @return 车名
	 */
	public String getCarName() {
		return carName;
	}

	/**
	 * @param 车名
	 *            设置车名
	 */
	public void setCarName(String carName) {
		this.carName = carName;
	}

	/**
	 * @return 维修日期
	 */
	public String getRepairDate() {
		return repairDate;
	}

	/**
	 * @param 维修日期
	 *            设置维修日期
	 */
	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
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
	 * @return 预算费用
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param 预算费用
	 *            设置预算费用
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return 实际费用
	 */
	public String getRealPrice() {
		return realPrice;
	}

	/**
	 * @param 实际费用
	 *            设置实际费用
	 */
	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	/**
	 * @return 实际费用合计
	 */
	public String getRealSum() {
		return realSum;
	}

	/**
	 * @param 实际费用合计
	 *            设置实际费用合计
	 */
	public void setRealSum(String realSum) {
		this.realSum = realSum;
	}

	/**
	 * @return 维修里程
	 */
	public String getDriveMile() {
		return driveMile;
	}

	/**
	 * @param 维修里程
	 *            设置维修里程
	 */
	public void setDriveMile(String driveMile) {
		this.driveMile = driveMile;
	}

	/**
	 * @return 经办人
	 */
	public String getChsName() {
		return chsName;
	}

	/**
	 * @param 经办人
	 *            设置经办人
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}

	/**
	 * @return 支出事由
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param 支出事由
	 *            设置支出事由
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return 有无清单
	 */
	public String getHaveLise() {
		return haveLise;
	}

	/**
	 * @param 有无清单
	 *            设置有无清单
	 */
	public void setHaveLise(String haveLise) {
		this.haveLise = haveLise;
	}

	/**
	 * @return 项目代码
	 */
	public String getProCode() {
		return proCode;
	}

	/**
	 * @param 项目代码
	 *            设置项目代码
	 */
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param 备注
	 *            设置备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return 隐藏车辆维护序号
	 */
	public String getHdnWhId() {
		return hdnWhId;
	}

	/**
	 * @param 隐藏车辆维护序号
	 *            设置隐藏车辆维护序号
	 */
	public void setHdnWhId(String hdnWhId) {
		this.hdnWhId = hdnWhId;
	}

	/**
	 * @return 单位名称
	 */
	public String getCpName() {
		return cpName;
	}

	/**
	 * @param 单位名称
	 *            设置单位名称
	 */
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
}
