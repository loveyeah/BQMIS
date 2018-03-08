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
 * 学历简历登记 远程处理对象
 * 
 * @author wangjunjie
 */
@Remote
public interface HrJEducationFacadeRemote {
	/**
	 * 新增学历简历
	 * 
	 * @param entity 学历简历
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEducation entity) throws SQLException;

	/**
	 * Delete a persistent HrJEducation entity.
	 * 
	 * @param entity
	 *            HrJEducation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEducation entity);

	/**
	 * 修改学历简历
	 * 
	 * @param entity 学历简历
	 * @return HrJEducation 修改后的学历简历
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEducation update(HrJEducation entity) throws SQLException, DataChangeException;

	public HrJEducation findById(Long id);

	/**
	 * Find all HrJEducation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEducation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEducation> found by query
	 */
	public List<HrJEducation> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrJEducation entities.
	 * 
	 * @return List<HrJEducation> all HrJEducation entities
	 */
	public List<HrJEducation> findAll();

	/**
	 * 根据员工Id, 获得其学历简历基本信息
	 * @param argEmpId 员工Id
	 * @param argEnterpriseCode 企业编码
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 学历简历基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PageObject getEducationInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount);
	/**
	 * 根据员工Id, 获得其原始学历基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码	
	 * @return 学历简历基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public HrJEducationBean getOriEducationInfo(Long argEmpId, String argEnterpriseCode) ;
	
	
	/**
	 * add by liuyi 20100609 
	 * 批量删除
	 * @param ids
	 */
	void delete(String ids);
	
	/**
	 * add by liuyi 20100611 
	 * 批量导入
	 * @param educationList
	 */
	void importPersonnelFilesWorkResume(List<HrJEducation> educationList);
}