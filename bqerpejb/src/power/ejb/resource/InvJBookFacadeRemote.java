package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for InvJBookFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJBookFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJBook entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJBook entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 *                 /**
     * 
     * @param initialBookNo 传入的单号格式 如"DP000000"
     * @param tabelName 表名
     * @param idColumnName 列名
     * @return 最大的单号
     */
    public String getMaxBookNo(String initialBookNo,String tabelName,String idColumnName) ;
    
	public void save(InvJBook entity);

	/**
	 * Delete a persistent InvJBook entity.
	 * 
	 * @param entity
	 *            InvJBook entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJBook entity);

	/**
	 * Persist a previously saved InvJBook entity and return it or a copy of it
	 * to the sender. A copy of the InvJBook entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJBook entity to update
	 * @return InvJBook the persisted InvJBook entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJBook update(InvJBook entity);

	public InvJBook findById(Long id);

	/**
	 * Find all InvJBook entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJBook property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJBook> found by query
	 */
	public List<InvJBook> findByProperty(String propertyName, Object value);

	public List<InvJBook> findByBookNo(Object bookNo);

	public List<InvJBook> findByBookWhs(Object bookWhs);

	public List<InvJBook> findByBookLocation(Object bookLocation);

	public List<InvJBook> findByBookMaterialClass(Object bookMaterialClass);

	public List<InvJBook> findByBookMan(Object bookMan);

	public List<InvJBook> findByBookStatus(Object bookStatus);

	public List<InvJBook> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvJBook> findByEnterpriseCode(Object enterpriseCode);

	public List<InvJBook> findByIsUse(Object isUse);

	/**
	 * Find all InvJBook entities.
	 * 
	 * @return List<InvJBook> all InvJBook entities
	 */
	public List<InvJBook> findAll();
	
	public List<InvJBook> findBookNoList(String enterpriseCode);
}