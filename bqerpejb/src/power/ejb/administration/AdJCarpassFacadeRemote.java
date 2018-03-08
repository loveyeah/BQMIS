package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJCarpassFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJCarpassFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJCarpass entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarpass entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarpass entity);

	/**
	 * Delete a persistent AdJCarpass entity.
	 * 
	 * @param entity
	 *            AdJCarpass entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	//public void delete(AdJCarpass entity);

	/**
	 * Persist a previously saved AdJCarpass entity and return it or a copy of
	 * it to the sender. A copy of the AdJCarpass entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarpass entity to update
	 * @return AdJCarpass the persisted AdJCarpass entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarpass update(AdJCarpass entity,String updateTime)throws SQLException, DataChangeException;

	public AdJCarpass findById(Long id);

	/**
	 * Find all AdJCarpass entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarpass property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarpass> found by query
	 */
	public List<AdJCarpass> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJCarpass entities.
	 * 
	 * @return List<AdJCarpass> all AdJCarpass entities
	 */
	public List<AdJCarpass> findAll();
	/**
     * 查询所有进出车辆信息 
     * @return PageObject
     */
    public PageObject findAllCar(String enterpriseCode,final int... rowStartIdxAndCount) throws SQLException;
	/**
	 * 逻辑删除一条进出车辆信息. 
	 * @param id, workerCode
	 */
	public void delete(Long id,String workerCode,String updateTime)throws SQLException, DataChangeException;   
}