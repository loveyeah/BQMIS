package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJDepstationcorrespondFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJDepstationcorrespondFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJDepstationcorrespond
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrJDepstationcorrespond entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJDepstationcorrespond entity);

	/**
	 * Delete a persistent HrJDepstationcorrespond entity.
	 * 
	 * @param entity
	 *            HrJDepstationcorrespond entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJDepstationcorrespond entity);

	/**
	 * Persist a previously saved HrJDepstationcorrespond entity and return it
	 * or a copy of it to the sender. A copy of the HrJDepstationcorrespond
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJDepstationcorrespond entity to update
	 * @return HrJDepstationcorrespond the persisted HrJDepstationcorrespond
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJDepstationcorrespond update(HrJDepstationcorrespond entity);

	public HrJDepstationcorrespond findById(Long id);

	/**
	 * Find all HrJDepstationcorrespond entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJDepstationcorrespond property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJDepstationcorrespond> found by query
	 */
	public List<HrJDepstationcorrespond> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJDepstationcorrespond entities.
	 * 
	 * @return List<HrJDepstationcorrespond> all HrJDepstationcorrespond
	 *         entities
	 */
	public List<HrJDepstationcorrespond> findAll();
	/**
	 *  根据部门查找岗位
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByDeptId(String deptId,String enterpriseCode) throws SQLException;

	/**
	 *  班组联动查询  add by sychen 20100721
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	 public PageObject getBeforeBanZuList(String deptId,String enterpriseCode) throws SQLException;
	
	/**
	 * 查询部门岗位信息
	 * @param strDeptID 部门id
	 * @param strEmpID 人员id
	 * @param strEnterpriseCode 企业代码
	 * @return 部门岗位信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findStationMaintain(String strDeptID,String strEmpID,
			String strEnterpriseCode, final int... rowStartIdxAndCount)
		throws SQLException;
}