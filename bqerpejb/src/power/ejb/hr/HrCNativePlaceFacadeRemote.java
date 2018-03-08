package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCNativePlaceFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCNativePlaceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCNativePlace entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCNativePlace entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCNativePlace entity);

	/**
	 * Delete a persistent HrCNativePlace entity.
	 *
	 * @param entity
	 *            HrCNativePlace entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCNativePlace entity);

	/**
	 * Persist a previously saved HrCNativePlace entity and return it or a copy
	 * of it to the sender. A copy of the HrCNativePlace entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 *
	 * @param entity
	 *            HrCNativePlace entity to update
	 * @return HrCNativePlace the persisted HrCNativePlace entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCNativePlace update(HrCNativePlace entity);

	public HrCNativePlace findById(Long id);

	/**
	 * Find all HrCNativePlace entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCNativePlace property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCNativePlace> found by query
	 */
	public List<HrCNativePlace> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCNativePlace entities.
	 *
	 * @return List<HrCNativePlace> all HrCNativePlace entities
	 */
	public List<HrCNativePlace> findAll();
	/**
	 * 籍贯检索
	 * @param enterpriceCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllNativePlace(String enterpriceCode);
	
	/**
	 * add by liuyi 20100610 
	 * 通过名称查询id
	 * @param name
	 * @return
	 */
	Long getNativePlaceIdByName(String name);
}