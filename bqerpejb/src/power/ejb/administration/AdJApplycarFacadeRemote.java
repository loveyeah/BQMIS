package power.ejb.administration;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJApplycarFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJApplycarFacadeRemote {
	/**
	 * 增加用车申请信息
	 */
	public void save(AdJApplycar entity) throws SQLException;

	/**
	 * Delete a persistent AdJApplycar entity.
	 * 
	 * @param entity
	 *            AdJApplycar entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJApplycar entity);

	/**
	 *  修改用车申请信息
	 * @param entity
	 * @throws SQLException
	 */
	public AdJApplycar update(AdJApplycar entity) throws SQLException;

	/**
	 *  修改用车申请信息
	 * @param entity 用车申请实体
	 * @param strUpdateTime 用车申请实体取得时的更新时间
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public void update(AdJApplycar entity, Long updateTime) throws SQLException, DataChangeException;

	public AdJApplycar findById(Long id) throws SQLException;

	/**
	 * Find all AdJApplycar entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJApplycar property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJApplycar> found by query
	 */
	public List<AdJApplycar> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJApplycar entities.
	 * 
	 * @return List<AdJApplycar> all AdJApplycar entities
	 */
	public List<AdJApplycar> findAll();

	/**
	 * 从申请单号查询派车单
	 *
	 * @param strApplyNo 申请单号
	 * @param strEnterpriseCode 企业代码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 派车单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByApplyNo(String strApplyNo, String strEnterpriseCode, final int... rowStartIdxAndCount)
	throws ParseException;
	/**
	 *  查询用车申请信息
	 * 
     */
	public PageObject findCarApply(String workerCode,String enterpriseCode,final int... rowStartIdxAndCount)
		throws SQLException;
	/**
	 *  删除用车申请信息
	 *  @throws SQLException
     */
	public void delete(Long id,String workerCode)throws SQLException;
	/**
	 *  用车管理：删除用车申请信息
	 *  @throws SQLException
	 *  @throws DataChangeException
     */
	public void delete(Long id,String workerCode, Long updateTime)throws SQLException, DataChangeException;
	/**
	 *  上报用车申请
     */
	public void updateState(Long id,String updater) throws SQLException;


	/**
	 * 从查询用车申请
	 *
	 * @param strStartDate 用车开始时间
	 * @param strEndDate 用车结束时间
	 * @param strdeptCode 用车部门
	 * @param strWorkerCode 申请人
	 * @param strDriver 司机
	 * @param strDcmStatus 上报状态
	 * @param strEnterpriseCode 企业代码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 派车单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getApplyCarInfo(String strStartDate,String strEndDate,String strDeptCode,String strWorkerCode,
			String strDriver, String strDcmStatus, String strEnterpriseCode,
	        final int ...rowStartIdxAndCount) throws SQLException;
	
	public PageObject findCarApplyBy(String strStartDate,String strEndDate,
            String strDepCode,String strWorkerCode,
            String strDriverCode,String strdrpCarStatus,String enterpriseCode,
            final int ...rowStartIdxAndCount) throws SQLException;
}