package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCTechnologyGradeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCTechnologyGradeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCTechnologyGrade
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCTechnologyGrade entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCTechnologyGrade entity);

	/**
	 * Delete a persistent HrCTechnologyGrade entity.
	 * 
	 * @param entity
	 *            HrCTechnologyGrade entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCTechnologyGrade entity);

	/**
	 * Persist a previously saved HrCTechnologyGrade entity and return it or a
	 * copy of it to the sender. A copy of the HrCTechnologyGrade entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCTechnologyGrade entity to update
	 * @return HrCTechnologyGrade the persisted HrCTechnologyGrade entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCTechnologyGrade update(HrCTechnologyGrade entity);

	public HrCTechnologyGrade findById(Long id);

	/**
	 * Find all HrCTechnologyGrade entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTechnologyGrade property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyGrade> found by query
	 */
	public List<HrCTechnologyGrade> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<HrCTechnologyGrade> findByTechnologyGradeName(
			Object technologyGradeName, int... rowStartIdxAndCount);

	public List<HrCTechnologyGrade> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCTechnologyGrade> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCTechnologyGrade entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyGrade> all HrCTechnologyGrade entities
	 */
	public List<HrCTechnologyGrade> findAll(int... rowStartIdxAndCount);
	
	
	
	/**
	 * add by liuyi 091123
	 * 查找所有技术等级
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllTechnologyGrades(String enterpriseCode);
	
	/**
	 * add by liuyi 20100603 
	 * 通过职称等级查找该id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long findGradeIdByName(String name,String enterpriseCode);
}