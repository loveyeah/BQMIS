/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * @author chaihao
 * 
 */
public class CarFileMaintenInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 序号 */
	private String id;
	/** 车号 */
	private String carNo;
	/** 车名 */
	private String carName;
	/** 车种 */
	private String carKind;
	/** 车型 */
	private String carType;
	/** 车架 */
	private String carFrame;
	/** 发动机号码 */
	private String engineNo;
	/** 行驶里程 */
	private String runMile;
	/** 载人数 */
	private String loadman;
	/** 载重 */
	private String weight;
	/** 特殊设备 */
	private String equip;
	/** 驾驶员 */
	private String name;
	/** 人员编码 */
	private String empCode;
	/** 行驶证 */
	private String runLic;
	/** 通行证 */
	private String runAllLic;
	/** 购买日期 */
	private String buyDate;
	/** 购买金额 */
	private String price;
	/** 销售商家 */
	private String carshop;
	/** 发票号 */
	private String invoiceNo;
	/** 耗油率 */
	private String oilRate;
	/** 保险费 */
	private String isurance;
	/** 部门名称 */
	private String deptName;
	/** 使用情况 */
	private String useStatus;
	/** 部门编码 */
	private String dep;

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
	 * @return 车号
	 */
	public String getCarNo() {
		return carNo;
	}

	/**
	 * @param 车号
	 *            设置车号
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
	 * @return 车种
	 */
	public String getCarKind() {
		return carKind;
	}

	/**
	 * @param 车种
	 *            设置车种
	 */
	public void setCarKind(String carKind) {
		this.carKind = carKind;
	}

	/**
	 * @return 车型
	 */
	public String getCarType() {
		return carType;
	}

	/**
	 * @param 车型
	 *            设置车型
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}

	/**
	 * @return 车架
	 */
	public String getCarFrame() {
		return carFrame;
	}

	/**
	 * @param 车架
	 *            设置车架
	 */
	public void setCarFrame(String carFrame) {
		this.carFrame = carFrame;
	}

	/**
	 * @return 发动机号码
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param 发动机号码
	 *            设置发动机号码
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return 行驶里程
	 */
	public String getRunMile() {
		return runMile;
	}

	/**
	 * @param 行驶里程
	 *            设置行驶里程
	 */
	public void setRunMile(String runMile) {
		this.runMile = runMile;
	}

	/**
	 * @return 载人数
	 */
	public String getLoadman() {
		return loadman;
	}

	/**
	 * @param 载人数
	 *            设置载人数
	 */
	public void setLoadman(String loadman) {
		this.loadman = loadman;
	}

	/**
	 * @return 载重
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param 载重
	 *            设置载重
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * @return 特殊设备
	 */
	public String getEquip() {
		return equip;
	}

	/**
	 * @param 特殊设备
	 *            设置特殊设备
	 */
	public void setEquip(String equip) {
		this.equip = equip;
	}

	/**
	 * @return 驾驶员
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param 驾驶员
	 *            设置驾驶员
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 行驶证
	 */
	public String getRunLic() {
		return runLic;
	}

	/**
	 * @param 行驶证
	 *            设置行驶证
	 */
	public void setRunLic(String runLic) {
		this.runLic = runLic;
	}

	/**
	 * @return 通行证
	 */
	public String getRunAllLic() {
		return runAllLic;
	}

	/**
	 * @param 通行证
	 *            设置通行证
	 */
	public void setRunAllLic(String runAllLic) {
		this.runAllLic = runAllLic;
	}

	/**
	 * @return 购买日期
	 */
	public String getBuyDate() {
		return buyDate;
	}

	/**
	 * @param 购买日期
	 *            设置购买日期
	 */
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	/**
	 * @return 购买金额
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param 购买金额
	 *            设置购买金额
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return 销售商家
	 */
	public String getCarshop() {
		return carshop;
	}

	/**
	 * @param 销售商家
	 *            设置销售商家
	 */
	public void setCarshop(String carshop) {
		this.carshop = carshop;
	}

	/**
	 * @return 发票号
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/**
	 * @param 发票号
	 *            设置发票号
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/**
	 * @return 耗油率
	 */
	public String getOilRate() {
		return oilRate;
	}

	/**
	 * @param 耗油率
	 *            设置耗油率
	 */
	public void setOilRate(String oilRate) {
		this.oilRate = oilRate;
	}

	/**
	 * @return 保险费
	 */
	public String getIsurance() {
		return isurance;
	}

	/**
	 * @param 保险费
	 *            设置保险费
	 */
	public void setIsurance(String isurance) {
		this.isurance = isurance;
	}

	/**
	 * @return 部门名称
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param 部门名称
	 *            设置部门名称
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return 使用情况
	 */
	public String getUseStatus() {
		return useStatus;
	}

	/**
	 * @param 使用情况
	 *            设置使用情况
	 */
	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	/**
	 * @return 人员编码
	 */
	public String getEmpCode() {
		return empCode;
	}

	/**
	 * @param 人员编码
	 *            设置人员编码
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * @return 部门编码
	 */
	public String getDep() {
		return dep;
	}

	/**
	 * @param 部门编码
	 *            设置部门编码
	 */
	public void setDep(String dep) {
		this.dep = dep;
	}
}
