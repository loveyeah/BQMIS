/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 员工借调登记
 * EmpMoveRegisterFacadeRemote interface.
 * 
 * @author chenshoujiang
 */
@Remote
public interface EmpMoveRegisterFacadeRemote {

	/**
	 * 查询员工借调登记信息
	 * @param strStartDate
	 * @param strEndDate
	 * @param strSSDeptCode
	 * @param strJDDeptCode
	 * @param strDcmStatus
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpMoveRegisterInfo(String strStartDate,String strEndDate,String strSSDeptCode,
			String strJDDeptCode,String strDcmStatus,String enterpriseCode,final int ...rowStartIdxAndCount)
			throws SQLException;
	
	/**
	 *  通过部门ID查询员工借调登记
	 * @param strStartDate
	 * @param strEndDate
	 * @param strJDDeptId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpBorrowInByDeptId(String strJDDeptId,String enterpriseCode,final int ...rowStartIdxAndCount)throws SQLException;
}
