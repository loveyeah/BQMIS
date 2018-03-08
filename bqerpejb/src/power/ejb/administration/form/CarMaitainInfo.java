/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.util.ArrayList;
import java.util.List;
// default package

/**
 * 车辆维修申请单 entity.
 * 
 * @author daichunlin
 */
public class CarMaitainInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 车辆维护表 */
	/** 车名 */
	private String carName;
	/** 车牌号 */
	private String carNo;
	/** 维修日期 */
	private String repairDate;
	/** 维修里程 */
	private String driveMile;
	/** 支出事由 */
	private String reason;
	/** 预计费用 */
	private String sum;
	/** 实际费用 */
	private String realSum;	
	/** 备注 */
	private String memo;
	/** 部门经理审核意见 */
	private String depIdea;
	/** 部门经理签名 */
	private String depBossCode;
	/** 部门经理签名图片 */
	private byte[] depBossPicture;
	/** 部门经理签字时间 */
	private String depSignDate;
	/** 行政部经理审核意见 */
	private String viceBossIdea;
	/** 行政部经理签名 */
	private String viceBossCode;
	/** 行政部经理签名图片 */
	private byte[] xzBossPicture;
	/** 行政部经理签字时间 */
	private String viceBossSignDate;	
	/** 总经理审核意见 */
	private String bigBossIdea;
	/** 总经理签名 */
	private String bigBossCode;
	/** 总经理签名图片 */
	private byte[] bigBossPicture;
	/** 总经理签字时间 */
	private String bigBossSignDate;	
	/** 部门经理姓名 */
	private String depBossName;
	/** 行政部经理姓名 */
	private String xzBossName;
	/** 总经理姓名 */
	private String bigBossName;
	/** 部门经理Flag */
	private boolean depBossFlag = true;
	/** 行政部经理Flag */
	private boolean xzBossFlag = true;
	/** 总经理Flag */
	private boolean bigBossFlag = true;
	/** 车辆维护明细表 */
	/** 费用预算 */
	private String price;
	/** 实际费用 */
	private String realPrice;
	/** 车辆维修单位维护表 */
	/** 单位名称 */
	private String cpName;
	/** 车辆维护之维修项目表 */
	/** 项目名称 */
	private String proName;
	/** 人员基本信息表 */
	/** 姓名 */
	private String chsName;
	/** 车辆检修申请单List详细信息 */
	private  List<CarMaitainDetailInfo> carMaitainList = new ArrayList<CarMaitainDetailInfo>();
	/** cntRow */
	private String cntRow ="1";
	
	/**
	 * @return the cntRow
	 */
	public String getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(String cntRow) {
		this.cntRow = cntRow;
	}
	/**
	 * @return the carName
	 */
	public String getCarName() {
		return carName;
	}
	/**
	 * @param carName the carName to set
	 */
	public void setCarName(String carName) {
		this.carName = carName;
	}
	/**
	 * @return the carNo
	 */
	public String getCarNo() {
		return carNo;
	}
	/**
	 * @param carNo the carNo to set
	 */
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	/**
	 * @return the repairDate
	 */
	public String getRepairDate() {
		return repairDate;
	}
	/**
	 * @param repairDate the repairDate to set
	 */
	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
	}
	/**
	 * @return the driveMile
	 */
	public String getDriveMile() {
		return driveMile;
	}
	/**
	 * @param driveMile the driveMile to set
	 */
	public void setDriveMile(String driveMile) {
		this.driveMile = driveMile;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the sum
	 */
	public String getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(String sum) {
		this.sum = sum;
	}
	/**
	 * @return the realSum
	 */
	public String getRealSum() {
		return realSum;
	}
	/**
	 * @param realSum the realSum to set
	 */
	public void setRealSum(String realSum) {
		this.realSum = realSum;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the depIdea
	 */
	public String getDepIdea() {
		return depIdea;
	}
	/**
	 * @param depIdea the depIdea to set
	 */
	public void setDepIdea(String depIdea) {
		this.depIdea = depIdea;
	}
	/**
	 * @return the depBossCode
	 */
	public String getDepBossCode() {
		return depBossCode;
	}
	/**
	 * @param depBossCode the depBossCode to set
	 */
	public void setDepBossCode(String depBossCode) {
		this.depBossCode = depBossCode;
	}
	/**
	 * @return the depSignDate
	 */
	public String getDepSignDate() {
		return depSignDate;
	}
	/**
	 * @param depSignDate the depSignDate to set
	 */
	public void setDepSignDate(String depSignDate) {
		this.depSignDate = depSignDate;
	}
	/**
	 * @return the viceBossIdea
	 */
	public String getViceBossIdea() {
		return viceBossIdea;
	}
	/**
	 * @param viceBossIdea the viceBossIdea to set
	 */
	public void setViceBossIdea(String viceBossIdea) {
		this.viceBossIdea = viceBossIdea;
	}
	/**
	 * @return the viceBossCode
	 */
	public String getViceBossCode() {
		return viceBossCode;
	}
	/**
	 * @param viceBossCode the viceBossCode to set
	 */
	public void setViceBossCode(String viceBossCode) {
		this.viceBossCode = viceBossCode;
	}
	/**
	 * @return the viceBossSignDate
	 */
	public String getViceBossSignDate() {
		return viceBossSignDate;
	}
	/**
	 * @param viceBossSignDate the viceBossSignDate to set
	 */
	public void setViceBossSignDate(String viceBossSignDate) {
		this.viceBossSignDate = viceBossSignDate;
	}
	/**
	 * @return the bigBossIdea
	 */
	public String getBigBossIdea() {
		return bigBossIdea;
	}
	/**
	 * @param bigBossIdea the bigBossIdea to set
	 */
	public void setBigBossIdea(String bigBossIdea) {
		this.bigBossIdea = bigBossIdea;
	}
	/**
	 * @return the bigBossCode
	 */
	public String getBigBossCode() {
		return bigBossCode;
	}
	/**
	 * @param bigBossCode the bigBossCode to set
	 */
	public void setBigBossCode(String bigBossCode) {
		this.bigBossCode = bigBossCode;
	}
	/**
	 * @return the bigBossSignDate
	 */
	public String getBigBossSignDate() {
		return bigBossSignDate;
	}
	/**
	 * @param bigBossSignDate the bigBossSignDate to set
	 */
	public void setBigBossSignDate(String bigBossSignDate) {
		this.bigBossSignDate = bigBossSignDate;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the realPrice
	 */
	public String getRealPrice() {
		return realPrice;
	}
	/**
	 * @param realPrice the realPrice to set
	 */
	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}
	/**
	 * @return the cpName
	 */
	public String getCpName() {
		return cpName;
	}
	/**
	 * @param cpName the cpName to set
	 */
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	/**
	 * @return the proName
	 */
	public String getProName() {
		return proName;
	}
	/**
	 * @param proName the proName to set
	 */
	public void setProName(String proName) {
		this.proName = proName;
	}
	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}
	/**
	 * @param chsName the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	/**
	 * @return the depBossPicture
	 */
	public byte[] getDepBossPicture() {
		return depBossPicture;
	}
	/**
	 * @param depBossPicture the depBossPicture to set
	 */
	public void setDepBossPicture(byte[] depBossPicture) {
		this.depBossPicture = depBossPicture;
	}
	/**
	 * @return the xzBossPicture
	 */
	public byte[] getXzBossPicture() {
		return xzBossPicture;
	}
	/**
	 * @param xzBossPicture the xzBossPicture to set
	 */
	public void setXzBossPicture(byte[] xzBossPicture) {
		this.xzBossPicture = xzBossPicture;
	}
	/**
	 * @return the bigBossPicture
	 */
	public byte[] getBigBossPicture() {
		return bigBossPicture;
	}
	/**
	 * @param bigBossPicture the bigBossPicture to set
	 */
	public void setBigBossPicture(byte[] bigBossPicture) {
		this.bigBossPicture = bigBossPicture;
	}
	/**
	 * @return the carMaitainList
	 */
	public List<CarMaitainDetailInfo> getCarMaitainList() {
		return carMaitainList;
	}
	/**
	 * @param carMaitainList the carMaitainList to set
	 */
	public void setCarMaitainList(List<CarMaitainDetailInfo> carMaitainList) {
		this.carMaitainList = carMaitainList;
	}
	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	/**
	 * @return the depBossName
	 */
	public String getDepBossName() {
		return depBossName;
	}
	/**
	 * @param depBossName the depBossName to set
	 */
	public void setDepBossName(String depBossName) {
		this.depBossName = depBossName;
	}
	/**
	 * @return the xzBossName
	 */
	public String getXzBossName() {
		return xzBossName;
	}
	/**
	 * @param xzBossName the xzBossName to set
	 */
	public void setXzBossName(String xzBossName) {
		this.xzBossName = xzBossName;
	}
	/**
	 * @return the bigBossName
	 */
	public String getBigBossName() {
		return bigBossName;
	}
	/**
	 * @param bigBossName the bigBossName to set
	 */
	public void setBigBossName(String bigBossName) {
		this.bigBossName = bigBossName;
	}
	/**
	 * @return the depBossFlag
	 */
	public boolean isDepBossFlag() {
		return depBossFlag;
	}
	/**
	 * @param depBossFlag the depBossFlag to set
	 */
	public void setDepBossFlag(boolean depBossFlag) {
		this.depBossFlag = depBossFlag;
	}
	/**
	 * @return the xzBossFlag
	 */
	public boolean isXzBossFlag() {
		return xzBossFlag;
	}
	/**
	 * @param xzBossFlag the xzBossFlag to set
	 */
	public void setXzBossFlag(boolean xzBossFlag) {
		this.xzBossFlag = xzBossFlag;
	}
	/**
	 * @return the bigBossFlag
	 */
	public boolean isBigBossFlag() {
		return bigBossFlag;
	}
	/**
	 * @param bigBossFlag the bigBossFlag to set
	 */
	public void setBigBossFlag(boolean bigBossFlag) {
		this.bigBossFlag = bigBossFlag;
	}
	
}