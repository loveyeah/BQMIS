package power.ejb.hr.ca;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrCOvertimeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCOvertimeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCOvertime entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCOvertime entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCOvertime entity);

	/**
	 * Delete a persistent HrCOvertime entity.
	 * 
	 * @param entity
	 *            HrCOvertime entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCOvertime entity);

	/**
	 * Persist a previously saved HrCOvertime entity and return it or a copy of
	 * it to the sender. A copy of the HrCOvertime entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCOvertime entity to update
	 * @return HrCOvertime the persisted HrCOvertime entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCOvertime update(HrCOvertime entity);

	public HrCOvertime findById(Long id);

	/**
	 * Find all HrCOvertime entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCOvertime property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCOvertime> found by query
	 */
	public List<HrCOvertime> findByProperty(String propertyName, Object value);

	public List<HrCOvertime> findByOvertimeTypeCode(Object overtimeTypeCode);

	public List<HrCOvertime> findByOvertimeType(Object overtimeType);

	public List<HrCOvertime> findByIfOvertimeFee(Object ifOvertimeFee);

	public List<HrCOvertime> findByOvertimeMark(Object overtimeMark);

	public List<HrCOvertime> findByLastModifiyBy(Object lastModifiyBy);

	public List<HrCOvertime> findByIsUse(Object isUse);

	public List<HrCOvertime> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all HrCOvertime entities.
	 * 
	 * @return List<HrCOvertime> all HrCOvertime entities
	 */
	public List<HrCOvertime> findAll();
}