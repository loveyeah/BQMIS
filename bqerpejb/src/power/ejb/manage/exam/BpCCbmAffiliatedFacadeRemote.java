package power.ejb.manage.exam;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCCbmAffiliatedFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCCbmAffiliatedFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCCbmAffiliated entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCCbmAffiliated entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(BpCCbmAffiliated entity);

	/**
	 * Delete a persistent BpCCbmAffiliated entity.
	 * 
	 * @param entity
	 *            BpCCbmAffiliated entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(BpCCbmAffiliated entity);
	public boolean delete(String ids);
//	public boolean namePermitted(Long id,String deptcode);

	/**
	 * Persist a previously saved BpCCbmAffiliated entity and return it or a
	 * copy of it to the sender. A copy of the BpCCbmAffiliated entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCCbmAffiliated entity to update
	 * @return BpCCbmAffiliated the persisted BpCCbmAffiliated entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(BpCCbmAffiliated entity);

	public BpCCbmAffiliated findById(Long id);

	/**
	 * Find all BpCCbmAffiliated entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCCbmAffiliated property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCCbmAffiliated> found by query
	 */
	public List<BpCCbmAffiliated> findByProperty(String propertyName,
			Object value);

	public boolean findByFourProperty(BpCCbmAffiliated entity);
	/**
	 * Find all BpCCbmAffiliated entities.
	 * 
	 * @return List<BpCCbmAffiliated> all BpCCbmAffiliated entities
	 */
	public List<BpCCbmAffiliated> findAll();

	public PageObject findStandardList(String enterpriseCode,
			int... rowStartIdxAndCount) throws Exception;

	public Object findStandardInfo(String aid);
}