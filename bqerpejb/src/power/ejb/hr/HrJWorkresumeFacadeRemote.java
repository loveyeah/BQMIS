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
 * 工作简历登记 远程处理对象
 * 
 * @author huyou
 */
@Remote
public interface HrJWorkresumeFacadeRemote {
	/**
	 * 新增工作简历
	 * 
	 * @param entity 工作简历
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkresume entity) throws SQLException;

	/**
	 * Delete a persistent HrJWorkresume entity.
	 * 
	 * @param entity
	 *            HrJWorkresume entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkresume entity);

	/**
	 * 修改工作简历
	 * 
	 * @param entity 工作简历
	 * @return HrJWorkresume 修改后的工作简历
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJWorkresume update(HrJWorkresume entity) throws SQLException, DataChangeException;

	public HrJWorkresume findById(Long id);

	/**
	 * Find all HrJWorkresume entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkresume property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkresume> found by query
	 */
	public List<HrJWorkresume> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrJWorkresume entities.
	 * 
	 * @return List<HrJWorkresume> all HrJWorkresume entities
	 */
	public List<HrJWorkresume> findAll();

	/**
	 * 根据员工Id, 获得其工作简历基本信息
	 * @param argEmpId 员工Id
	 * @param argEnterpriseCode 企业编码
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 工作简历基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PageObject getWorksumeInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount);
	
	/**
	 * 查询一条有参数sql语句,返回查询结果 
	 * @param argSql SQL语句
	 * @param params 参数值数组
	 * @param argClass JavaBean class对象
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 符合条件的List对象
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public List queryDescribeByNativeSQL(String argSql, Object[] params, Class<?> argClass,
			final int ...rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 20100609 
	 * 删除多个工作简历
	 * @param ids
	 */
	void deleteMutilEmpWorkresumeInfo(String ids);
	
	/**
	 * add by liuyi 20100611 
	 * 批量导入多个工作简历
	 * @param workResumeList
	 */
	void importPersonnelFilesWorkResume(List<HrJWorkresume> workResumeList);
}