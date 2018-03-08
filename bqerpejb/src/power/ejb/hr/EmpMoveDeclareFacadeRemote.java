/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

import java.sql.SQLException;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
//import com.googlecode.jsonplugin.JSONException;
import power.ear.comm.ejb.PageObject;

/**
 * 员工调动单申报
 * EmpMoveDeclareFacadeRemote interface.
 * 
 * @author chenshoujiang
 */
@Remote
public interface EmpMoveDeclareFacadeRemote {
	/**
	 * 员工调动单申报查询
	 * @param strStartDate 开始日期
	 * @param strEndDate 结束日期
	 * @param strbeforeDeptCode 调动前部门
	 * @param strafterDeptCode 调动后部门
	 * @param strDcmStatus 单据状态
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 分页数据
	 * @return PageObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpMoveDeclareInfo(String deptFlag,String queryText,String insertDate,String queryNo,String moveType,String falg,String strStartDate,String strEndDate,String strbeforeDeptCode,String strafterDeptCode,
				String strDcmStatus,String enterpriseCode,final int ...rowStartIdxAndCount)throws SQLException;
	/**
	 *  根据人员code查找部门,岗位,级别信息
	 * @param empCode 人员code
	 * @param enterpriseCode 企业编码
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptStationLevel(String empCode,String enterpriseCode)throws SQLException;
	/**
	 *  上报岗位调动单记录
	 * @param stationRemoveId
	 * @param workerCode
	 * @param enterpriseCode
	 * @param lastModifiedDate
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void report(String stationRemoveId,String workerCode,String enterpriseCode,String lastModifiedDate)
		throws Exception;
	
	/**
	 * 更新岗位调动单和薪酬登记表
	 * @param entity
	 * @param lastModifiedDate
	 * @param bean
	 * @param lastModifiedDateSub
	 * @param flag
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(HrJStationremove entity,String lastModifiedDate,HrJSalayradjust bean,String lastModifiedDateSub,
			String flag)throws Exception ;
	
	/**
	 *  增加
	 * @param entity
	 * @param subBean
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(HrJStationremove entity,HrJSalayradjust subBean)throws Exception;
	/**
	 * 删除岗位调动单记录
	 * @param stationRemoveId
	 * @param workerCode
	 * @param enterpriseCode
	 * @param lastModifiedDate
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(String stationRemoveId,String workerCode,String enterpriseCode,String lastModifiedDate)
		throws Exception;
}
