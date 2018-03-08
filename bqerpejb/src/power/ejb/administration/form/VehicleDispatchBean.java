/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.util.Date;

/**
* 派车单Bean
* @author zhaozhijie
*
*/
public class VehicleDispatchBean {

	/** 用车部门 */
	private String dep;
	/** 申请人 */
	private String applyMan;
	/** 用车人数 */
	private String useNum;
	/** 用车事由 */
	private String reason;
	/** 到达地点 */
	private String aim;
	/** 用车时间 */
	private String useDate;
	/** 是否出省 */
	private String ifOut;
	/** 用车天数 */
	private String useDays;
	/** 发车时间 */
	private String startTime;
	/** 收车时间 */
	private String endTime;
	/** 部门经理审核意见 */
	private String depIDEA;
	/** 部门经理签名Code */
	private String depBossCode = "";
	/** 部门经理签名内容 */
	private Boolean depBossFlag;
	/** 部门经理中文名 */
	private String depBossName;
	/** 部门经理签名图片 */
	private byte[] depBossPicture;
	/** 部门经理签字时间 */
	private String depSignDate;
	/** 行政部经理审核意见 */
	private String xzBossIDEA;
	/** 行政部经理签名Code */
	private String xzBossCode = "";
	/** 行政部经理签名内容 */
	private Boolean xzBossFlag;
	/** 行政部经理签名中文名 */
	private String xzBossName;
	/** 行政部经理签名图片 */
	private byte[] xzBossPicture;
	/** 行政部经理签字时间 */
	private String xzSignDate;
	/** 总经理审核意见 */
	private String bigBossIDEA;
	/** 总经理签名Code */
	private String bigBossCode = "";
	/** 总经理签名内容 */
	private Boolean bigBossFlag;
	/** 总经理签名中文名 */
	private String bigBossName;
	/** 总经理签名图片 */
	private byte[] bigBossPicture;
	/** 总经理签字时间 */
	private String bigBossSignDate;
	/** 发车里程 */
	private String goMile;
	/** 收车里程 */
	private String comeMile;
	/** 行车里程 */
	private String distance;
	/** 车牌号 */
	private String carNo;
	/** 司机 */
	private String driver;
	/** 路桥费 */
	private String lqPay;
	/** 油费 */
	private String useOil;
	/** 上报状态 */
	private String dcmStatus;

	/**
	 * @return the dep
	 */
	public String getDep() {
		return dep;
	}
	/**
	 * @param dep the dep to set
	 */
	public void setDep(String dep) {
		this.dep = dep;
	}
	/**
	 * @return the applyMan
	 */
	public String getApplyMan() {
		return applyMan;
	}
	/**
	 * @param applyMan the applyMan to set
	 */
	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}
	/**
	 * @return the useNum
	 */
	public String getUseNum() {
		return useNum;
	}
	/**
	 * @param useNum the useNum to set
	 */
	public void setUseNum(String useNum) {
		this.useNum = useNum;
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
	 * @return the aim
	 */
	public String getAim() {
		return aim;
	}
	/**
	 * @param aim the aim to set
	 */
	public void setAim(String aim) {
		this.aim = aim;
	}
	/**
	 * @return the useDate
	 */
	public String getUseDate() {
		return useDate;
	}
	/**
	 * @param useDate the useDate to set
	 */
	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}
	/**
	 * @return the ifOut
	 */
	public String getIfOut() {
		return ifOut;
	}
	/**
	 * @param ifOut the ifOut to set
	 */
	public void setIfOut(String ifOut) {
		this.ifOut = ifOut;
	}
	/**
	 * @return the useDays
	 */
	public String getUseDays() {
		return useDays;
	}
	/**
	 * @param useDays the useDays to set
	 */
	public void setUseDays(String useDays) {
		this.useDays = useDays;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the depIDEA
	 */
	public String getDepIDEA() {
		return depIDEA;
	}
	/**
	 * @param depIDEA the depIDEA to set
	 */
	public void setDepIDEA(String depIDEA) {
		this.depIDEA = depIDEA;
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
	 * @return the depBossFlag
	 */
	public Boolean getDepBossFlag() {
		return depBossFlag;
	}
	/**
	 * @param depBossFlag the depBossFlag to set
	 */
	public void setDepBossFlag(Boolean depBossFlag) {
		this.depBossFlag = depBossFlag;
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
	 * @return the xzBossIDEA
	 */
	public String getXzBossIDEA() {
		return xzBossIDEA;
	}
	/**
	 * @param xzBossIDEA the xzBossIDEA to set
	 */
	public void setXzBossIDEA(String xzBossIDEA) {
		this.xzBossIDEA = xzBossIDEA;
	}
	/**
	 * @return the xzBossCode
	 */
	public String getXzBossCode() {
		return xzBossCode;
	}
	/**
	 * @param xzBossCode the xzBossCode to set
	 */
	public void setXzBossCode(String xzBossCode) {
		this.xzBossCode = xzBossCode;
	}
	/**
	 * @return the xzBossFlag
	 */
	public Boolean getXzBossFlag() {
		return xzBossFlag;
	}
	/**
	 * @param xzBossFlag the xzBossFlag to set
	 */
	public void setXzBossFlag(Boolean xzBossFlag) {
		this.xzBossFlag = xzBossFlag;
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
	 * @return the xzSignDate
	 */
	public String getXzSignDate() {
		return xzSignDate;
	}
	/**
	 * @param xzSignDate the xzSignDate to set
	 */
	public void setXzSignDate(String xzSignDate) {
		this.xzSignDate = xzSignDate;
	}
	/**
	 * @return the bigBossIDEA
	 */
	public String getBigBossIDEA() {
		return bigBossIDEA;
	}
	/**
	 * @param bigBossIDEA the bigBossIDEA to set
	 */
	public void setBigBossIDEA(String bigBossIDEA) {
		this.bigBossIDEA = bigBossIDEA;
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
	 * @return the bigBossFlag
	 */
	public Boolean getBigBossFlag() {
		return bigBossFlag;
	}
	/**
	 * @param bigBossFlag the bigBossFlag to set
	 */
	public void setBigBossFlag(Boolean bigBossFlag) {
		this.bigBossFlag = bigBossFlag;
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
	 * @return the goMile
	 */
	public String getGoMile() {
		return goMile;
	}
	/**
	 * @param goMile the goMile to set
	 */
	public void setGoMile(String goMile) {
		this.goMile = goMile;
	}
	/**
	 * @return the comeMile
	 */
	public String getComeMile() {
		return comeMile;
	}
	/**
	 * @param comeMile the comeMile to set
	 */
	public void setComeMile(String comeMile) {
		this.comeMile = comeMile;
	}
	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
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
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}
	/**
	 * @return the lqPay
	 */
	public String getLqPay() {
		return lqPay;
	}
	/**
	 * @param lqPay the lqPay to set
	 */
	public void setLqPay(String lqPay) {
		this.lqPay = lqPay;
	}
	/**
	 * @return the useOil
	 */
	public String getUseOil() {
		return useOil;
	}
	/**
	 * @param useOil the useOil to set
	 */
	public void setUseOil(String useOil) {
		this.useOil = useOil;
	}
	/**
	 * @return the dcmStatus
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}
	/**
	 * @param dcmStatus the dcmStatus to set
	 */
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	
}
