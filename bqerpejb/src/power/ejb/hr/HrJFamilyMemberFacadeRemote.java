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
 * 社会关系登记 远程处理对象
 * 
 * @author wangjunjie
 */
@Remote
public interface HrJFamilyMemberFacadeRemote {
	/**
	 * 新增社会关系
	 * 
	 * @param entity 社会关系
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJFamilymember entity) throws SQLException;

	/**
	 * Delete a persistent HrJFamilymember entity.
	 * 
	 * @param entity
	 *            HrJFamilymember entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJFamilymember entity);

	/**
	 * 修改社会关系
	 * 
	 * @param entity 社会关系
	 * @return HrJFamilymember 修改后的社会关系
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJFamilymember update(HrJFamilymember entity) throws SQLException, DataChangeException;

	public HrJFamilymember findById(Long id);

	/**
	 * Find all HrJFamilymember entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJFamilymember property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJFamilymember> found by query
	 */
	public List<HrJFamilymember> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrJFamilymember entities.
	 * 
	 * @return List<HrJFamilymember> all HrJFamilymember entities
	 */
	public List<HrJFamilymember> findAll();

	/**
	 * 根据员工Id, 获得其社会关系基本信息
	 * @param argEmpId 员工Id
	 * @param argEnterpriseCode 企业编码
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 社会关系基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PageObject getFamilyMemberInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 20100610
	 * 批量删除
	 * @param ids
	 */
	void delete(String ids);
	
	void importPersonnelFilesWorkResume(List<HrJFamilymember> familyMemberList);
}