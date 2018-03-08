package power.ejb.hr.ca;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrCExchangetorestFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCExchangetorestFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCExchangetorest entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCExchangetorest entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCExchangetorest entity);

	/**
	 * Delete a persistent HrCExchangetorest entity.
	 * 
	 * @param entity
	 *            HrCExchangetorest entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCExchangetorest entity);

	/**
	 * Persist a previously saved HrCExchangetorest entity and return it or a
	 * copy of it to the sender. A copy of the HrCExchangetorest entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCExchangetorest entity to update
	 * @return HrCExchangetorest the persisted HrCExchangetorest entity
	 *         instance, may not be the same
	 * @throws DataChangeException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCExchangetorest update(HrCExchangetorest entity) throws DataChangeException;

	public HrCExchangetorest findById(HrCExchangetorestId id);

	/**
	 * Find all HrCExchangetorest entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCExchangetorest property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCExchangetorest> found by query
	 */
	public List<HrCExchangetorest> findByProperty(String propertyName,
			Object value);

	public List<HrCExchangetorest> findByExchangerestHours(
			Object exchangerestHours);

	public List<HrCExchangetorest> findBySignState(Object signState);

	public List<HrCExchangetorest> findByWorkFlowNo(Object workFlowNo);

	public List<HrCExchangetorest> findByLastModifiyBy(Object lastModifiyBy);

	public List<HrCExchangetorest> findByIsUse(Object isUse);

	public List<HrCExchangetorest> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all HrCExchangetorest entities.
	 * 
	 * @return List<HrCExchangetorest> all HrCExchangetorest entities
	 */
	public List<HrCExchangetorest> findAll();

	/**
	 * 查询加班换休信息
	 * 
	 * @param depId
	 *            部门ID
	 * @param year
	 *            考勤年份
	 * @param month
	 *            考勤月份
	 * @param enterpriseCode
	 *            企业编码
	 * @return List<OverTimeRegiste>
	 */
	public List<OverTimeRegiste> searchLastMonth(String depId, String year,
			String month, String enterpriseCode);

	/**
	 * 加班换休登记表中插入或更新
	 * 
	 * @param hrCExchangetorest
	 * @throws DataChangeException 
	 */
	public boolean saveOrUpdate(List<HrCExchangetorest> list) throws DataChangeException, RuntimeException,Exception;
}