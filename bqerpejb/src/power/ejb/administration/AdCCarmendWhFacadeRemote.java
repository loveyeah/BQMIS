package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdCCarmendWhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdCCarmendWhFacadeRemote {
	/**
	 * 保存数据
	 * 
	 * @param entity
	 *            AdCCarmendWh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCCarmendWh entity) throws SQLException;

	/**
	 * Delete a persistent AdCCarmendWh entity.
	 * 
	 * @param entity
	 *            AdCCarmendWh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCCarmendWh entity);

	/**
	 * 更新车辆维修单位维护表
	 * @param entity
	 *            AdCCarmendWh entity to update
	 * @return AdCCarmendWh the persisted AdCCarmendWh entity instance, may not
	 *         be the same
	 * @throws DataFormatException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCCarmendWh update(AdCCarmendWh entity) throws DataFormatException, SQLException;

	public AdCCarmendWh findById(Long id);

	/**
	 * Find all AdCCarmendWh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCCarmendWh property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCCarmendWh> found by query
	 */
	public List<AdCCarmendWh> findByProperty(String propertyName, Object value);
    /**
     * 通过是否使用查找车辆维护单位
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     */
	public PageObject findByIsUse(String strEnterPriseCode,int start,int limit) throws SQLException;
	/**
	 * Find all AdCCarmendWh entities.
	 * 
	 * @return List<AdCCarmendWh> all AdCCarmendWh entities
	 */
	public List<AdCCarmendWh> findAll();
	
}