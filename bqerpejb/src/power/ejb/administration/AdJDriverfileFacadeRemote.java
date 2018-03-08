/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJDriverfileFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJDriverfileFacadeRemote {

	/**
	 * 保存司机档案
	 * 
	 * @param entity 司机实体
	 */
	public void save(AdJDriverfile entity) throws SQLException;

	/**
	 * Delete a persistent AdJDriverfile entity.
	 * 
	 * @param entity
	 *            AdJDriverfile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJDriverfile entity);

	/**
	 * 更新司机档案
	 * 
	 * @param entity 司机实体
	 * @return 司机实体
	 */
	public AdJDriverfile update(AdJDriverfile entity) throws SQLException;

	/**
	 * 按序号查找司机实体
	 * 
	 * @param id 序号
	 * @return 司机实体
	 */
	public AdJDriverfile findById(Long id) throws SQLException;
	
	/**
	 * 司机档案是否重复检查
	 * 
	 * @param strDriverCode 司机编码
	 * @param strEnterpriseCode 企业代码
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkDriver(String strDriverCode, String strEnterpriseCode) throws SQLException;

	/**
	 * Find all AdJDriverfile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJDriverfile property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJDriverfile> found by query
	 */
	public List<AdJDriverfile> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJDriverfile entities.
	 * 
	 * @return List<AdJDriverfile> all AdJDriverfile entities
	 */
	public List<AdJDriverfile> findAll();
}