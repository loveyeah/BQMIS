package power.ejb.manage.exam;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.exam.form.BpJCbmExecutionForm;

/**
 * Remote interface for BpJCbmExecutionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJCbmExecutionFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJCbmExecution entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJCbmExecution entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJCbmExecution entity);

	/**
	 * Delete a persistent BpJCbmExecution entity.
	 * 
	 * @param entity
	 *            BpJCbmExecution entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJCbmExecution entity);

	/**
	 * Persist a previously saved BpJCbmExecution entity and return it or a copy
	 * of it to the sender. A copy of the BpJCbmExecution entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJCbmExecution entity to update
	 * @return BpJCbmExecution the persisted BpJCbmExecution entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJCbmExecution update(BpJCbmExecution entity);

	public BpJCbmExecution findById(Long id);

	/**
	 * Find all BpJCbmExecution entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJCbmExecution property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJCbmExecution> found by query
	 */
	public List<BpJCbmExecution> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpJCbmExecution entities.
	 * 
	 * @return List<BpJCbmExecution> all BpJCbmExecution entities
	 */
	public List<BpJCbmExecution> findAll();

	public PageObject getExecutionValueList(String type, String datetime,
			String enterpriseCode);

	public boolean saveExecutionValueList(String type, String datetime,
			List<BpJCbmExecutionForm> postlist, String enterpriseCode);

	/**
	 * 查询指标对应的年度计划
	 * 
	 * @author ywliu 20091119
	 * @param itemId
	 * @param datetime
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getYearExecutionValueListByItemId(String itemId,
			String datetime, String enterpriseCode);

	/**
	 * 保存指标对应的年度计划
	 * 
	 * @author ywliu 20091119
	 * @param type
	 * @param datetime
	 * @param postlist
	 * @param enterpriseCode
	 * @return
	 */
	public boolean saveYearExecutionValueList(String type, String datetime,
			List<BpJCbmExecutionForm> postlist, String enterpriseCode);

	/**
	 * 年度计划执行情况查询
	 * 
	 * @author ywliu 20091119
	 * @param datetime
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getYearExecutionValueList(String datetime,
			String enterpriseCode);

	public boolean issueExecutionTable(String enterpriseCode, String dateTime);

	public boolean ifAllowSaveExecutionTable(String enterpriseCode,
			String dateTime);

}