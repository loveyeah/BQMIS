package power.ejb.administration;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJRoomPriceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJRoomPriceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJRoomPrice entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJRoomPrice entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJRoomPrice entity);

	/**
	 * Delete a persistent AdJRoomPrice entity.
	 * 
	 * @param entity
	 *            AdJRoomPrice entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJRoomPrice entity);

	/**
	 * Persist a previously saved AdJRoomPrice entity and return it or a copy of
	 * it to the sender. A copy of the AdJRoomPrice entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJRoomPrice entity to update
	 * @return AdJRoomPrice the persisted AdJRoomPrice entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJRoomPrice update(AdJRoomPrice entity);

	public AdJRoomPrice findById(Long id);

	/**
	 * Find all AdJRoomPrice entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJRoomPrice property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJRoomPrice> found by query
	 */
	public List<AdJRoomPrice> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJRoomPrice entities.
	 * 
	 * @return List<AdJRoomPrice> all AdJRoomPrice entities
	 */
	public List<AdJRoomPrice> findAll();

	/**
	 * 从房间类别编码查询宾馆房间价格维护
	 *
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return AdJRoomPrice 宾馆房间价格维护
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByRoomTypeCode(String roomTypeCode, final int... rowStartIdxAndCount)
	throws ParseException;
}