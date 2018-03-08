/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.List;

public class HrCYearPlanFields implements java.io.Serializable{
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 人员list*/
	private List<HrCYearPlanTwo>  yearPlanList ;
	/** 假别list*/
    private List<HrCVacationtype>   vacationType;
	/**
	 * @return the yearPlanList
	 */
	public List<HrCYearPlanTwo> getYearPlanList() {
		return yearPlanList;
	}
	/**
	 * @param yearPlanList the yearPlanList to set
	 */
	public void setYearPlanList(List<HrCYearPlanTwo> yearPlanList) {
		this.yearPlanList = yearPlanList;
	}
	/**
	 * @return the vacationType
	 */
	public List<HrCVacationtype> getVacationType() {
		return vacationType;
	}
	/**
	 * @param vacationType the vacationType to set
	 */
	public void setVacationType(List<HrCVacationtype> vacationType) {
		this.vacationType = vacationType;
	}
	
}
