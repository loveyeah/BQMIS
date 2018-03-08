/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 年初计划登记.        
 * 
 * @author wujiao
 */
public class HrCYearPlanTwo implements java.io.Serializable {
    /** serial id */
    private static final long serialVersionUID = 1L;
    /** 假别id*/
    private String vacationTypeId;
    /** 假别*/
    private String vacationType;
    /** 人员id*/
    private String empId;
    /** 中文名*/
    private String chsName;
    /** 部门名称*/
    private String deptName;
    /** 天数*/
    private String days;
    /** 时长*/
    private String hours;
    /** 签字状态*/
    private String signState;
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
     * @return the hours
     */
    public String getHours() {
        return hours;
    }
    /**
     * @param hours the hours to set
     */
    public void setHours(String hours) {
        this.hours = hours;
    }
    /**
     * @return the signState
     */
    public String getSignState() {
        return signState;
    }
    /**
     * @param signState the signState to set
     */
    public void setSignState(String signState) {
        this.signState = signState;
    }
}
