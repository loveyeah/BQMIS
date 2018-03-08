package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PurCPlanerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurCPlanerFacadeRemote {
	
	/**
	 * 保存一条记录
	 * 
	 * @param entity 要保存的记录
	 * @throws RuntimeException
	 */
	public void save(PurCPlaner entity);

	/**
	 * Delete a persistent PurCPlaner entity.
	 * 
	 * @param entity
	 *            PurCPlaner entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurCPlaner entity);

	/**
	 * 修改一条记录
	 * 
	 * @param entity 要修改的记录
	 * @return PurCPlaner 修改的记录
	 * @throws RuntimeException 
	 */
	public PurCPlaner update(PurCPlaner entity);

	public PurCPlaner findById(Long id);

	/**
	 * Find all PurCPlaner entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurCPlaner property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurCPlaner> found by query
	 */
	public List<PurCPlaner> findByProperty(String propertyName, Object value);

	/**
	 * 根据计划员编码查询计划员
	 * 
	 * @param planer 计划员编码
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 计划员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByPlaner(String planer, String enterpriseCode, final int... rowStartIdxAndCount);

	public List<PurCPlaner> findByPlanerName(Object planerName);

	public List<PurCPlaner> findByMaterialOrClassNo(Object materialOrClassNo);

	public List<PurCPlaner> findByIsMaterialClass(Object isMaterialClass);

	public List<PurCPlaner> findByLastModifiedBy(Object lastModifiedBy);

	public List<PurCPlaner> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * 根据是否使用查询计划员
	 * 
	 * @param isUse 是否使用
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 计划员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByIsUse(String isUse, String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * Find all PurCPlaner entities.
	 * 
	 * @return List<PurCPlaner> all PurCPlaner entities
	 */
	public List<PurCPlaner> findAll();
	/**
	 * 查询所有数据
	 * return PageObject
	 */
	public PageObject findAllList(String enterpriseCode);
	
	/**
	 * 查询计划员为物料盘点打印做选择
	 * 
	 * @param isUse 是否使用
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return List 计划员
	 * add By ywliu 09/05/19
	 */
	public List<PurCPlaner> findPlanerForMaterialInventory(String enterpriseCode, final int... rowStartIdxAndCount);
}