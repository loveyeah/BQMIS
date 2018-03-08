package power.ejb.administration;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ejb.administration.form.VisitRegisterInfo;

/**
 * Remote interface for AdJManpassFacade.
 * 
 * @author daichunlin
 */
@Remote
public interface AdJManpassFacadeRemote {
	/**	
	 * 来访人员登记表
	 * @param entity
	 *            AdJManpass entity to persist
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJManpass entity) throws SQLException;

	/**
	 * 来访人员登记删除
	 * @param strEmployee  更新者
	 * @param lngId  序号
	 * @param strUpdateTime 排他时间
	 */
	public void delete(String strEmployee, Long lngId, String strUpdateTime)
			throws DataChangeException, SQLException;

	/**
	 * 来访人员登记修改
	 * @param entity 来访人员登记表	
	 * @param strLastmodifyTime 排他时间
	 */
	public void update(AdJManpass entity, String strLastmodifyTime)
	throws DataChangeException, SQLException;

	public AdJManpass findById(Long id);

	/**
	 * Find all AdJManpass entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJManpass property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJManpass> found by query
	 */
	public List<AdJManpass> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJManpass entities.
	 * 
	 * @return List<AdJManpass> all AdJManpass entities
	 */
	public List<AdJManpass> findAll();
}