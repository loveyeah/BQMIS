package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCEducationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCEducationFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCEducation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCEducation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	
	public String getEducationName(Long id);
	public void save(HrCEducation entity) throws CodeRepeatException ;

	/**
	 * Delete a persistent HrCEducation entity.
	 * 
	 * @param entity
	 *            HrCEducation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCEducation entity);

	/**
	 * Persist a previously saved HrCEducation entity and return it or a copy of
	 * it to the sender. A copy of the HrCEducation entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCEducation entity to update
	 * @returns HrCEducation the persisted HrCEducation entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCEducation update(HrCEducation entity) throws CodeRepeatException ;

	public HrCEducation findById(Long id);

	/**
	 * Find all HrCEducation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCEducation property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCEducation> found by query
	 */
	public List<HrCEducation> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<HrCEducation> findByEducationName(Object educationName,
			int... rowStartIdxAndCount);

	public List<HrCEducation> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCEducation> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCEducation entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCEducation> all HrCEducation entities
	 */
	public List<HrCEducation> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 091123
	 * 查找所有学历编码
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllEducations(String enterpriseCode);
	
	/**
	 * add by liuyi 20100603 
	 * 通过学历查找其id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long findEducationIdByName(String name,String enterpriseCode);
}