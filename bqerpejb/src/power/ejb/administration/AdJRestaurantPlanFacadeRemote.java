package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJRestaurantPlanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJRestaurantPlanFacadeRemote {
	/**
	 * 增加餐厅计划
	 * @author sufeiyu
	 * @param entity
	 *            餐厅计划 entity 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJRestaurantPlan entity) throws SQLException;

	/**
	 * 删除一条餐厅计划
	 * @author sufeiyu
	 * @param strEmployee 修改人
	 * @param lngId 序号
	 * @param strUpdateTime 上次修改时间
	 * 
	 */
	public void delete(String strEmployee, Long lngId, String strUpdateTime)
	throws DataChangeException, SQLException;

	/**
	 * 修改餐厅计划数据
	 * @author sufeiyu
	 * @param entity
	 *            餐厅计划entity
	 * @return 餐厅计划object
	 * @throws DataChangeException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(AdJRestaurantPlan entity, String strUpdateTime)
	throws DataChangeException, SQLException;

	public AdJRestaurantPlan findById(Long id);

	/**
	 * Find all AdJRestaurantPlan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJRestaurantPlan property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJRestaurantPlan> found by query
	 */
	public List<AdJRestaurantPlan> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all AdJRestaurantPlan entities.
	 * 
	 * @return List<AdJRestaurantPlan> all AdJRestaurantPlan entities
	 */
	public List<AdJRestaurantPlan> findAll();
	
	/**
	 * 取得餐厅计划的详细信息
	 * 
	 * @param planDate 日期
	 * 
	 * @return PageObject 餐厅计划信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getRestaurantPlan(String strEnterpriseCode, String planDate, final int... rowStartIdxAndCount);
	
	/**
	 *  获得表中最大的ID
	 */
	public Long getMaxId();
}