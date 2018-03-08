/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;


public class StationBean implements java.io.Serializable{
    /** 岗位ID **/
	private Long stationId;
	/** 岗位编码 **/
	private String stationCode;
	/** 岗位名称 **/
	private String stationName;
	/** 岗位级别ID **/
	private Long stationLevelId;
	/** 岗位级别名称 **/
	private String stationLevelName;
	/** 岗位类别ID **/
	private Long stationTypeId;
	/** 岗位类别名称 **/
	private String stationTypeName;
	/** 工作类别 **/
	private String workKind;
	/** 岗薪标准 **/
	private Long stationSalary;
	/** 是否常设 **/
	private String ifAlways;
	/** 检索码 **/
	private String retrieveCode;
	/** 创建者 **/
	private String chsName;
	/** 创建日期 **/
	private String insertdate;
	/** 岗位说明 **/
	private String stationNote;
	/** 岗位职责 **/
	private String stationDuty;
	/** 资质要求 **/
	private String aptitude;
	/** 备注 **/
	private String memo;
	/** 导入标志位 **/
	private String importflag;
	
	
	public String getImportflag() {
		return importflag;
	}
	public void setImportflag(String importflag) {
		this.importflag = importflag;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
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
	public Long getStationTypeId() {
		return stationTypeId;
	}
	public void setStationTypeId(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}
	public String getStationTypeName() {
		return stationTypeName;
	}
	public void setStationTypeName(String stationTypeName) {
		this.stationTypeName = stationTypeName;
	}
	public String getWorkKind() {
		return workKind;
	}
	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}
	public Long getStationSalary() {
		return stationSalary;
	}
	public void setStationSalary(Long stationSalary) {
		this.stationSalary = stationSalary;
	}
	public String getIfAlways() {
		return ifAlways;
	}
	public void setIfAlways(String ifAlways) {
		this.ifAlways = ifAlways;
	}
	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public String getInsertdate() {
		return insertdate;
	}
	public void setInsertdate(String insertdate) {
		this.insertdate = insertdate;
	}
	public String getStationNote() {
		return stationNote;
	}
	public void setStationNote(String stationNote) {
		this.stationNote = stationNote;
	}
	public String getStationDuty() {
		return stationDuty;
	}
	public void setStationDuty(String stationDuty) {
		this.stationDuty = stationDuty;
	}
	public String getAptitude() {
		return aptitude;
	}
	public void setAptitude(String aptitude) {
		this.aptitude = aptitude;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
