package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJEmpStationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJEmpStationFacadeRemote {

	/**
	 * 新增职工岗位
	 * 
	 * @param entity 职工岗位
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmpStation entity) throws SQLException;

	/**
	 * 新增职工岗位(多次增加时用)
	 *
	 * @param entity 职工岗位
	 * @param argId 上次增加记录的流水号
	 * @return Long 增加后记录的流水号
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Long save(HrJEmpStation entity, Long argId) throws SQLException;
	
	/**
	 * Delete a persistent HrJEmpStation entity.
	 * 
	 * @param entity
	 *            HrJEmpStation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmpStation entity);

	/**
	 * 修改职工岗位
	 * 
	 * @param entity 职工岗位
	 * @return HrJEmpStation 修改后的职工岗位
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEmpStation update(HrJEmpStation entity)
			throws SQLException, DataChangeException;

	public HrJEmpStation findById(Long id);

	/**
	 * Find all HrJEmpStation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmpStation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEmpStation> found by query
	 */
	public List<HrJEmpStation> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrJEmpStation entities.
	 * 
	 * @return List<HrJEmpStation> all HrJEmpStation entities
	 */
	public List<HrJEmpStation> findAll();

	/**
	 * 查询职工岗位信息
	 * @param strEmpID 人员id
	 * @param strEnterpriseCode 企业代码
	 * @return 职工岗位信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findEmpStationInfo(String strEmpID,
			String strEnterpriseCode, final int... rowStartIdxAndCount)
		throws SQLException;
	
	/**
	 * 查询职工岗位信息
	 * 
	 * @param empId 人员id
	 * @param enterpriseCode 企业代码
	 * @return PageObject
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findEmpStationInfoByEmpId(String empId,
			String enterpriseCode) throws SQLException;
}