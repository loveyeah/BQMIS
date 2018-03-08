package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvJBookDetailsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJBookDetailsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJBookDetails entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJBookDetails entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJBookDetails entity);

	/**
	 * Delete a persistent InvJBookDetails entity.
	 * 
	 * @param entity
	 *            InvJBookDetails entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJBookDetails entity);

	/**
	 * Persist a previously saved InvJBookDetails entity and return it or a copy
	 * of it to the sender. A copy of the InvJBookDetails entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            InvJBookDetails entity to update
	 * @return InvJBookDetails the persisted InvJBookDetails entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJBookDetails update(InvJBookDetails entity);

	public InvJBookDetails findById(Long id);
	
	/**
	 * 查询物料盘点状态
	 * @param materialId
	 * 			物料ID
	 * @param	whsNo
	 * 			仓库编码
	 * @param 	location
	 * 			库位编码
	 * @param  lotNo
	 * 			批号
	 *  @param enterpriseCode
	 *  		企业编码
	 *  @return PageObject
	 */
	public PageObject getBookStatus(String materialId,String whsNo,String location,
			String lotNo,String enterpriseCode);
	/**
	 * Find all InvJBookDetails entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJBookDetails property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJBookDetails> found by query
	 */
	public List<InvJBookDetails> findByProperty(String propertyName,
			Object value);

	public List<InvJBookDetails> findByBookNo(Object bookNo);

	public List<InvJBookDetails> findByBookDetailNo(Object bookDetailNo);

	public List<InvJBookDetails> findByMaterialId(Object materialId);

	public List<InvJBookDetails> findByWhsNo(Object whsNo);

	public List<InvJBookDetails> findByLocationNo(Object locationNo);

	public List<InvJBookDetails> findByLotNo(Object lotNo);

	public List<InvJBookDetails> findByBookQty(Object bookQty);

	public List<InvJBookDetails> findByPhysicalQty(Object physicalQty);

	public List<InvJBookDetails> findByReason(Object reason);

	public List<InvJBookDetails> findByBookStatus(Object bookStatus);

	public List<InvJBookDetails> findByEnterpriseCode(Object enterpriseCode);

	public List<InvJBookDetails> findByIsUse(Object isUse);

	/**
	 * Find all InvJBookDetails entities.
	 * 
	 * @return List<InvJBookDetails> all InvJBookDetails entities
	 */
	public List<InvJBookDetails> findAll();
}