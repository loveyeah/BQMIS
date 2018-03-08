/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.administration.form;

import java.io.Serializable;

public class CarApplyInfo implements Serializable{
	// Fields

	private Long id;
	private String reqName;
	private String deptName;
	private String aim;
	private String driverName;
	private String driverCode;
	private String useDate;
	private String startTime;
	private String endTime;
	private Long useNum;
	private String ifOut;
	private Double useDays;
	private String reason;
	private String goMile;
	private Long  carId;
	private String  carNo;
	private String comeMile;
	private String   useOil;
	private String lqPay;
	private Long updateTime;
	private Long carFileUpdateTime;
	private String distance;
	private String carApplyId;
	private String isUse;



	/**
	 * @return the carApplyId
	 */
	public String getCarApplyId() {
		return carApplyId;
	}
	/**
	 * @param carApplyId the carApplyId to set
	 */
	public void setCarApplyId(String carApplyId) {
		this.carApplyId = carApplyId;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the reqName
	 */
	public String getReqName() {
		return reqName;
	}
	/**
	 * @param reqName the reqName to set
	 */
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}
	/**
	 * @param driverName the driverName to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	/**
	 * @return the driverCode
	 */
	public String getDriverCode() {
		return driverCode;
	}
	/**
	 * @param driverCode the driverCode to set
	 */
	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
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
	 * @return the useNum
	 */
	public Long getUseNum() {
		return useNum;
	}
	/**
	 * @param useNum the useNum to set
	 */
	public void setUseNum(Long useNum) {
		this.useNum = useNum;
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
	public Double getUseDays() {
		return useDays;
	}
	/**
	 * @param useDays the useDays to set
	 */
	public void setUseDays(Double useDays) {
		this.useDays = useDays;
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
	 * @return the carId
	 */
	public Long getCarId() {
		return carId;
	}
	/**
	 * @param carId the carId to set
	 */
	public void setCarId(Long carId) {
		this.carId = carId;
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
	 * @return the updateTime
	 */
	public Long getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the carFileUpdateTime
	 */
	public Long getCarFileUpdateTime() {
		return carFileUpdateTime;
	}
	/**
	 * @param carFileUpdateTime the carFileUpdateTime to set
	 */
	public void setCarFileUpdateTime(Long carFileUpdateTime) {
		this.carFileUpdateTime = carFileUpdateTime;
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
	//是否使用标志
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public CarApplyInfo(){
		
	};
	public CarApplyInfo(Long id, String reqName, String deptName, String aim,
			String driverName,String driverCode, String useDate, String startTime,
			String endTime, Long useNum, String ifOut, Double useDays,
			String reason, String goMile,Long carId, String carNo, String comeMile,
			String useOil, String lqPay, Long updateTime,Long carFileUpdateTime,
			String distance,String carApplyId,String isUse) {
		this.id = id;
		this.reqName = reqName;
		this.deptName = deptName;
		this.aim = aim;
		this.driverCode = driverCode;
		this.driverName = driverName;
		this.useDate = useDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.useNum = useNum;
		this.ifOut = ifOut;
		this.useDays = useDays;
		this.reason = reason;
		this.goMile = goMile;
		this.carId = carId;
		this.carNo = carNo;
		this.comeMile = comeMile;
		this.useOil = useOil;
		this.lqPay = lqPay;
		this.updateTime = updateTime;
		this.carFileUpdateTime = carFileUpdateTime;
		this.distance = distance;
		this.carApplyId = carApplyId;
		this.isUse = isUse;
	}

}
