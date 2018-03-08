package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ear.comm.DataChangeException;

/**
 * Remote interface for AdJOndutyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJOndutyFacadeRemote {
	
	/**
	 * 值班记事保存
	 * 
	 * @param entity
	 *            值班保存entity
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJOnduty entity) throws SQLException;

	/**
	 * 删除一条值班记事数据
	 * 
	 * @param entity
	 *            值班记事entity
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String strEmployee, Long noteId, String strUpdateTime) throws DataChangeException, SQLException;

	/**
	 * 更新值班记事entity
	 * 
	 * @param entity
	 *            值班记事entity
	 * @return 值班记事Object对象 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(AdJOnduty entity, String strUpdateTime)
	throws DataChangeException, SQLException;

	public AdJOnduty findById(Long id);

	/**
	 * Find all AdJOnduty entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOnduty property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOnduty> found by query
	 */
	public List<AdJOnduty> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJOnduty entities.
	 * 
	 * @return List<AdJOnduty> all AdJOnduty entities
	 */
	public List<AdJOnduty> findAll();
	
	/**
	 * 取得值班记事的详细信息
	 * @param worktypeCode 工作类别
	 * @return PageObject 值班记事信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getRecord(String strEnterpriseCode, String worktypeCode, final int... rowStartIdxAndCount);
}