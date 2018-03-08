package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * 定期工作登记表
 * 
 * @author daichunlin
 */
@Remote
public interface AdJTimeworkFacadeRemote {
	/**
	 * 定期工作登记表插入数据
	 * 
	 * @param entity
	 *            定期工作登记表
	 * @throws RuntimeException
	 */
	public void save(AdJTimework entity);

	/**
	 * 定期工作登记表删除数据
	 * 
	 * @param strEmployee
	 *            更新者
	 * @param lngId
	 *            序号
	 * @throws RuntimeException
	 */
	public void delete(String strEmployee, Long lngId, String strUpdateTime)
			throws DataChangeException, SQLException;

	/**
	 * 定期工作登记表更新数据
	 * 
	 * @param entity
	 *            定期工作登记表
	 * @return AdJTimework
	 * @throws RuntimeException
	 */
	public AdJTimework update(AdJTimework entity);

	public AdJTimework findById(Long id);

	/**
	 * Find all AdJTimework entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJTimework property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJTimework> found by query
	 */
	public List<AdJTimework> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJTimework entities.
	 * 
	 * @return List<AdJTimework> all AdJTimework entities
	 */
	public List<AdJTimework> findAll();

	/**
	 * 定期工作登记表更新数据
	 * 
	 * @param entity
	 *            定期工作登记表
	 * @return AdJTimework 时间
	 * @throws RuntimeException
	 */
	public void update(AdJTimework entity, String strLastmodifyTime)
			throws DataChangeException, SQLException;
}