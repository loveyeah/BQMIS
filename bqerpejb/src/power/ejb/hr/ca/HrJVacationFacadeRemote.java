package power.ejb.hr.ca;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJVacationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJVacationFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJVacation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJVacation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJVacation entity);

	/**
	 * Delete a persistent HrJVacation entity.
	 * 
	 * @param entity
	 *            HrJVacation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJVacation entity);

	/**
	 * Persist a previously saved HrJVacation entity and return it or a copy of
	 * it to the sender. A copy of the HrJVacation entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrJVacation entity to update
	 * @return HrJVacation the persisted HrJVacation entity instance, may not be
	 *         the same
	 * @throws DataChangeException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJVacation update(HrJVacation entity);
	
	public HrJVacation update1(HrJVacation entity) throws DataChangeException, ParseException ;

	public HrJVacation findById(Long id);

	/**
	 * Find all HrJVacation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJVacation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJVacation> found by query
	 */
	public List<HrJVacation> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrJVacation entities.
	 * 
	 * @return List<HrJVacation> all HrJVacation entities
	 */
	public List<HrJVacation> findAll();

	/**
	 * 查询员工请假信息
	 * @param strDeptId 部门id
	 * @param strEmpId 人员id
	 * @param strStartTime 开始时间
	 * @param strEndTime 结束时间
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 员工请假信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findEmpVacation(String strDeptId, String strEmpId,
			String strStartTime, String strEndTime, String strEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 查询假别的已用时长信息
	 * @param strEmpId 人员id
	 * @param strDeptId 部门id
	 * @param strStartTime 开始时间
	 * @param strVacationTypeId 假别id
	 * @param blnFlag 是否修改
	 * @param strVacationId 请假id
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 假别的时长信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationUsedHours(String strEmpId, String strDeptId,
			String strStartTime, String strVacationTypeId, boolean blnFlag,
			String strVacationId, String strEnterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException;

	/**
	 * 查询请假期间重复信息
	 * @param strEmpId 人员id
	 * @param strEndTime 结束时间
	 * @param strStartTime 开始时间
	 * @param strVacationTypeId 请假id
	 * @param strEnterpriseCode 企业代码
	 * @param blnFlag 是否修改
	 * @return PageObject 假别的时长信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationRepeat(String strEmpId, String strEndTime,
			String strStartTime, String strVacationId, String strEnterpriseCode,
			boolean blnFlag, final int... rowStartIdxAndCount) throws SQLException;
	/**
	 * 保存操作
	 * @param lstSaveHrJVacation 新增数据
	 * @param lstUpdateHrJVacation 修改数据
	 * @param lstDeleteHrJVacation 删除数据
	 */
	@SuppressWarnings("unchecked")
	public List save(List<HrJVacation> lstSaveHrJVacation,
			List<HrJVacation> lstUpdateHrJVacation,
			List<HrJVacation> lstDeleteHrJVacation) throws DataChangeException, SQLException;
	
	/**
	 * 检索员工销假登记信息
	 * 
	 * @param enterpriseCode
	 *            企业编码 empId 员工Id deptId  
	 * @author liuxin
	 */
	public List<employeeLeaveBean> getLeaveInfo(String enterpriseCode,
			String empId, String deptId, int... rowStartIdxAndCount);
	
	public List<employeeLeaveBean> getLeaveInfoDeptNull(String enterpriseCode,
			String empId, int... rowStartIdxAndCount);
	
	public List<DeptClearLeaveEmp> getEmpIdName(String enterpriseCode,
			String deptId);
	
	public List<DeptClearLeaveEmp> getEmpIdNameDeptNull(String enterpriseCode);
	
	/**
	 * 获取请假统计报表详细信息
	 * 
	 * @param enterpriseCode
	 *            企业编码  
	 * @param year   
	 *            统计年份 
	 * @param month  
	 *            统计月份    
	 * @return 
	 *           PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAttendentStaticDetailInfo(String enterpriseCode,String year,String month);

	 /**
     * 查询部门员工信息
     * @param lgnDeptId 部门id
     * @param lgnEmpId 人员id
     * @param strEnterpriseCode 企业代码
     * @return PageObject 部门员工信息
     * @throws SQLException
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public PageObject getDeptEmpInfo(Long lgnDeptId, Long lgnEmpId, String strEnterpriseCode)
	    throws SQLException;
    
    /**
	 * 获取加班统计报表详细信息
	 * 
	 * @param enterpriseCode
	 *            企业编码  
	 * @param year   
	 *            统计年份 
	 * @param month  
	 *            统计月份    
	 * @return 
	 *           PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkOvertimeStaticDetailInfo(String enterpriseCode,String year,String month);
	
	/**
	 * 获取运行班统计报表详细信息
	 * 
	 * @param enterpriseCode
	 *            企业编码  
	 * @param year   
	 *            统计年份 
	 * @param month  
	 *            统计月份    
	 * @return 
	 *           PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkShiftStaticDetailInfo(String enterpriseCode,String year,String month);
	/**
	 * 请假查询登记，查询信息
	 * 
	 * @param enterpriseCode
	 *            企业编码  
	 * @param startTime   
	 *            请假开始日期 
	 * @param endTime  
	 *            请假结束日期    
	 * @return 
	 *           PageObject
	 */
	public PageObject getAllVacations(String enterpriseCode,String startTime,String endTime,final int... rowStartIdxAndCount)throws SQLException;
	
	/**
	 * 获取全部假别
	 * 
	 * @param enterpriseCode 企业代码
	 * @return  PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getVacationType(String enterpriseCode);
	
	/**
	 * 获取全部加班类别
	 * 
	 * @param enterpriseCode 企业代码
	 * @return  PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkOvertimeType(String enterpriseCode);
	
	/**
	 * 获取全部运行班类别
	 * 
	 * @param enterpriseCode 企业代码
	 * @return  PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkShiftType(String enterpriseCode);
}