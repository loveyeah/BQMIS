/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCYearplanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCYearplanFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCYearplan entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCYearplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCYearplan entity);

	/**
	 * Delete a persistent HrCYearplan entity.
	 * 
	 * @param entity
	 *            HrCYearplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCYearplan entity);

	/**
	 * Persist a previously saved HrCYearplan entity and return it or a copy of
	 * it to the sender. A copy of the HrCYearplan entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCYearplan entity to update
	 * @return HrCYearplan the persisted HrCYearplan entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCYearplan update(HrCYearplan entity);

	public HrCYearplan findById(HrCYearplanId id);

	/**
	 * Find all HrCYearplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCYearplan property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCYearplan> found by query
	 */
	public List<HrCYearplan> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCYearplan entities.
	 * 
	 * @return List<HrCYearplan> all HrCYearplan entities
	 */
	public List<HrCYearplan> findAll();
	 /**
     * 查询某一部门下的所有员工
     * @param enterpriseCode 企业编码 ,year 年份,deptId 部门id
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
	public HrCYearPlanFields findAllVacation(String year ,String deptId,boolean isSelectAllDept,String enterpriseCode) throws SQLException;
	 /**
     * 查询某一员工在加班登记表中上一年的总共换休时间
     * @param enterpriseCode 企业编码 ,empId 员工ID ,year 上一年份
     * @return result  查询结果
     */
	public double findExchangeTime(String lastYear ,String enterpriseCode,String empId) throws SQLException;
	/**
     * 查询某一部门的标准出勤时间
     * @param enterpriseCode 企业编码 ,empId 员工ID ,year 上一年份
     * @return result  查询结果
     */
	public double findStandardTime(String year ,String enterpriseCode,String empId) throws SQLException;

	/**
	 * 查询假别的时长信息
	 * @param strEmpId 人员id
	 * @param strStartTime 开始时间
	 * @param strVacationTypeId 假别id
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 假别的时长信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationHours(String strEmpId,
			String strStartTime, String strVacationTypeId, String strEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;
	
	 /**
     * 根据逻辑主键查询数据信息
     * @param  empId 员工ID ,year 年份，假别id,enterpriseCode 企业编码
     * @return result  查询结果
     */
	public List<HrCYearplan> searchMsgByLogicId(String year ,String empId,String vacationTypeId,String enterpriseCode) throws SQLException;
	
	/**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return void  
     * @throws SQLException
     */
    public void updateYearPlan(HrCYearplan entity)throws SQLException ;
    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws SQLException
     * @return void  
     */
    public void addYearPlan(HrCYearplan entity)throws SQLException;
    /**
     * 上报一条数据
     *
     * @param mapList 要上报的记录
     * @throws SQLException
     * @return void  
     */
    public void reportYearPlan( Map<String, Object> mapList )throws SQLException;
    /**
     * 增加和插入数据 
     *
     * @param updateList  要修改的记录
     * @param insertList  要插入的记录
     * @return void  
     * @throws SQLException
     */
    public void updateAndInsertYearPlan(List<HrCYearplan> updateList,List<HrCYearplan> insertList)throws SQLException;
    /**
     * 保存上报数据
     *
     * @param reportList 要上报的记录
     * @param saveList 要保存的纪录
     * @throws SQLException
     * @return void  
     */
    @SuppressWarnings("unchecked")
	public void saveAndReportYearPlan(List<Map<String, Object>> reportList,List<List> saveList) throws SQLException;
}