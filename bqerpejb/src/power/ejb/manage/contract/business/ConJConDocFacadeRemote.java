package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.ConDocForm;

/**
 * Remote interface for ConJConDocFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConJConDocFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved ConJConDoc entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConJConDoc entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public ConJConDoc save(ConJConDoc entity);

	/**
	 * Delete a persistent ConJConDoc entity.
	 * 
	 * @param entity
	 *            ConJConDoc entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long contypeId);

	/**
	 * 删除多条记录
	 * @param contypeIds
	 */
	public void deleteMulti(String contypeIds);
	/**
	 * Persist a previously saved ConJConDoc entity and return it or a copy of
	 * it to the sender. A copy of the ConJConDoc entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            ConJConDoc entity to update
	 * @return ConJConDoc the persisted ConJConDoc entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConJConDoc update(ConJConDoc entity);

	public ConJConDoc findById(Long id);

	/**
	 * Find all ConJConDoc entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConJConDoc property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJConDoc> found by query
	 */
	public List<ConJConDoc> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all ConJConDoc entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJConDoc> all ConJConDoc entities
	 */
	public List<ConJConDoc> findAll(int... rowStartIdxAndCount);
	
/**
 * 根据keyID，文件类型查询
 * @param enterpriseCode
 * @param conModifyId
 * @param docType
 * @return
 */
	public PageObject findConDocList(String enterpriseCode,Long keyId,String docType,Long conDocId);
	
	/**
	 * 根据keyID，文件类型取model
	 * @param enterpriseCode
	 * @param keyId
	 * @param docType
	 * @return
	 */
	public ConJConDoc findConDocModel(String enterpriseCode,Long keyId,String docType);
}