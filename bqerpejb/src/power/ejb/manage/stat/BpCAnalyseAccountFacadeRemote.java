package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCAnalyseAccountFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCAnalyseAccountFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCAnalyseAccount entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCAnalyseAccount entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCAnalyseAccount entity);

	public void save(List<BpCAnalyseAccount> addList);

	/**
	 * Delete a persistent BpCAnalyseAccount entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccount entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCAnalyseAccount entity);

	public boolean delete(String ids);

	/**
	 * Persist a previously saved BpCAnalyseAccount entity and return it or a
	 * copy of it to the sender. A copy of the BpCAnalyseAccount entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccount entity to update
	 * @return BpCAnalyseAccount the persisted BpCAnalyseAccount entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCAnalyseAccount update(BpCAnalyseAccount entity);

	public void update(List<BpCAnalyseAccount> updateList);

	public BpCAnalyseAccount findById(Long id);

	/**
	 * Find all BpCAnalyseAccount entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCAnalyseAccount property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCAnalyseAccount> found by query
	 */
	public List<BpCAnalyseAccount> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpCAnalyseAccount entities.
	 * 
	 * @return List<BpCAnalyseAccount> all BpCAnalyseAccount entities
	 */
	public PageObject findAll(String enterpriseCode, String type,String workerCode,int... rowStartIdxAndCount);
	// public Long checkAccountCode(String acconutCode);
}