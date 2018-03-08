package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJdbhCSyxmwhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJdbhCSyxmwhFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJdbhCSyxmwh entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJdbhCSyxmwh entity to persist
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJdbhCSyxmwh entity) throws CodeRepeatException;

	/**
	 * Delete a persistent PtJdbhCSyxmwh entity.
	 * 
	 * @param entity
	 *            PtJdbhCSyxmwh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids);

	/**
	 * Persist a previously saved PtJdbhCSyxmwh entity and return it or a copy
	 * of it to the sender. A copy of the PtJdbhCSyxmwh entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJdbhCSyxmwh entity to update
	 * @return PtJdbhCSyxmwh the persisted PtJdbhCSyxmwh entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJdbhCSyxmwh update(PtJdbhCSyxmwh entity) throws CodeRepeatException;

	public PtJdbhCSyxmwh findById(Long id);

	/**
	 * Find all PtJdbhCSyxmwh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJdbhCSyxmwh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhCSyxmwh> found by query
	 */
	public List<PtJdbhCSyxmwh> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<PtJdbhCSyxmwh> findBySyxmName(Object syxmName,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSyxmwh> findByDisplayNo(Object displayNo,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSyxmwh> findByEnterpriseCode(Object enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * 模糊查询所有试验项目
	 * @param fuzzyString
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findPtJdbhCSyxmwhList(String enterpriseCode, int... rowStartIdxAndCount);
}