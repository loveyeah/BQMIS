/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.administration.form;

import java.io.Serializable;


/**
 * 车辆使用申请bean
 * @author Li Chensheng 
 *
 */

public class CarUseApplyInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	// Fields

	private Long id;
	private String applyMan;
	private String useDate;
	private Long useNum;
	private String ifOut;
	private String reason;
	private String aim;
	private Double useDays;
	private String updateTime;
    private String name;
    private String depName;

	// Constructors

	/** default constructor */
	public CarUseApplyInfo() {
	}

	/** minimal constructor */
	public CarUseApplyInfo(Long id) {
		this.id = id;
	}
    // 构造函数 
	public CarUseApplyInfo(Long id, String applyMan, String useDate,
			Long useNum, String ifOut, String reason, String aim,
			Double useDays, String updateTime, String name, String depName) {
		this.id = id;
		this.applyMan = applyMan;
		this.useDate = useDate;
		this.useNum = useNum;
		this.ifOut = ifOut;
		this.reason = reason;
		this.aim = aim;
		this.useDays = useDays;
		this.updateTime = updateTime;
		this.name = name;
		this.depName = depName;
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
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param depName the depName to set
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}


}
