package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for PurJArrivalFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurJArrivalFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PurJArrival entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PurJArrival entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PurJArrival entity);

	/**
	 * Delete a persistent PurJArrival entity.
	 * 
	 * @param entity
	 *            PurJArrival entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurJArrival entity);

	/**
	 * Delete a persistent PurJArrival entity.
	 * 
	 * @param entity
	 *            PurJArrival entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void deleteMuti(Long id) ;
	/**
	 * Persist a previously saved PurJArrival entity and return it or a copy of
	 * it to the sender. A copy of the PurJArrival entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PurJArrival entity to update
	 * @return PurJArrival the persisted PurJArrival entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PurJArrival update(PurJArrival entity);

	public PurJArrival findById(Long id);

	/**
	 * 查询采购单编号.
	 * 
	 * @param mifNo 到货单号
	 * 
	 * @throws CodeRepeatException
	 */
	public String findByMifNo(String argMifNo) throws CodeRepeatException;

	/**
	 * 查询到货登记bean
	 * 
	 * @param mifNo 到货单号
	 * 
	 * @throws CodeRepeatException
	 */
	public PurJArrival findByArrivalNo(String argArrivalNo) throws CodeRepeatException;

	/**
	 * 查询到货单详细id
	 * 
	 * @throws CodeRepeatException
	 */
	public Long findMaxId() throws CodeRepeatException;

	/**
	 * 查询到货单编号
	 * 
	 * @throws CodeRepeatException
	 */
	public String findMaxArrivalNo() throws CodeRepeatException;

	/**
	 * Find all PurJArrival entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurJArrival property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurJArrival> found by query
	 */
	public List<PurJArrival> findByProperty(String propertyName, Object value);

	/**
	 * Find all PurJArrival entities.
	 * 
	 * @return List<PurJArrival> all PurJArrival entities
	 */
	public List<PurJArrival> findAll();
}