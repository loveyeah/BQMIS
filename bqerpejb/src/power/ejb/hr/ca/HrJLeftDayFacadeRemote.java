package power.ejb.hr.ca;

import power.ear.comm.ejb.PageObject;

public interface HrJLeftDayFacadeRemote {
	public PageObject findLeftDayList(String deptId,final int ...rowStartIdxAndCount);
}