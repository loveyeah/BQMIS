package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJCarwhInvoiceFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJCarwhInvoiceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJCarwhInvoice entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrJCarwhInvoice entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJCarwhInvoice entity);

	/**
	 * Delete a persistent HrJCarwhInvoice entity.
	 *
	 * @param entity
	 *            HrJCarwhInvoice entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJCarwhInvoice entity);

	/**
	 * Persist a previously saved HrJCarwhInvoice entity and return it or a copy
	 * of it to the sender. A copy of the HrJCarwhInvoice entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 *
	 * @param entity
	 *            HrJCarwhInvoice entity to update
	 * @return HrJCarwhInvoice the persisted HrJCarwhInvoice entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJCarwhInvoice update(HrJCarwhInvoice entity);

	public HrJCarwhInvoice findById(Long id);

	/**
	 * Find all HrJCarwhInvoice entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrJCarwhInvoice property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJCarwhInvoice> found by query
	 */
	public List<HrJCarwhInvoice> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJCarwhInvoice entities.
	 *
	 * @return List<HrJCarwhInvoice> all HrJCarwhInvoice entities
	 */
	public List<HrJCarwhInvoice> findAll();
	/**
	 * 根据合同id和附件来源查找劳动合同附件
	 * @param workContractId 合同id
	 * @param fileOriger 附件来源
	 * @param enterpriseCode 企业编码
	 * @return 查找结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAppendFileByIdAndOriger(Long workContractId,
			String fileOriger, String enterpriseCode)throws SQLException;
}