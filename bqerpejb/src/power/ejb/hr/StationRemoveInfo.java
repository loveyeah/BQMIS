/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

/**
 * 班组人员调动bean
 * @author zhengzhipeng
 * 
 */
public class StationRemoveInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	    // Fields
            /** 岗位调动单ID */
	    private String stationremoveid;
	    /** 岗位调动类别 */
	    private String stationMoveTypeName;
	    /** 调动日期 */
	    private String removeDate;
	    /** 调动前部门名称 */
	    private String oldDepName;
	    /** 调动前部门Id */
	    private String oldDepId;
	    /** 调动后部门名称 */
	    private String newDepName;
	    /** 调动后部门Id */
	    private String newDepId;
	    /** 执行日期 */
	    private String doDate;
	    /** 备注 */
	    private String memo;
	    /** 上次修改日期 */
	    private String lastModifiedDate;
	    /** 人员ID */
	    private String empId;
	    /** 人员姓名 */
	    private String empName;
	    /** 人员编码(工号) */
	    private String empCode;
	    /**
	     * @return the empCode
	     */
	    public String getEmpCode() {
	        return empCode;
	    }
	    /**
	     * @param empCode the empCode to set
	     */
	    public void setEmpCode(String empCode) {
	        this.empCode = empCode;
	    }
	    /**
	     * @return the stationremoveid
	     */
	    public String getStationremoveid() {
	        return stationremoveid;
	    }
	    /**
	     * @param stationremoveid the stationremoveid to set
	     */
	    public void setStationremoveid(String stationremoveid) {
	        this.stationremoveid = stationremoveid;
	    }
	    /**
	     * @return the stationMoveTypeName
	     */
	    public String getStationMoveTypeName() {
	        return stationMoveTypeName;
	    }
	    /**
	     * @param stationMoveTypeName the stationMoveTypeName to set
	     */
	    public void setStationMoveTypeName(String stationMoveTypeName) {
	        this.stationMoveTypeName = stationMoveTypeName;
	    }
	    /**
	     * @return the removeDate
	     */
	    public String getRemoveDate() {
	        return removeDate;
	    }
	    /**
	     * @param removeDate the removeDate to set
	     */
	    public void setRemoveDate(String removeDate) {
	        this.removeDate = removeDate;
	    }
	    /**
	     * @return the oldDepName
	     */
	    public String getOldDepName() {
	        return oldDepName;
	    }
	    /**
	     * @param oldDepName the oldDepName to set
	     */
	    public void setOldDepName(String oldDepName) {
	        this.oldDepName = oldDepName;
	    }
	    /**
	     * @return the newDepName
	     */
	    public String getNewDepName() {
	        return newDepName;
	    }
	    /**
	     * @param newDepName the newDepName to set
	     */
	    public void setNewDepName(String newDepName) {
	        this.newDepName = newDepName;
	    }
	    /**
	     * @return the doDate
	     */
	    public String getDoDate() {
	        return doDate;
	    }
	    /**
	     * @param doDate the doDate to set
	     */
	    public void setDoDate(String doDate) {
	        this.doDate = doDate;
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
	     * @return the lastModifiedDate
	     */
	    public String getLastModifiedDate() {
	        return lastModifiedDate;
	    }
	    /**
	     * @param lastModifiedDate the lastModifiedDate to set
	     */
	    public void setLastModifiedDate(String lastModifiedDate) {
	        this.lastModifiedDate = lastModifiedDate;
	    }
	    /**
	     * @return the empId
	     */
	    public String getEmpId() {
	        return empId;
	    }
	    /**
	     * @param empId the empId to set
	     */
	    public void setEmpId(String empId) {
	        this.empId = empId;
	    }
	    /**
	     * @return the empName
	     */
	    public String getEmpName() {
	        return empName;
	    }
	    /**
	     * @param empName the empName to set
	     */
	    public void setEmpName(String empName) {
	        this.empName = empName;
	    }
	    /**
	     * @return the oldDepId
	     */
	    public String getOldDepId() {
	        return oldDepId;
	    }
	    /**
	     * @param oldDepId the oldDepId to set
	     */
	    public void setOldDepId(String oldDepId) {
	        this.oldDepId = oldDepId;
	    }
	    /**
	     * @return the newDepId
	     */
	    public String getNewDepId() {
	        return newDepId;
	    }
	    /**
	     * @param newDepId the newDepId to set
	     */
	    public void setNewDepId(String newDepId) {
	        this.newDepId = newDepId;
	    }
}
