package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJDutymanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJDutymanFacadeRemote {
	
	/**
	 * 增加值班人员
	 * 
	 * @param entity
	 *            值班人员entity
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJDutyman entity) throws SQLException;

	/**
	 * 删除值班人员
	 * 
	 * @param entity
	 *            值班人员entity
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String strEmployee, Long personId, String strUpdateTime)
	throws DataChangeException, SQLException;

	/**
	 * 修改值班人员数据
	 * 
	 * @param entity
	 *            值班人员entity
	 * @return 值班人员object
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(AdJDutyman entity, String strUpdateTime)
	throws DataChangeException, SQLException;

	public AdJDutyman findById(Long id);

	/**
	 * Find all AdJDutyman entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJDutyman property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJDutyman> found by query
	 */
	public List<AdJDutyman> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJDutyman entities.
	 * 
	 * @return List<AdJDutyman> all AdJDutyman entities
	 */
	public List<AdJDutyman> findAll();
	
	/**
	 * 取得值班人员的详细信息
	 * @param worktypeCode 工作类别
	 * @return PageObject 值班人员信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getPerson(String strEnterpriseCode, String worktypeCode, final int... rowStartIdxAndCount);
}