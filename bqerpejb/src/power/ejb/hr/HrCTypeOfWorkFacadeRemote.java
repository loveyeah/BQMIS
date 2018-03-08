package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCTypeOfWorkFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCTypeOfWorkFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCTypeOfWork entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCTypeOfWork entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCTypeOfWork entity)	throws CodeRepeatException;

	/**
	 * Delete a persistent HrCTypeOfWork entity.
	 * 
	 * @param entity
	 *            HrCTypeOfWork entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids);

	/**
	 * Persist a previously saved HrCTypeOfWork entity and return it or a copy
	 * of it to the sender. A copy of the HrCTypeOfWork entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCTypeOfWork entity to update
	 * @return HrCTypeOfWork the persisted HrCTypeOfWork entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCTypeOfWork update(HrCTypeOfWork entity) throws CodeRepeatException;

	public HrCTypeOfWork findById(Long id);

	/**
	 * Find all HrCTypeOfWork entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTypeOfWork property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTypeOfWork> found by query
	 */
	public List<HrCTypeOfWork> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<HrCTypeOfWork> findByTypeOfWorkName(Object typeOfWorkName,
			int... rowStartIdxAndCount);

	public List<HrCTypeOfWork> findByTypeOfWorkType(Object typeOfWorkType,
			int... rowStartIdxAndCount);

	public List<HrCTypeOfWork> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCTypeOfWork> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCTypeOfWork entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTypeOfWork> all HrCTypeOfWork entities
	 */
	public List<HrCTypeOfWork> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 091117
	 * 得到所有启用工种类型的信息
	 * @return List<HrCTypeOfWork>
	 */
	public List<HrCTypeOfWork> getTypeOfWorkList();
	
	/**
	 * add by liuyi 091123
	 * 获得工种信息
	 *
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getWorkTypeList(String enterpriseCode,
			final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091123
	 * 查找所有工种编码
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllWorkTypes(String enterpriseCode);
}