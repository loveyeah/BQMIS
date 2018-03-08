/**
 * 
 */
package power.web.hr.station;

/**
 * @author ustcpower
 *
 */
public class StationInfo {
	private String stationId;
	private String stationTypeId;
	private String stationCode;
	private String stationName;
	private String stationDuty;
	private String isUse;
	private String memo;
	private String retrieveCode;
//	private Long ordeyBy;
	
	//工作类别 
	//add by drdu 090929
	private String workKind;
	//岗位级别 
	private Long stationLevelId;
	//岗位级别名称
	private String stationLevelName;

	
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationDuty() {
		return stationDuty;
	}
	public void setStationDuty(String stationDuty) {
		this.stationDuty = stationDuty;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	public String getStationTypeId() {
		return stationTypeId;
	}
	public void setStationTypeId(String stationTypeId) {
		this.stationTypeId = stationTypeId;
	}
	public String getWorkKind() {
		return workKind;
	}
	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}
	public Long getStationLevelId() {
		return stationLevelId;
	}
	public void setStationLevelId(Long stationLevelId) {
		this.stationLevelId = stationLevelId;
	}
	public String getStationLevelName() {
		return stationLevelName;
	}
	public void setStationLevelName(String stationLevelName) {
		this.stationLevelName = stationLevelName;
	}
	
}
