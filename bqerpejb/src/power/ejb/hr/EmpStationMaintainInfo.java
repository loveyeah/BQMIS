/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 员工岗位维护 entity.
 * 
 * @author zhaozhijie
 */
public class EmpStationMaintainInfo  implements java.io.Serializable {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 岗位id */
	private String stationId;
	/** 岗位名称 */
	private String stationName;
	/** 岗位级别 */
	private String stationLevel;
	/** 主岗位 */
	private String mainStation;
	/** 备注 */
	private String memo;
	/** 职工岗位id */
	private String empStationId;
	/** 上次修改时间 */
	private String lastModifyDate;
	/** 判断DB中是否存在数据的flag */
	private String dbFlag = "false";
	/** 判断更新,插入,删除的flag */
	private String existFlag = "";

	/**
	 * @return 判断DB中是否存在数据的flag
	 */
	public String getDbFlag() {
		return dbFlag;
	}
	/**
	 * @param 判断DB中是否存在数据的flag
	 */
	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}
	/**
	 * @return 判断更新,插入,删除的flag 
	 */
	public String getExistFlag() {
		return existFlag;
	}
	/**
	 * @param 判断更新,插入,删除的flag 
	 */
	public void setExistFlag(String existFlag) {
		this.existFlag = existFlag;
	}
	/**
	 * @return 上次修改时间
	 */
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	/**
	 * @param 上次修改时间
	 */
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	/**
	 * @return 岗位id
	 */
	public String getStationId() {
		return stationId;
	}
	/**
	 * @param 岗位id
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	/**
	 * @return 岗位名称
	 */
	public String getStationName() {
		return stationName;
	}
	/**
	 * @param 岗位名称
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	/**
	 * @return 岗位级别
	 */
	public String getStationLevel() {
		return stationLevel;
	}
	/**
	 * @param 岗位级别
	 */
	public void setStationLevel(String stationLevel) {
		this.stationLevel = stationLevel;
	}
	/**
	 * @return 主岗位
	 */
	public String getMainStation() {
		return mainStation;
	}
	/**
	 * @param 主岗位
	 */
	public void setMainStation(String mainStation) {
		this.mainStation = mainStation;
	}
	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return 职工岗位id 
	 */
	public String getEmpStationId() {
		return empStationId;
	}
	/**
	 * @param 职工岗位id 
	 */
	public void setEmpStationId(String empStationId) {
		this.empStationId = empStationId;
	}

}
