package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCOperationSalaryFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCOperationSalaryFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCOperationSalary
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCOperationSalary entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCOperationSalary entity);

	/**
	 * Delete a persistent HrCOperationSalary entity.
	 * 
	 * @param entity
	 *            HrCOperationSalary entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids);

	/**
	 * Persist a previously saved HrCOperationSalary entity and return it or a
	 * copy of it to the sender. A copy of the HrCOperationSalary entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCOperationSalary entity to update
	 * @return HrCOperationSalary the persisted HrCOperationSalary entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCOperationSalary update(HrCOperationSalary entity);

	public HrCOperationSalary findById(Long id);

	/**
	 * Find all HrCOperationSalary entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCOperationSalary property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCOperationSalary> found by query
	 */
	public List<HrCOperationSalary> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all HrCOperationSalary entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCOperationSalary> all HrCOperationSalary entities
	 */
	public PageObject findAll(String EnterpriseCode,int... rowStartIdxAndCount);
}