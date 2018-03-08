/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * 职工奖惩登记Remote
 * 
 * @author zhaozhijie
 */
@Remote
public interface HrJRewardspunishFacadeRemote {

	/**
	 * 新增职工奖惩登记
	 * 
	 * @param entity 新增职工奖惩登记实体
	 * @throws SQLException 
	 */
	public void save(HrJRewardspunish entity) throws SQLException ;

	/**
	 * Delete a persistent HrJRewardspunish entity.
	 * 
	 * @param entity
	 *            HrJRewardspunish entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJRewardspunish entity);

	/**
	 * 更新职工奖惩登记
	 * 
	 * @param entity 更新职工奖惩登记实体
	 * @return 职工奖惩登记实体
	 * @throws SQLException 
	 */
	public HrJRewardspunish update(HrJRewardspunish entity) throws SQLException;

	public HrJRewardspunish findById(Long id);

	/**
	 * Find all HrJRewardspunish entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJRewardspunish property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJRewardspunish> found by query
	 */
	public List<HrJRewardspunish> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJRewardspunish entities.
	 * 
	 * @return List<HrJRewardspunish> all HrJRewardspunish entities
	 */
	public List<HrJRewardspunish> findAll();

	/**
	 * 查询员工奖惩登记信息
	 * @param  strRewardMonth 奖惩月份
	 * @param strEnterpriseCode 企业代码
	 * @return 所有员工奖惩登记信息
	 * @throws SQLException 
	 */
	public PageObject getEmpRewardInfo(String strRewardMonth, String strEnterpriseCode
			,final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 员工奖惩登记保存操作
	 * @param 新增员工奖惩登记 lstSaveHrJRewardspunish
	 * @param 更新员工奖惩登记 lstUpdateHrJRewardspunish
	 * @param 删除员工奖惩登记 lstDeleteHrJRewardspunish
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	public void save(List<HrJRewardspunish> lstSaveHrJRewardspunish,
			List<HrJRewardspunish> lstUpdateHrJRewardspunish,
			List<HrJRewardspunish> lstDeleteHrJRewardspunish)
	  throws SQLException, DataChangeException ;
}