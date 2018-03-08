package power.ejb.hr;

import java.util.Date;

public class DeptPostAssignmentMaintenInfo implements java.io.Serializable{
	/** 部门岗位ID */
	private Long depstationcorrespondid;
	/** 部门ID */
	private Long deptId;
	/** 标准人数 */
	private Long standardPersonNum;
	/** 上次修改日期 */
	private String lastModifiedDate;
	/** 岗位ID */
	private Long stationId;
	/** 是否领导岗位 */
	private String isLead;
	/** 用于判断这条记录是否删除、新规、修改 */
	private String flag;
	/** 岗位名称 */
	private String stationName;
	/** 岗位编码 */
	private String stationCode;
	
	public Long getDepstationcorrespondid() {
		return depstationcorrespondid;
	}
	public void setDepstationcorrespondid(Long depstationcorrespondid) {
		this.depstationcorrespondid = depstationcorrespondid;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getStandardPersonNum() {
		return standardPersonNum;
	}
	public void setStandardPersonNum(Long standardPersonNum) {
		this.standardPersonNum = standardPersonNum;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getIsLead() {
		return isLead;
	}
	public void setIsLead(String isLead) {
		this.isLead = isLead;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	
	
	
	
}
