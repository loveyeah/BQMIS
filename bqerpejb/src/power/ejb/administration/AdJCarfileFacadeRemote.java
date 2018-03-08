/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for AdJCarfileFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJCarfileFacadeRemote {

	/**
	 * 保存车辆档案
	 * 
	 * @param entity 车辆档案实体
	 * @throws SQLException
	 */
	public void save(AdJCarfile entity) throws SQLException;

	/**
	 * Delete a persistent AdJCarfile entity.
	 * 
	 * @param entity
	 *            AdJCarfile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarfile entity);

	/**
	 * 更新车辆档案
	 * 
	 * @param entity 车辆档案实体
	 * @return 车辆档案实体
	 * @throws SQLException
	 */
	public AdJCarfile update(AdJCarfile entity) throws SQLException;

	/**
	 * 更新车辆档案
	 * 
	 * @param entity 车辆档案实体
	 * @return 车辆档案实体
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public AdJCarfile update(AdJCarfile entity, Long updateTime) throws SQLException, DataChangeException;

	/**
	 * 按序号查找车辆档案
	 * 
	 * @param id 序号
	 * @return 车辆档案实体
	 * @throws SQLException
	 */
	public AdJCarfile findById(Long id) throws SQLException;

	/**
	 * 车辆档案是否重复检查
	 * 
	 * @param strCarNo 车牌号
	 * @param strEnterpriseCode 企业代码
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkCar(String strCarNo, String strEnterpriseCode) throws SQLException;
	/**
	 * Find all AdJCarfile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarfile property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarfile> found by query
	 */
	public List<AdJCarfile> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJCarfile entities.
	 * 
	 * @return List<AdJCarfile> all AdJCarfile entities
	 */
	public List<AdJCarfile> findAll();

	/**
	 * 按车牌号查找车辆档案
	 * 
	 * @param strCarNo 车牌号
	 * @return 车辆档案实体
	 * @throws SQLException
	 */
	public AdJCarfile findByCarNo(String strCarNo) throws SQLException;
}