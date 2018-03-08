package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCEmpTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCEmpTypeFacadeRemote {
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	public HrCEmpType save(HrCEmpType entity) throws CodeRepeatException ;

	public String getEmpTypeName(Long id);
	/**
	 * Delete a persistent HrCEmpType entity.
	 * 
	 * @param entity
	 *            HrCEmpType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCEmpType entity);

	/**
	 * Persist a previously saved HrCEmpType entity and return it or a copy of
	 * it to the sender. A copy of the HrCEmpType entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCEmpType entity to update
	 * @return HrCEmpType the persisted HrCEmpType entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCEmpType update(HrCEmpType entity) throws CodeRepeatException ;

	public HrCEmpType findById(Long id);

	/**
	 * Find all HrCEmpType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCEmpType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCEmpType> found by query
	 */
	public List<HrCEmpType> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);
	public List<HrCEmpType> findByPropertys(String strWhere,
			Object o, int... rowStartIdxAndCount);

	public List<HrCEmpType> findByEmpTypeName(Object empTypeName,
			int... rowStartIdxAndCount);

	public List<HrCEmpType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCEmpType> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCEmpType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCEmpType> all HrCEmpType entities
	 */
	public List<HrCEmpType> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 091123
	 * 查找所有员工类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllEmpTypes(String enterpriseCode);
}