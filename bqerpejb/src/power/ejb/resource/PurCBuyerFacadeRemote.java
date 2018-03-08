package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PurCBuyerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurCBuyerFacadeRemote {
	/**
	 * 保存一条记录
	 * 
	 * @param entity 要保存的记录
	 * @throws RuntimeException
	 */
	public void save(PurCBuyer entity);

	/**
	 * Delete a persistent PurCBuyer entity.
	 * 
	 * @param entity
	 *            PurCBuyer entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurCBuyer entity);

	/**
	 * 修改一条记录
	 * 
	 * @param entity 要修改的记录
	 * @return PurCBuyer 修改的记录
	 * @throws RuntimeException 
	 */
	public PurCBuyer update(PurCBuyer entity);

	public PurCBuyer findById(Long id);

	/**
	 * Find all PurCBuyer entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurCBuyer property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurCBuyer> found by query
	 */
	public List<PurCBuyer> findByProperty(String propertyName, Object value);

	/**
	 * 根据采购员编码查询采购员
	 * 
	 * @param buyer 采购员编码
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 采购员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByBuyer(String buyer, String enterpriseCode, final int... rowStartIdxAndCount);

	public List<PurCBuyer> findByBuyerName(Object buyerName);

	public List<PurCBuyer> findByMaterialOrClassNo(Object materialOrClassNo);

	public List<PurCBuyer> findByIsMaterialClass(Object isMaterialClass);

	public List<PurCBuyer> findByLastModifiedBy(Object lastModifiedBy);

	public List<PurCBuyer> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * 根据是否使用查询采购员
	 * 
	 * @param isUse 是否使用
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 采购员
	 */
	public PageObject findByIsUse(String isUse, String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * Find all PurCBuyer entities.
	 * 
	 * @return List<PurCBuyer> all PurCBuyer entities
	 */
	public List<PurCBuyer> findAll();
}