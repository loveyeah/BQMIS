package power.ejb.hr;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrJContractchangeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJContractchangeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJContractchange entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJContractchange entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJContractchange entity);

	/**
	 * Delete a persistent HrJContractchange entity.
	 * 
	 * @param entity
	 *            HrJContractchange entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJContractchange entity);

	/**
	 * Persist a previously saved HrJContractchange entity and return it or a
	 * copy of it to the sender. A copy of the HrJContractchange entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJContractchange entity to update
	 * @return HrJContractchange the persisted HrJContractchange entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJContractchange update(HrJContractchange entity);

	public HrJContractchange findById(Long id);

	/**
	 * Find all HrJContractchange entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJContractchange property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJContractchange> found by query
	 */
	public List<HrJContractchange> findByProperty(String propertyName,
			Object value);

	public List<HrJContractchange> findByWorkcontractid(Object workcontractid);

	public List<HrJContractchange> findByOldContractCode(Object oldContractCode);

	public List<HrJContractchange> findByNewContractCode(Object newContractCode);

	public List<HrJContractchange> findByOldDepCode(Object oldDepCode);

	public List<HrJContractchange> findByNewDepCode(Object newDepCode);

	public List<HrJContractchange> findByOldStationCode(Object oldStationCode);

	public List<HrJContractchange> findByNewStationCode(Object newStationCode);

	public List<HrJContractchange> findByChangeReason(Object changeReason);

	public List<HrJContractchange> findByMemo(Object memo);

	public List<HrJContractchange> findByInsertby(Object insertby);

	public List<HrJContractchange> findByEnterpriseCode(Object enterpriseCode);

	public List<HrJContractchange> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrJContractchange> findByEmpId2(Object empId2);

	public List<HrJContractchange> findByIsUse(Object isUse);

	/**
	 * Find all HrJContractchange entities.
	 * 
	 * @return List<HrJContractchange> all HrJContractchange entities
	 */
	public List<HrJContractchange> findAll();
}