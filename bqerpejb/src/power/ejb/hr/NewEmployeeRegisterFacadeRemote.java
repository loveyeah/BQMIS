/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 新进员工登记Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface NewEmployeeRegisterFacadeRemote {

	/**
	 * 查询新进员工信息
	 * 
	 * @param date
	 *            年度
	 * @param deptId
	 *            部门ID
	 * @param isSave
	 *            是否存档
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getNewEmployeeQuery(String flag,String date, String deptId,
			String isSave, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 查询人员信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return PageObject
	 */
	public PageObject getEmpInfoNewEmployee(String enterpriseCode,
			final int... rowStartIdxAndCount);
}
