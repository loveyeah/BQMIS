package power.ejb.administration;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJEmployeesignpictureFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJEmployeesignpictureFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJEmployeesignpicture
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            AdJEmployeesignpicture entity to persist
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJEmployeesignpicture entity) throws SQLException;

	/**
	 * Delete a persistent AdJEmployeesignpicture entity.
	 * 
	 * @param entity
	 *            AdJEmployeesignpicture entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJEmployeesignpicture entity);

	/**
	 * Persist a previously saved AdJEmployeesignpicture entity and return it or
	 * a copy of it to the sender. A copy of the AdJEmployeesignpicture entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            AdJEmployeesignpicture entity to update
	 * @return AdJEmployeesignpicture the persisted AdJEmployeesignpicture
	 *         entity instance, may not be the same
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJEmployeesignpicture update(AdJEmployeesignpicture entity) throws SQLException;

	public AdJEmployeesignpicture findById(Long id);
	
	/**
	 * Find all AdJEmployeesignpicture entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJEmployeesignpicture property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJEmployeesignpicture> found by query
	 */
	public List<AdJEmployeesignpicture> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all AdJEmployeesignpicture entities.
	 * 
	 * @return List<AdJEmployeesignpicture> all AdJEmployeesignpicture entities
	 */
	public List<AdJEmployeesignpicture> findAll();

	/**
	 * 从人员编码查询个性签名
	 *
	 * @param strWorkCode 人员编码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 派车单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByWorkCode(String strWorkCode, final int... rowStartIdxAndCount)
	throws ParseException;
}