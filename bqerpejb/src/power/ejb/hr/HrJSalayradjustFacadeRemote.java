/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJSalayradjustFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJSalayradjustFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJSalayradjust entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJSalayradjust entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJSalayradjust entity) throws SQLException;

	/**
	 * Delete a persistent HrJSalayradjust entity.
	 * 
	 * @param entity
	 *            HrJSalayradjust entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJSalayradjust entity) throws SQLException;
	
	/**
     * 上报岗位调动单
     * @param entity 岗位调动单bean
     * @throws SQLException
     */
	    public void report(HrJSalayradjust entity) throws SQLException;

	/**
	 * Persist a previously saved HrJSalayradjust entity and return it or a copy
	 * of it to the sender. A copy of the HrJSalayradjust entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJSalayradjust entity to update
	 * @return HrJSalayradjust the persisted HrJSalayradjust entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	 public HrJSalayradjust update(HrJSalayradjust entity) throws SQLException, DataChangeException;

	public HrJSalayradjust findById(Long id);

	/**
	 * Find all HrJSalayradjust entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJSalayradjust property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJSalayradjust> found by query
	 */
	public List<HrJSalayradjust> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJSalayradjust entities.
	 * 
	 * @return List<HrJSalayradjust> all HrJSalayradjust entities
	 */
	public List<HrJSalayradjust> findAll();
	
	/**
     * 通过查询条件获得相应的薪酬变动申请上报单信息数据
     * @param strStartDate
     * @param strEndDate
     * @param strDeptCode
     * @param strDcmStatus
     * @param rowStartIdxAndCount
     * @return PageObject
     */
	public PageObject getSalaryChangeList(String strStartDate,String strEndDate,String strDeptCode,
			String strDcmStatus,String enterpriseCode, final int ...rowStartIdxAndCount) throws SQLException;

	
	/**
	 * 根据人员code取得执行岗级，标准岗级，薪级
	 * 
	 * @param empCode
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpInfoMsg(String empCode) throws SQLException ;
	/**
     * 根据岗位调动单ID查找酬变动记录信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
	@SuppressWarnings("unchecked")
	public PageObject getSalayAdjustByRemoveId(String stationRemoveId,String enterpriseCode) throws SQLException;
	/**
	 * 更新数据
	 * @param entity
	 * @param date
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public void updateData(HrJSalayradjust entity,String date)  throws SQLException, DataChangeException;
	/**
	 * 员工名称重复查询
	 * 
	 * @param empId
	 * @return boolean
	 */
	public boolean empInfoIsReapeat(Long empId,String enterpriseCode) throws SQLException ;
}