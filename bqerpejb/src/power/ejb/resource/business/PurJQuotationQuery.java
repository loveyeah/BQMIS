/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource.business;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.PurJQuotation;

/**
 * Remote interface for PurJQuotationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurJQuotationQuery {
	/**
	 * Perform an initial save of a previously unsaved PurJQuotation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PurJQuotation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Boolean save(PurJQuotation entity) throws CodeRepeatException;

	/**
	 * Delete a persistent PurJQuotation entity.
	 * 
	 * @param entity
	 *            PurJQuotation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long quotationId, String strEmployee);

	/**
	 * Persist a previously saved PurJQuotation entity and return it or a copy
	 * of it to the sender. A copy of the PurJQuotation entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PurJQuotation entity to update
	 * @return PurJQuotation the persisted PurJQuotation entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PurJQuotation update(PurJQuotation entity);
	
	/**
	 * find data by id 
	 * @param id
	 * @return
	 */
	public PurJQuotation findById(Long id);

	/**
	 * Find PurJQuotation entities.
	 * 
	 * @param fuzzy
	 *            the name of the PurJQuotation property to query
	 * @return PageObject
	 */
	public PageObject getDetail(String enterpriseCode, String fuzzy,
			final int... rowStartIdxAndCount);

	/**
	 * Find All Currency ID and Name.
	 * 
	 * @param enterpriseCode
	 * 
	 * @return PageObject
	 */
	public PageObject getAllCurrency(String enterpriseCode);
	
	/**
	 * 检查有效日期是否重叠
	 */
	public Boolean isValid(PurJQuotation entity); 
}