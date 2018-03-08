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
 * 政治面貌登记 远程处理对象
 * 
 * @author wangjunjie
 */
@Remote
public interface HrJPoliticsFacadeRemote {
	/**
	 * 新增政治面貌
	 * 
	 * @param entity 政治面貌
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJPolitics entity) throws SQLException;

	/**
	 * Delete a persistent HrJPolitics entity.
	 * 
	 * @param entity
	 *            HrJPolitics entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJPolitics entity);

	/**
	 * 修改政治面貌
	 * 
	 * @param entity 政治面貌
	 * @return HrJPolitics 修改后的政治面貌
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJPolitics update(HrJPolitics entity) throws SQLException, DataChangeException;

	public HrJPolitics findById(Long id);

	/**
	 * Find all HrJPolitics entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJPolitics property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJPolitics> found by query
	 */
	public List<HrJPolitics> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrJPolitics entities.
	 * 
	 * @return List<HrJPolitics> all HrJPolitics entities
	 */
	public List<HrJPolitics> findAll();

	/**
	 * 根据员工Id, 获得其政治面貌基本信息
	 * @param argEmpId 员工Id
	 * @param argEnterpriseCode 企业编码
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 政治面貌基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PageObject getPoliticsInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount);
	/**
	 * 根据员工Id, 获得其当前政治面貌基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码	
	 * @return HrJPoliticsBean
	 * 			  当前政治面貌基本信息 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public HrJPoliticsBean getNowPoliticsInfo(Long argEmpId, String argEnterpriseCode);
	
	/**
	 * add by liuyi 20100603 
	 * 通过政治面貌名称查找该id 
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long findPoliticsIdByName(String name,String enterpriseCode);
}