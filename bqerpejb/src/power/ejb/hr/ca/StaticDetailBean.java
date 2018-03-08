/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.hr.ca;

/**
 * (请假、加班、运行班)统计报表详细信息
 * @author zhujie 
 */
public class StaticDetailBean implements java.io.Serializable{
	/** 员工ID */
    private String empId="";
    /** 员工姓名 */
    private String chsName="";
    /** 上级部门ID */
    private String pdeptId="";
    /** 上级部门名称 */
    private String pdeptName="";
    /** 部门ID */
    private String deptId="";
    /** 部门名称 */
    private String deptName="";
    /** 列类别ID */
    private String vacationTypeId="";
    /** 天数 */
    private String days="0";
    /** 列类别名称 */
    private String vacationType="";
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
	 * @return the pdeptId
	 */
	public String getPdeptId() {
		return pdeptId;
	}
	/**
	 * @param pdeptId the pdeptId to set
	 */
	public void setPdeptId(String pdeptId) {
		this.pdeptId = pdeptId;
	}
	/**
	 * @return the pdeptName
	 */
	public String getPdeptName() {
		return pdeptName;
	}
	/**
	 * @param pdeptName the pdeptName to set
	 */
	public void setPdeptName(String pdeptName) {
		this.pdeptName = pdeptName;
	}
	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
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
	 * @return the vacationTypeId
	 */
	public String getVacationTypeId() {
		return vacationTypeId;
	}
	/**
	 * @param vacationTypeId the vacationTypeId to set
	 */
	public void setVacationTypeId(String vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}
	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}
	/**
	 * @return the vacationType
	 */
	public String getVacationType() {
		return vacationType;
	}
	/**
	 * @param vacationType the vacationType to set
	 */
	public void setVacationType(String vacationType) {
		this.vacationType = vacationType;
	}
}
