package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for HrCAttendancedepFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCAttendancedepFacadeRemote {
	
	/**
	 * 判断考勤部门是否重复
	 * add by sychen 20100716
	 * @param attendanceDeptName
	 * @param id
	 * @param enterpriseCode
	 * @return
	 */
	public boolean checkAttendanceDeptName(String attendanceDeptName,String id,String enterpriseCode) ;
	
	public void save(HrCAttendancedep entity) throws SQLException;

	
	public void delete(HrCAttendancedep entity) throws RuntimeException, SQLException;

	
	public void update(HrCAttendancedep entity) throws SQLException;
	public List<TreeNode> getDeptsByTopDeptid(Long pid,String enterpriseCode);
	public List<TreeNode> getDeptsByLogPeople(Long pid,Long empId,String enterpriseCode);//modify by wpzhu 20100714  增加根据登录人查找所负责的部门
	/**
	 * 通过考勤部门ID取得部门及相关信息
	 * @param pid
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptsByAttendanceDeptid(String pid,String enterpriseCode);
	
	/**
	 * 查找该当部门下所有的子部门
	 * @param classNo
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findAllChildrenDept(String classNo, String enterpriseCode);

	/**
	 * 查找该当部门下所有的子部门不包括自身
	 * @param classNo
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findAllChildrenDeptInfo(String classNo, String enterpriseCode);
	/**
	 * 查找考勤登记人或考勤审核人为制定人的所有信息
	 * @param classNo
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findDataByWriterCheckerId(Long empId, String enterpriseCode);
	public HrCAttendancedep findById(Long id);

	

	public List<HrCAttendancedep> findByAttendanceDeptId(Object attendanceDeptId);

	

	/**
	 * Find all HrCAttendancedep entities.
	 * 
	 * @return List<HrCAttendancedep> all HrCAttendancedep entities
	 */
	public List<HrCAttendancedep> findAll();

	/**
	 * 查询系统月份上下班时间
	 * @param strEmpId 人员id
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 系统月份上下班时间
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findSysOnDutyTime(String strEmpId, String strEnterpriseCode) throws SQLException;

	/**
	 * 查询参数年的上下班时间
	 * @param strEmpId 人员id
	 * @param strDate 日期
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 系统月份上下班时间
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findOnDutyTime(String strEmpId, String strDate,
			String strEnterpriseCode) throws SQLException;
	/**
	 * 查询考勤部门名称
	 * @param strEmpId 人员id
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 考勤部门名称
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAttendanceDept(String strEmpId, String strEnterpriseCode)
	throws SQLException;

	/**
	 * 查询开始时间和结束时间之间的中间年度
	 * @param strEmpId 人员id
	 * @param strStartDate 开始日期
	 * @param strEndDate 结束日期
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 系统月份上下班时间
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findBetYear(String strEmpId, String strStartDate,
			String strEndDate, String strEnterpriseCode) throws SQLException;
	
	/**
	 * 查询考勤部门表的叶子节点（用于考勤登记页面）
	 * add by fyyang 20100705
	 * @param loginId
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getAttendanceDeptForRegister(String loginId,
			String enterpriseCode);
	
	/**
	 * 考勤部门管理<考勤人员变更>删除考勤部门人员方法
	 * add by sychen 20100713
	 * @param ids
	 */
	public void deleteAttendanceDeptId(String ids) ;
}