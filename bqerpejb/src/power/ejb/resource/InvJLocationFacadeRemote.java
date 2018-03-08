package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for InvJLocationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJLocationFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJLocation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJLocation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJLocation entity);

	/**
	 * Delete a persistent InvJLocation entity.
	 * 
	 * @param entity
	 *            InvJLocation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJLocation entity);

	/**
	 * Persist a previously saved InvJLocation entity and return it or a copy of
	 * it to the sender. A copy of the InvJLocation entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJLocation entity to update
	 * @return InvJLocation the persisted InvJLocation entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJLocation update(InvJLocation entity);

	public InvJLocation findById(Long id);

	/**
	 * Find all InvJLocation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJLocation property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJLocation> found by query
	 */
	public List<InvJLocation> findByProperty(String propertyName, Object value);

    public List<InvJLocation> findByMaterialId(Object materialId);

	public List<InvJLocation> findByWhsNo(Object whsNo);

	public List<InvJLocation> findByLocationNo(Object locationNo);

	public List<InvJLocation> findByMaterialNo(Object materialNo);

	public List<InvJLocation> findByMonthCost(Object monthCost);

	public List<InvJLocation> findByYearAmount(Object yearAmount);

	public List<InvJLocation> findByYearCost(Object yearCost);

	public List<InvJLocation> findByOpenBalance(Object openBalance);

	public List<InvJLocation> findByReceipt(Object receipt);

	public List<InvJLocation> findByAdjust(Object adjust);

	public List<InvJLocation> findByIssue(Object issue);

	public List<InvJLocation> findByReserved(Object reserved);

	public List<InvJLocation> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvJLocation> findByEnterpriseCode(Object enterpriseCode);

	public List<InvJLocation> findByIsUse(Object isUse);
	/**
	 * Find all InvJLocation entities.
	 * 
	 * @return List<InvJLocation> all InvJLocation entities
	 */
	public List<InvJLocation> findAll();
	
	/**
	 * 由仓库编码，库位编码和物料编码查询库位物料记录
	 * @param enterpriseCode 企业编码
	 * @param whsNo 仓库编码
	 * @param locationNo 库位编码
	 * @param materialId 物料流水号
	 * @return
	 */
	public List<InvJLocation> findByWHLM(String enterpriseCode, String whsNo, String locationNo, Long materialId);
}