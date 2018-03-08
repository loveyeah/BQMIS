package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ejb.manage.contract.form.ConFileOpinionForm;

/**
 * Remote interface for ConJFileOpinionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConJFileOpinionFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved ConJFileOpinion entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConJFileOpinion entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ConJFileOpinion entity) throws CodeRepeatException;

	/**
	 * Delete a persistent ConJFileOpinion entity.
	 * 
	 * @param entity
	 *            ConJFileOpinion entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ConJFileOpinion entity);

	/**
	 * Persist a previously saved ConJFileOpinion entity and return it or a copy
	 * of it to the sender. A copy of the ConJFileOpinion entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            ConJFileOpinion entity to update
	 * @returns ConJFileOpinion the persisted ConJFileOpinion entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConJFileOpinion update(ConJFileOpinion entity);

	public ConJFileOpinion findById(Long id);

	/**
	 * Find all ConJFileOpinion entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConJFileOpinion property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJFileOpinion> found by query
	 */
	public List<ConJFileOpinion> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all ConJFileOpinion entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJFileOpinion> all ConJFileOpinion entities
	 */
	public List<ConJFileOpinion> findAll(int... rowStartIdxAndCount);
	/**
	 * 查询合同归档退回意见列表
	 * @param id
	 * @param type
	 * @param enterpriseCode
	 * @return
	 */
	public List<ConFileOpinionForm> findFileOList(Long id,String type,String enterpriseCode);
}