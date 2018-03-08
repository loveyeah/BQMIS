/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.hr.ca;

import java.util.ArrayList;
import java.util.List;

import power.ejb.hr.ca.HrJVacationByW;

/**
 * 请假登记报表Bean
 * @author zhaozhijie
 *
 */
public class VacationRegisterQueryBean {

	 /**请假登记报表数据 */
    private List<HrJVacationByW> list =new ArrayList<HrJVacationByW>();

	/**
	 * @return 请假登记报表数据
	 */
	public List<HrJVacationByW> getList() {
		return list;
	}

	/**
	 * @param 请假登记报表数据
	 */
	public void setList(List<HrJVacationByW> list) {
		this.list = list;
	}

}
