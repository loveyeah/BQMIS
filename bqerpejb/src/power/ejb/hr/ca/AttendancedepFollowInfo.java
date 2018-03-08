/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 考勤部门及其附带信息bean
 * 
 * @author chen shoujiang
 */
public class AttendancedepFollowInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String attendanceDeptId;
	private String attendanceDeptName;
	private String attendDepType;
	private String topCheckDepId;
	private String topCheckDepName;
	private String replaceDepId;
	private String replaceDepName;
	private String attendWriterId;
	private String attendWriterName;
	private String attendCheckerId;
	private String attendCheckerName;
	private String lastModifyDate;
	
	/**
	 * @return the lastModifyDate
	 */
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	/**
	 * @param lastModifyDate the lastModifyDate to set
	 */
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	/**
	 * @return the attendanceDeptId
	 */
	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}
	/**
	 * @param attendanceDeptId the attendanceDeptId to set
	 */
	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}
	/**
	 * @return the attendanceDeptName
	 */
	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}
	/**
	 * @param attendanceDeptName the attendanceDeptName to set
	 */
	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}
	/**
	 * @return the attendDepType
	 */
	public String getAttendDepType() {
		return attendDepType;
	}
	/**
	 * @param attendDepType the attendDepType to set
	 */
	public void setAttendDepType(String attendDepType) {
		this.attendDepType = attendDepType;
	}
	/**
	 * @return the topCheckDepId
	 */
	public String getTopCheckDepId() {
		return topCheckDepId;
	}
	/**
	 * @param topCheckDepId the topCheckDepId to set
	 */
	public void setTopCheckDepId(String topCheckDepId) {
		this.topCheckDepId = topCheckDepId;
	}
	/**
	 * @return the topCheckDepName
	 */
	public String getTopCheckDepName() {
		return topCheckDepName;
	}
	/**
	 * @param topCheckDepName the topCheckDepName to set
	 */
	public void setTopCheckDepName(String topCheckDepName) {
		this.topCheckDepName = topCheckDepName;
	}
	/**
	 * @return the replaceDepId
	 */
	public String getReplaceDepId() {
		return replaceDepId;
	}
	/**
	 * @param replaceDepId the replaceDepId to set
	 */
	public void setReplaceDepId(String replaceDepId) {
		this.replaceDepId = replaceDepId;
	}
	/**
	 * @return the replaceDepName
	 */
	public String getReplaceDepName() {
		return replaceDepName;
	}
	/**
	 * @param replaceDepName the replaceDepName to set
	 */
	public void setReplaceDepName(String replaceDepName) {
		this.replaceDepName = replaceDepName;
	}
	/**
	 * @return the attendWriterId
	 */
	public String getAttendWriterId() {
		return attendWriterId;
	}
	/**
	 * @param attendWriterId the attendWriterId to set
	 */
	public void setAttendWriterId(String attendWriterId) {
		this.attendWriterId = attendWriterId;
	}
	/**
	 * @return the attendWriterName
	 */
	public String getAttendWriterName() {
		return attendWriterName;
	}
	/**
	 * @param attendWriterName the attendWriterName to set
	 */
	public void setAttendWriterName(String attendWriterName) {
		this.attendWriterName = attendWriterName;
	}
	/**
	 * @return the attendCheckerId
	 */
	public String getAttendCheckerId() {
		return attendCheckerId;
	}
	/**
	 * @param attendCheckerId the attendCheckerId to set
	 */
	public void setAttendCheckerId(String attendCheckerId) {
		this.attendCheckerId = attendCheckerId;
	}
	/**
	 * @return the attendCheckerName
	 */
	public String getAttendCheckerName() {
		return attendCheckerName;
	}
	/**
	 * @param attendCheckerName the attendCheckerName to set
	 */
	public void setAttendCheckerName(String attendCheckerName) {
		this.attendCheckerName = attendCheckerName;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
