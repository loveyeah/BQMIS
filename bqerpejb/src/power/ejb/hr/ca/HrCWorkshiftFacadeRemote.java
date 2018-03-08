package power.ejb.hr.ca;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrCWorkshiftFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCWorkshiftFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCWorkshift entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCWorkshift entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCWorkshift entity);

	/**
	 * Delete a persistent HrCWorkshift entity.
	 * 
	 * @param entity
	 *            HrCWorkshift entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCWorkshift entity);

	/**
	 * Persist a previously saved HrCWorkshift entity and return it or a copy of
	 * it to the sender. A copy of the HrCWorkshift entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCWorkshift entity to update
	 * @return HrCWorkshift the persisted HrCWorkshift entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCWorkshift update(HrCWorkshift entity);

	public HrCWorkshift findById(Long id);

	/**
	 * Find all HrCWorkshift entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCWorkshift property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCWorkshift> found by query
	 */
	public List<HrCWorkshift> findByProperty(String propertyName, Object value);

	public List<HrCWorkshift> findByWorkShiftCode(Object workShiftCode);

	public List<HrCWorkshift> findByWorkShift(Object workShift);

	public List<HrCWorkshift> findByWorkShitFee(Object workShitFee);

	public List<HrCWorkshift> findByWorkShiftMark(Object workShiftMark);

	public List<HrCWorkshift> findByLastModifiyBy(Object lastModifiyBy);

	public List<HrCWorkshift> findByIsUse(Object isUse);

	public List<HrCWorkshift> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all HrCWorkshift entities.
	 * 
	 * @return List<HrCWorkshift> all HrCWorkshift entities
	 */
	public List<HrCWorkshift> findAll();
}