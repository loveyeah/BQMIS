package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrJEmployeeborrowFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJEmployeeborrowFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJEmployeeborrow entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJEmployeeborrow entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmployeeborrow entity);

	/**
	 * Delete a persistent HrJEmployeeborrow entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrow entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmployeeborrow entity);

	/**
	 * Persist a previously saved HrJEmployeeborrow entity and return it or a
	 * copy of it to the sender. A copy of the HrJEmployeeborrow entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrow entity to update
	 * @return HrJEmployeeborrow the persisted HrJEmployeeborrow entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEmployeeborrow update(HrJEmployeeborrow entity);

	public HrJEmployeeborrow findById(Long id);

	/**
	 * Find all HrJEmployeeborrow entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmployeeborrow property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEmployeeborrow> found by query
	 */
	public List<HrJEmployeeborrow> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJEmployeeborrow entities.
	 * 
	 * @return List<HrJEmployeeborrow> all HrJEmployeeborrow entities
	 */
	public List<HrJEmployeeborrow> findAll();
	/**
	 * 
	 * @param list
	 * @throws DataChangeException
	 * @throws SQLException
	 */
//	public void  saveOrUpdateOrDelete(List<HrJEmployeeborrow> list)throws DataChangeException,SQLException,CodeRepeatException;
	/**
	 * 
	 * @throws CodeRepeatException
	 */
	public void checkEmployeeRepeat(HrJEmployeeborrow entity)throws CodeRepeatException;
	/**
	 * 
	 * @param list
	 * @throws SQLException
	 * @throws CodeRepeatException
	 */
	public void save2(List<HrJEmployeeborrow> list)throws SQLException,CodeRepeatException;
	/**
	 * 
	 * @param list
	 * @throws DataChangeException
	 * @throws SQLException
	 * @throws CodeRepeatException
	 */
	public void update2(List<HrJEmployeeborrow> list)throws DataChangeException,SQLException,CodeRepeatException;
	/**
	 * 
	 * @param list
	 * @throws DataChangeException
	 * @throws SQLException
	 * @throws CodeRepeatException
	 */
	public void delete2(List<HrJEmployeeborrow> list)throws DataChangeException,SQLException,CodeRepeatException;

}