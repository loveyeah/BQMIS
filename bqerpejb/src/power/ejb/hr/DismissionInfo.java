/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

/**
 * 员工离职登记bean
 * @author zhengzhipeng
 * 
 */
public class DismissionInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	    // Fields

            /** 流水号 */
	    private String dimissionid;
	    /** 离职人员ID */
	    private String empId;
	    /** 员工工号 */
	    private String empCode;
	    /** 员工姓名 */
	    private String empName;
	    /** 员工类别 */
	    private String empTypeName;
	    /** 原工作部门*/
	    private String oldDepName;
	    /** 原工作岗位 */
	    private String oldStationName;
	    /** 离职类别ID */
	    private String outTypeId;
	    /** 离职类别Name */
	    private String outTypeName;
	    /** 离职日期 */
	    private String disMissionDate;
	    /** 离职原因 */
	    private String disMissionReason;
	    /** 离职后去向 */
	    private String whither;
	    /** 是否存档 */
	    private String ifSave;
	    /** 备注 */
	    private String memo;
	    /** 上次修改日期 */
	    private String lastModifiedDate;
	    /** 上次修改日期emp */
	    private String empLastModifiedDate;
	    /**  员工编号(新) **/
	    private String newEmpCode;
	    /** 止薪日期 add by ywliu 20100618*/
	    private String stopsalaryDate;
	    /** 通知单号 add by ywliu 20100618*/
	    private String advicenoteNo;

	    /** 登记时间 add by sychen 20100717*/
	    private String registerDate;
	    
	    
	    /**打印日期  add by wpzhu 20100816
	     * 
	     */
	    private String printDate;
	    public String getPrintDate() {
			return printDate;
		}
		public void setPrintDate(String printDate) {
			this.printDate = printDate;
		}
		/**
	     * @return the dimissionid
	     */
	    public String getDimissionid() {
	        return dimissionid;
	    }
	    /**
	     * @param dimissionid the dimissionid to set
	     */
	    public void setDimissionid(String dimissionid) {
	        this.dimissionid = dimissionid;
	    }
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
	     * @return the empTypeName
	     */
	    public String getEmpTypeName() {
	        return empTypeName;
	    }
	    /**
	     * @param empTypeName the empTypeName to set
	     */
	    public void setEmpTypeName(String empTypeName) {
	        this.empTypeName = empTypeName;
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
	     * @return the oldStationName
	     */
	    public String getOldStationName() {
	        return oldStationName;
	    }
	    /**
	     * @param oldStationName the oldStationName to set
	     */
	    public void setOldStationName(String oldStationName) {
	        this.oldStationName = oldStationName;
	    }
	    /**
	     * @return the outTypeId
	     */
	    public String getOutTypeId() {
	        return outTypeId;
	    }
	    /**
	     * @param outTypeId the outTypeId to set
	     */
	    public void setOutTypeId(String outTypeId) {
	        this.outTypeId = outTypeId;
	    }
	    /**
	     * @return the outTypeName
	     */
	    public String getOutTypeName() {
	        return outTypeName;
	    }
	    /**
	     * @param outTypeName the outTypeName to set
	     */
	    public void setOutTypeName(String outTypeName) {
	        this.outTypeName = outTypeName;
	    }
	    /**
	     * @return the disMissionDate
	     */
	    public String getDisMissionDate() {
	        return disMissionDate;
	    }
	    /**
	     * @param disMissionDate the disMissionDate to set
	     */
	    public void setDisMissionDate(String disMissionDate) {
	        this.disMissionDate = disMissionDate;
	    }
	    /**
	     * @return the disMissionReason
	     */
	    public String getDisMissionReason() {
	        return disMissionReason;
	    }
	    /**
	     * @param disMissionReason the disMissionReason to set
	     */
	    public void setDisMissionReason(String disMissionReason) {
	        this.disMissionReason = disMissionReason;
	    }
	    /**
	     * @return the whither
	     */
	    public String getWhither() {
	        return whither;
	    }
	    /**
	     * @param whither the whither to set
	     */
	    public void setWhither(String whither) {
	        this.whither = whither;
	    }
	    /**
	     * @return the ifSave
	     */
	    public String getIfSave() {
	        return ifSave;
	    }
	    /**
	     * @param ifSave the ifSave to set
	     */
	    public void setIfSave(String ifSave) {
	        this.ifSave = ifSave;
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
	     * @return the empLastModifiedDate
	     */
	    public String getEmpLastModifiedDate() {
	        return empLastModifiedDate;
	    }
	    /**
	     * @param empLastModifiedDate the empLastModifiedDate to set
	     */
	    public void setEmpLastModifiedDate(String empLastModifiedDate) {
	        this.empLastModifiedDate = empLastModifiedDate;
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
		public String getNewEmpCode() {
			return newEmpCode;
		}
		public void setNewEmpCode(String newEmpCode) {
			this.newEmpCode = newEmpCode;
		}
		public String getStopsalaryDate() {
			return stopsalaryDate;
		}
		public void setStopsalaryDate(String stopsalaryDate) {
			this.stopsalaryDate = stopsalaryDate;
		}
		public String getAdvicenoteNo() {
			return advicenoteNo;
		}
		public void setAdvicenoteNo(String advicenoteNo) {
			this.advicenoteNo = advicenoteNo;
		}
		public String getRegisterDate() {
			return registerDate;
		}
		public void setRegisterDate(String registerDate) {
			this.registerDate = registerDate;
		}
		
}
