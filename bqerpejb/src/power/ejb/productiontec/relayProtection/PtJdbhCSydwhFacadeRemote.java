package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for PtJdbhCSydwhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJdbhCSydwhFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJdbhCSydwh entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJdbhCSydwh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJdbhCSydwh entity) throws CodeRepeatException;

	/**
	 * Delete a persistent PtJdbhCSydwh entity.
	 * 
	 * @param entity
	 *            PtJdbhCSydwh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJdbhCSydwh entity);

	/**
	 * Persist a previously saved PtJdbhCSydwh entity and return it or a copy of
	 * it to the sender. A copy of the PtJdbhCSydwh entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJdbhCSydwh entity to update
	 * @return PtJdbhCSydwh the persisted PtJdbhCSydwh entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJdbhCSydwh update(PtJdbhCSydwh entity);
	
	/**
	 * 试验点保存，修改，删除
	 * @param addList
	 * @param updateList
	 * @param delIds
	 */
	public void save(List<PtJdbhCSydwh> addList,List<PtJdbhCSydwh> updateList, String delIds) throws CodeRepeatException;

	public PtJdbhCSydwh findById(Long id);

	/**
	 * Find all PtJdbhCSydwh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJdbhCSydwh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhCSydwh> found by query
	 */
	public List<PtJdbhCSydwh> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findBySyxmId(Object syxmId,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findBySydName(Object sydName,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findByUnitId(Object unitId,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findByMinimum(Object minimum,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findByMaximum(Object maximum,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findByDisplayNo(Object displayNo,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findByMemo(Object memo,
			int... rowStartIdxAndCount);

	public List<PtJdbhCSydwh> findByEnterpriseCode(Object enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all PtJdbhCSydwh entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhCSydwh> all PtJdbhCSydwh entities
	 */
	public List<PtJdbhCSydwh> findAll(int... rowStartIdxAndCount);
}