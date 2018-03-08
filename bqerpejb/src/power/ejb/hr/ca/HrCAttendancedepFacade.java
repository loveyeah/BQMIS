package power.ejb.hr.ca;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;
import power.ejb.hr.labor.HrCLaborMaterial;

/**
 * Facade for entity HrCAttendancedep.
 * 
 * @see power.ejb.hr.ca.HrCAttendancedep
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCAttendancedepFacade implements HrCAttendancedepFacadeRemote {
	// property constants
	public static final String ATTENDANCE_DEPT_ID = "attendanceDeptId";
	public static final String ATTENDANCE_DEPT_NAME = "attendanceDeptName";
	public static final String ATTEND_DEP_TYPE = "attendDepType";
	public static final String TOP_CHECK_DEP_ID = "topCheckDepId";
	public static final String REPLACE_DEP_ID = "replaceDepId";
	public static final String ATTEND_WRITER_ID = "attendWriterId";
	public static final String ATTEND_CHECKER_ID = "attendCheckerId";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**是否使用*/
	private String IS_USE_Y = "Y";
	
	/**
	 * 判断考勤部门是否重复
	 * add by sychen 20100716
	 */
	public boolean checkAttendanceDeptName(String attendanceDeptName,String id,String enterpriseCode) {
		String st = "SELECT t.attendance_dept_id\n" +
			"  FROM HR_C_ATTENDANCEDEP t\n" + 
			" WHERE t.attendance_dept_name = '"+attendanceDeptName+"'\n" + 
			"   AND t.is_use = 'Y'\n" + 
			"   AND T.ENTERPRISE_CODE = '"+enterpriseCode+"'\n";

		if(id != null)
			st += " and t.ATTENDANCE_DEPT_ID <>" + id;
		int a = bll.exeNativeSQL(st);
		if (a > 0) {
			return true;
		} else
			return false;
	}
	
	public void save(HrCAttendancedep entity) throws SQLException {
		LogUtil.log("EJB:考勤部门维护保存开始", Level.INFO, null);
		try {
			 // 取得最大的id
			if(entity.getId() == null) {
				entity.setId(bll.getMaxId("HR_C_ATTENDANCEDEP", "ID"));
			}
			if(entity.getAttendanceDeptId() == null) {
				entity.setAttendanceDeptId(bll.getMaxId("HR_C_ATTENDANCEDEP", "ATTENDANCE_DEPT_ID"));
			}
            // 设置最后修改时间
            entity.setLastModifiyDate(new Date());
            entityManager.persist(entity);
            LogUtil.log("EJB:考勤部门维护保存结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:考勤部门维护保存失败", Level.INFO, null);
			throw new SQLException(re.toString());
		}
	}

	
	public void delete(HrCAttendancedep entity) throws RuntimeException, SQLException{
		LogUtil.log("EJB:考勤部门维护信息删除开始", Level.INFO, null);
		try {
            // 设置最后修改时间
            entity.setLastModifiyDate(new Date());
            entityManager.merge(entity);
            LogUtil.log("EJB:考勤部门维护信息删除结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:考勤部门维护信息删除失败", Level.INFO, null);
			throw new SQLException(re.toString());
		}
	}

	
	public void update(HrCAttendancedep entity) throws SQLException{
		LogUtil.log("EJB:修改考勤部门维护信息开始", Level.INFO, null);
		try {
            // 设置最后修改时间
            entity.setLastModifiyDate(new Date());
            entityManager.merge(entity);
            LogUtil.log("EJB:修改考勤部门维护信息结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:修改考勤部门维护信息失败", Level.SEVERE, null);
			throw new SQLException(re.toString());
		}
	}

	/**
	 * 通过上级审核部门取得考勤部门
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> getDeptsByTopDeptid(Long pid,String enterpriseCode)
	{
		List<TreeNode> result = null;
		StringBuilder sbr = new StringBuilder();
			sbr.append("select distinct t.ATTENDANCE_DEPT_ID, ");
			sbr.append("t.ATTENDANCE_DEPT_NAME, ");
			sbr.append("t.ATTEND_DEP_TYPE, ");
			sbr.append("(select decode(count(1), 0, 'Y', 'N') ");
			sbr.append("from HR_C_ATTENDANCEDEP d ");
			sbr.append(" where d.TOP_CHECK_DEP_ID = t.ATTENDANCE_DEPT_ID ");
			sbr.append(" and d.is_use = ? ");
			sbr.append("and d.ENTERPRISE_CODE = ? ) ");
			sbr.append("from HR_C_ATTENDANCEDEP t ");
			sbr.append(" where t.TOP_CHECK_DEP_ID = ? ");
			sbr.append("and t.is_use = ? ");
			sbr.append("and t.ENTERPRISE_CODE = ? ");
			sbr.append("order by t.ATTENDANCE_DEPT_ID ");
		List list = bll.queryByNativeSQL(sbr.toString(), new Object[]{"Y",enterpriseCode,pid,"Y",enterpriseCode});
		LogUtil.log("EJB-getDeptsByTopDeptid:SQL=" + sbr.toString(), Level.INFO, null);
		LogUtil.log("EJB:para=" + pid, Level.INFO, null);
		if(list != null && list.size()>0)
		{
			result = new ArrayList<TreeNode>();
			for(int i=0;i<list.size();i++)
			{
				Object[] r =(Object[])list.get(i);
				TreeNode node = new TreeNode();
				node.setId(r[0].toString());
				if(r[1] !=null)
				node.setText(r[1].toString());
				if(r[2] != null)
				node.setDescription(r[2].toString());
				node.setCls(r[3].toString().equals("Y")?"file":"folder");
				node.setLeaf(r[3].toString().equals("Y")?true:false);
				result.add(node);
			}
		}

		return result;
	}
	//add by wpzhu 20100714
	public List<TreeNode> getDeptsByLogPeople(Long pid,Long empId,String enterpriseCode)
	{


		
		List<TreeNode> result = null;
		StringBuilder sbr = new StringBuilder();
			sbr.append("select distinct  t.ATTENDANCE_DEPT_ID, ");
			sbr.append("t.ATTENDANCE_DEPT_NAME, ");
			sbr.append("t.ATTEND_DEP_TYPE, ");
			sbr.append("(select decode(count(1), 0, 'Y', 'N') ");
			sbr.append("from HR_C_ATTENDANCEDEP d ");
			sbr.append(" where d.TOP_CHECK_DEP_ID = t.ATTENDANCE_DEPT_ID ");
			sbr.append(" and d.is_use = 'Y' ");
			sbr.append("and d.ENTERPRISE_CODE = '"+enterpriseCode+"' ) ");
			sbr.append("from HR_C_ATTENDANCEDEP t ");
			sbr.append(" where t.TOP_CHECK_DEP_ID = '"+pid+"' ");
			sbr.append("and t.is_use = 'Y' ");
			sbr.append("and t.ENTERPRISE_CODE =  '"+enterpriseCode+"' ");
//			sbr.append("and t.attend_writer_id=?");
			//update by sychen 20100724
			if(empId!=null)
			{
			sbr.append("AND GETFirstLevelBYID((SELECT i.dept_id\n" +
					"                                     FROM hr_j_emp_info i\n" + 
					"                                    WHERE i.emp_id = t.attend_writer_id)) =\n" + 
					"                 GETFirstLevelBYID((SELECT i.dept_id\n" + 
					"                                     FROM hr_j_emp_info i\n" + 
					"                                    WHERE i.emp_id = "+empId+"))");
			//update by sychen 20100724 end
			}
			sbr.append("order by t.ATTENDANCE_DEPT_ID ");
			
		List list = bll.queryByNativeSQL(sbr.toString());
//		LogUtil.log("EJB-getDeptsByTopDeptid:SQL=" + sbr.toString(), Level.INFO, null);
//		LogUtil.log("EJB:para=" + pid, Level.INFO, null);
		if(list != null && list.size()>0)
		{
			result = new ArrayList<TreeNode>();
			for(int i=0;i<list.size();i++)
			{
				Object[] r =(Object[])list.get(i);
				TreeNode node = new TreeNode();
				node.setId(r[0].toString());
				if(r[1] !=null)
				node.setText(r[1].toString());
				if(r[2] != null)
				node.setDescription(r[2].toString());
				node.setCls(r[3].toString().equals("Y")?"file":"folder");
				node.setLeaf(r[3].toString().equals("Y")?true:false);
				result.add(node);
			}
		}

		return result;
	}
	

	/**
	 * 通过考勤部门ID取得部门及相关信息
	 * @param pid
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptsByAttendanceDeptid(String pid,String enterpriseCode) {
	LogUtil.log("EJB:通过考勤部门ID取得部门及相关信息开始。",
                Level.INFO, null);
        try {
			PageObject object = new PageObject();
			StringBuilder sbr = new StringBuilder();
			sbr.append(" SELECT A.ATTENDANCE_DEPT_ID as attdanceDeptId");
			sbr.append(" ,A.ATTENDANCE_DEPT_NAME as attendanceDeptName");
			sbr.append(" ,A.ATTEND_WRITER_ID as attendWriterId ");
			sbr.append(" ,C.CHS_NAME  as attendWriterName ");
			sbr.append(" ,A.TOP_CHECK_DEP_ID as topCheckDepId ");
			sbr.append(" ,B.ATTENDANCE_DEPT_NAME as topCheckDepName ");
			sbr.append(" ,A.REPLACE_DEP_ID as replaceDepId ");
			sbr.append("  ,D.ATTENDANCE_DEPT_NAME as attdanceDeptName ");
			sbr.append(" ,A.ATTEND_DEP_TYPE as attendDepType ,A.ID  as id ");
			sbr.append(" ,A.ATTEND_CHECKER_ID as attendCheckerId ,E.CHS_NAME as chsName,to_char(A.LAST_MODIFIY_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifyDate ");
			sbr.append(" FROM HR_C_ATTENDANCEDEP A ");
			sbr.append(" LEFT JOIN HR_J_EMP_INFO C ON A.ATTEND_WRITER_ID = C.EMP_ID ");
			sbr.append(" AND C.ENTERPRISE_CODE = ? ");
			// modify by liuyi 090916 表中无该属性
//			sbr.append(" AND C.IS_USE = ? ");
			sbr.append(" LEFT JOIN HR_J_EMP_INFO E ON A.ATTEND_CHECKER_ID = E.EMP_ID ");
			sbr.append(" AND E.ENTERPRISE_CODE = ? ");
			// modify by liuyi 090916 表中无该属性
//			sbr.append(" AND E.IS_USE = ? ");
			sbr.append(" LEFT JOIN HR_C_ATTENDANCEDEP B ON  ");
			sbr.append(" B.ATTENDANCE_DEPT_ID = A.TOP_CHECK_DEP_ID ");
			sbr.append("  AND B.ENTERPRISE_CODE = ?");
			sbr.append(" AND B.IS_USE = ? ");
			sbr.append(" LEFT JOIN HR_C_ATTENDANCEDEP D ON  ");
			sbr.append(" D.ATTENDANCE_DEPT_ID = A.REPLACE_DEP_ID ");
			sbr.append(" AND D.ENTERPRISE_CODE = ? ");
			sbr.append(" AND D.IS_USE = ?");
			sbr.append(" WHERE A.ATTENDANCE_DEPT_ID = ? ");
			sbr.append(" AND A.ENTERPRISE_CODE = ? ");
			sbr.append(" AND A.IS_USE = ? ");
				
			List list = bll.queryByNativeSQL(sbr.toString(), new Object[]{enterpriseCode,enterpriseCode,
				enterpriseCode,IS_USE_Y,
				enterpriseCode,IS_USE_Y,pid,enterpriseCode,IS_USE_Y});
			LogUtil.log("EJB-getDeptsByAttendanceDeptid:SQL=" + sbr.toString(), Level.INFO, null);
			LogUtil.log("EJB:para=" + pid, Level.INFO, null);
			Iterator it = list.iterator();
	    	List<AttendancedepFollowInfo> arrlist = new ArrayList<AttendancedepFollowInfo>();
	        while (it.hasNext()) {
	        	AttendancedepFollowInfo info = new AttendancedepFollowInfo();
	        	Object[] data = (Object[]) it.next();
	        	if(null != data[0]) {
	        		info.setAttendanceDeptId(data[0].toString());
	        	}
	        	if(null != data[1]) {
	        		info.setAttendanceDeptName(data[1].toString());
	        	}
	        	if(null != data[2]) {
	        		info.setAttendWriterId(data[2].toString());
	        	}
	        	if(null != data[3]) {
	        		info.setAttendWriterName(data[3].toString());
	        	}
	        	if(null != data[4]) {
	        		info.setTopCheckDepId(data[4].toString());
	        	}
	        	if(null != data[5]) {
	        		info.setTopCheckDepName(data[5].toString());
	        	}
	        	if(null != data[6]) {
	        		info.setReplaceDepId(data[6].toString());
	        	}
	        	if(null != data[7]) {
	        		info.setReplaceDepName(data[7].toString());
	        	}
	        	if(null != data[8]) {
	        		info.setAttendDepType(data[8].toString());
	        	}
	        	if(null != data[9]) {
	        		info.setId(data[9].toString());
	        	}
	        	if(null != data[10]) {
	        		info.setAttendCheckerId(data[10].toString());
	        	}
	        	if(null != data[11]) {
	        		info.setAttendCheckerName(data[11].toString());
	        	}
	        	if(null != data[12]) {
	        		info.setLastModifyDate(data[12].toString());
	        	}
	        	arrlist.add(info);
	        }
		        object.setList(arrlist);
		        object.setTotalCount((long)arrlist.size());
		        LogUtil.log("EJB:通过考勤部门ID取得部门及相关信息结束。", Level.INFO, null);
		        return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:通过考勤部门ID取得部门及相关信息失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查找该当部门下所有的子部门
	 * @param classNo
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findAllChildrenDept(String classNo, String enterpriseCode){
    	LogUtil.log("finding child dept with attendance: " + classNo,
                Level.INFO, null);
    	try{
    		// 查询sql
    		String sql = 
    			"SELECT * \n" + 
    			"FROM HR_C_ATTENDANCEDEP \n" +
    			"WHERE is_use = 'Y' AND enterprise_code = '" + enterpriseCode +"'\n" +
    			"AND ATTENDANCE_DEPT_ID IN \n" +
    			"(SELECT ATTENDANCE_DEPT_ID \n" +
    			"FROM HR_C_ATTENDANCEDEP START WITH TOP_CHECK_DEP_ID = '" + classNo + "' and is_use = 'Y' \n" +
    			" AND enterprise_code = '" + enterpriseCode +"'\n" +
    			"CONNECT BY PRIOR ATTENDANCE_DEPT_ID = TOP_CHECK_DEP_ID AND IS_USE = 'Y'\n" +
    			"UNION \n" + 
    			" SELECT ATTENDANCE_DEPT_ID \n" +
    			" FROM HR_C_ATTENDANCEDEP \n" +
    			" WHERE ATTENDANCE_DEPT_ID = '" + classNo + "' and is_use = 'Y' " + " AND enterprise_code = '" +
    			enterpriseCode +"'\n" + " )";
    		// 执行查询
            List<HrCAttendancedep> list = bll.queryByNativeSQL(sql, HrCAttendancedep.class);
            // 返回
            return list;
    	}catch(Exception e)
    	{
    		LogUtil.log("find failed", Level.SEVERE, e);
    		return null;
    	}    	
    }
	
	/**
	 * 查找该当部门下所有的子部门不包括自身
	 * @param classNo
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findAllChildrenDeptInfo(String classNo, String enterpriseCode){
    	LogUtil.log("finding child dept with attendance: " + classNo,
                Level.INFO, null);
    	try{
    		// 查询sql
    		String sql = 
    			"SELECT * \n" + 
    			"FROM HR_C_ATTENDANCEDEP \n" +
    			"WHERE is_use = 'Y' AND enterprise_code = '" + enterpriseCode +"'\n" +
    			"AND ATTENDANCE_DEPT_ID IN \n" +
    			"(SELECT ATTENDANCE_DEPT_ID \n" +
    			"FROM HR_C_ATTENDANCEDEP START WITH TOP_CHECK_DEP_ID = '" + classNo + "' and is_use = 'Y' \n" +
    			" AND enterprise_code = '" + enterpriseCode +"'\n" +
    			"CONNECT BY PRIOR ATTENDANCE_DEPT_ID = TOP_CHECK_DEP_ID AND IS_USE = 'Y' " + 
    			" AND enterprise_code = '" + enterpriseCode +"') \n ";
    		// 执行查询
            List<HrCAttendancedep> list = bll.queryByNativeSQL(sql, HrCAttendancedep.class);
            // 返回
            return list;
    	}catch(Exception e)
    	{
    		LogUtil.log("find failed", Level.SEVERE, e);
    		return null;
    	}    	
    }
	
	/**
	 * 查找考勤登记人或考勤审核人为制定人的所有信息
	 * @param empId 考勤登记人或者考勤审核人
	 * @param enterpriseCode 企业编码
	 * @return obj
	 */
	@SuppressWarnings("unchecked")
	public PageObject findDataByWriterCheckerId(Long empId, String enterpriseCode){
    	try{
    		LogUtil.log("EJB:查找考勤登记人或考勤审核人为制定人的所有信息开始", Level.INFO,null);
			StringBuilder sbr = new StringBuilder();
			PageObject obj = new PageObject();
			sbr.append(" SELECT * ");
			sbr.append(" FROM HR_C_ATTENDANCEDEP A ");
			sbr.append(" WHERE A.ATTEND_WRITER_ID = ? ");
			sbr.append(" AND A.IS_USE = ? ");
			sbr.append(" AND A.ENTERPRISE_CODE = ? ");
			sbr.append(" UNION ");
			sbr.append(" SELECT * ");
			sbr.append(" FROM HR_C_ATTENDANCEDEP B ");
			sbr.append(" WHERE B.ATTEND_CHECKER_ID = ? ");
			sbr.append("  AND B.IS_USE = ? ");
			sbr.append(" AND B.ENTERPRISE_CODE = ? ");
					
    		// 执行查询
            List<HrCAttendancedep> list = bll.queryByNativeSQL(sbr.toString(), new Object[]{empId,IS_USE_Y,enterpriseCode,
            	empId,IS_USE_Y,enterpriseCode},HrCAttendancedep.class);
            LogUtil.log("EJB-findDataByWriterCheckerId:SQL=" + sbr.toString(), Level.INFO, null);
            LogUtil.log("EJB:查找考勤登记人或考勤审核人为制定人的所有信息结束", Level.INFO,null);
            // 返回
            obj.setList(list);
            obj.setTotalCount((long)list.size());
            return obj;
    	}catch(Exception e)
    	{
    		LogUtil.log("EJB:查找考勤登记人或考勤审核人为制定人的所有信息失败", Level.SEVERE,null);
    		return null;
    	}    	
    }
	public HrCAttendancedep findById(Long id) {
		LogUtil.log("finding HrCAttendancedep instance with id: " + id,
				Level.INFO, null);
		try {
			HrCAttendancedep instance = entityManager.find(
					HrCAttendancedep.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<HrCAttendancedep> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCAttendancedep instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCAttendancedep model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCAttendancedep> findByAttendanceDeptId(Object attendanceDeptId) {
		return findByProperty(ATTENDANCE_DEPT_ID, attendanceDeptId);
	}

	@SuppressWarnings("unchecked")
	public List<HrCAttendancedep> findAll() {
		LogUtil.log("finding all HrCAttendancedep instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCAttendancedep model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询系统月份上下班时间
	 * @param strEmpId 人员id
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 系统月份上下班时间
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findSysOnDutyTime(String strEmpId, String strEnterpriseCode) throws SQLException {
		LogUtil.log("EJB:系统月份上下班时间查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("A.AM_BEGING_TIME , ");
			sbd.append("A.PM_END_TIME ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_C_ATTENDANCESTANDARD A, ");
			sbd.append("HR_J_EMP_INFO B ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND B.ENTERPRISE_CODE = ? ");
			sbd.append("AND B.IS_USE = ? ");
			sbd.append("AND A.ATTENDANCE_DEPT_ID =  NVL(B.ATTENDANCE_DEPT_ID, B.DEPT_ID) ");
			sbd.append("AND B.EMP_ID = ? ");
			sbd.append("AND A.ATTENDANCE_YEAR = ? ");
			sbd.append("AND DECODE(LENGTH(A.ATTENDANCE_MONTH) , ");
			sbd.append("1, ('0'|| A.ATTENDANCE_MONTH), 2, A.ATTENDANCE_MONTH)= ? ");

			// 查询参数数组
			// 系统时间
			SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateParse.format(new Date()); 
			Object[] params = new Object[7];
			int i =0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEmpId;
			params[i++] = strDate.substring(0, 4);
			params[i++] = strDate.substring(5,7);
			// 记录数
			String sqlCount = "SELECT " +
					" COUNT(A.ATTENDANCESTANDARDID) " +sbd.substring(intLength, sbd.length());
			List<HrCAttendancestandard> list = bll.queryByNativeSQL(sbd.toString(), params);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<HrCAttendancestandard> arrList = new ArrayList<HrCAttendancestandard>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					HrCAttendancestandard hrCAttendancestandard = new HrCAttendancestandard();
					Object[] data = (Object[]) it.next();
					// 上午上班时间
					if(data[0] != null) {
						hrCAttendancestandard.setAmBegingTime(data[0].toString());
					}
					// 下午下班时间
					if (data[1] != null) {
						hrCAttendancestandard.setPmEndTime(data[1].toString());
					}
					arrList.add(hrCAttendancestandard);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:系统月份上下班时间查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:系统月份上下班时间查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}

	/**
	 * 查询考勤部门名称
	 * @param strEmpId 人员id
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 考勤部门名称
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAttendanceDept(String strEmpId, String strEnterpriseCode) throws SQLException {
		LogUtil.log("EJB:考勤部门名称查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("B.ATTENDANCE_DEPT_ID, ");
			sbd.append("B.ATTENDANCE_DEPT_NAME ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_J_EMP_INFO A, ");
			sbd.append("HR_C_ATTENDANCEDEP B ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND B.ENTERPRISE_CODE = ? ");
			sbd.append("AND B.IS_USE = ? ");
			sbd.append("AND NVL(A.ATTENDANCE_DEPT_ID, A.DEPT_ID) = B.ATTENDANCE_DEPT_ID ");
			sbd.append("AND A.EMP_ID = ? ");

			
			// 查询参数数组
			Object[] params = new Object[5];
			int i =0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEmpId;
			// 记录数
			String sqlCount = "SELECT " +
					" COUNT(B.ATTENDANCE_DEPT_ID) " +sbd.substring(intLength, sbd.length());
			List<AttendancedepFollowInfo> list = bll.queryByNativeSQL(sbd.toString(), params);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<AttendancedepFollowInfo> arrList = new ArrayList<AttendancedepFollowInfo>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					AttendancedepFollowInfo attendancedepFollowInfo = new AttendancedepFollowInfo();
					Object[] data = (Object[]) it.next();
					// 考勤部门id
					if(data[0] != null) {
						attendancedepFollowInfo.setAttendanceDeptId(data[0].toString());
					}
					// 考勤部门名称
					if (data[1] != null) {
						attendancedepFollowInfo.setAttendanceDeptName(data[1].toString());
					}
					arrList.add(attendancedepFollowInfo);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:考勤部门名称查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:考勤部门名称查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}

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
			String strEnterpriseCode) throws SQLException {
		LogUtil.log("EJB:参数年的上下班时间查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("A.AM_BEGING_TIME , ");
			sbd.append("A.AM_END_TIME,  ");
			sbd.append("A.PM_BEGING_TIME, ");
			sbd.append("A.PM_END_TIME, ");
			sbd.append("A.STANDARD_TIME ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_C_ATTENDANCESTANDARD A, ");
			sbd.append("HR_J_EMP_INFO B ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND B.ENTERPRISE_CODE = ? ");
			sbd.append("AND B.IS_USE = ? ");
			sbd.append("AND A.ATTENDANCE_DEPT_ID = NVL(B.ATTENDANCE_DEPT_ID, B.DEPT_ID) ");
			sbd.append("AND B.EMP_ID = ? ");
			sbd.append("AND A.ATTENDANCE_YEAR = ? ");
			sbd.append("AND DECODE(LENGTH(A.ATTENDANCE_MONTH) , ");
			sbd.append("1, ('0'|| A.ATTENDANCE_MONTH), 2, A.ATTENDANCE_MONTH)= ? ");

			// 查询参数数组
			Object[] params = new Object[7];
			int i =0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEmpId;
			params[i++] = strDate.substring(0, 4);
			params[i++] = strDate.substring(5,7);
			// 记录数
			String sqlCount = "SELECT " +
					" COUNT(A.ATTENDANCESTANDARDID) " +sbd.substring(intLength, sbd.length());
			List<HrCAttendancestandard> list = bll.queryByNativeSQL(sbd.toString(), params);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<HrCAttendancestandard> arrList = new ArrayList<HrCAttendancestandard>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					HrCAttendancestandard hrCAttendancestandard = new HrCAttendancestandard();
					Object[] data = (Object[]) it.next();
					// 上午上班时间
					if(data[0] != null) {
						hrCAttendancestandard.setAmBegingTime(data[0].toString());
					}
					// 上午下班时间
					if (data[1] != null) {
						hrCAttendancestandard.setAmEndTime(data[1].toString());
					}
					// 下午上班时间
					if(data[2] != null) {
						hrCAttendancestandard.setPmBegingTime(data[2].toString());
					}
					// 下午下班时间
					if (data[3] != null) {
						hrCAttendancestandard.setPmEndTime(data[3].toString());
					}
					// 标准出勤时间
					if (data[4] != null) {
						hrCAttendancestandard.setStandardTime(Double.parseDouble(data[4].toString()));
					}
					arrList.add(hrCAttendancestandard);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:参数年的上下班时间查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:参数年的上下班时间查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}

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
			String strEndDate, String strEnterpriseCode) throws SQLException {
		LogUtil.log("EJB:开始时间和结束时间之间的中间年度查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT DISTINCT ");
			sbd.append("A.ATTENDANCE_YEAR , ");
			sbd.append("A.STANDARD_TIME ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_C_ATTENDANCESTANDARD  A, ");
			sbd.append("HR_J_EMP_INFO B ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND B.ENTERPRISE_CODE = ? ");
			sbd.append("AND B.IS_USE = ? ");
			sbd.append("AND A.ATTENDANCE_DEPT_ID = B.ATTENDANCE_DEPT_ID ");
			sbd.append("AND B.EMP_ID = ? ");
			sbd.append("AND A.ATTENDANCE_YEAR > ? ");
			sbd.append("AND A.ATTENDANCE_YEAR < ? ");

			// 查询参数数组
			Object[] params = new Object[7];
			int i =0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEmpId;
			params[i++] = strStartDate.substring(0, 4);
			params[i++] = strEndDate.substring(0,4);
			// 记录数
			String sqlCount = "SELECT " +
					" COUNT(A.ATTENDANCESTANDARDID) " +sbd.substring(intLength, sbd.length());
			List<HrCAttendancestandard> list = bll.queryByNativeSQL(sbd.toString(), params);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<HrCAttendancestandard> arrList = new ArrayList<HrCAttendancestandard>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					HrCAttendancestandard hrCAttendancestandard = new HrCAttendancestandard();
					Object[] data = (Object[]) it.next();
					// 考勤年份
					if(data[0] != null) {
						hrCAttendancestandard.setAttendanceYear(data[0].toString());
					}
					// 标准出勤时间
					if (data[1] != null) {
						hrCAttendancestandard.setStandardTime(Double.parseDouble(data[1].toString()));
					}
					arrList.add(hrCAttendancestandard);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:开始时间和结束时间之间的中间年度查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:开始时间和结束时间之间的中间年度查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}
	
	/**
	 * 查询考勤部门表的叶子节点（用于考勤登记页面）
	 * add by fyyang 20100705
	 */
	public PageObject getAttendanceDeptForRegister(String loginId,
			String enterpriseCode) {
		LogUtil.log("EJB:考勤部门查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	String sql=
        		"SELECT a.attendance_dept_id,a.attendance_dept_name\n" +
        		"  FROM HR_C_ATTENDANCEDEP A\n" + 
        		" WHERE A.ATTEND_WRITER_ID = "+loginId+"\n" + 
        		"   and a.is_use = 'Y'\n" + 
        		"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
        		"   and (select count(1)\n" + 
        		"          from HR_C_ATTENDANCEDEP B\n" + 
        		"         where b.top_check_dep_id = A.Attendance_Dept_Id\n" + 
        		"           and b.is_use = 'Y'\n" + 
        		"           and b.enterprise_code = '"+enterpriseCode+"') = 0\n" + 
        		" order by a.attendance_dept_name";

        	
        	// 查询
        	List list = bll.queryByNativeSQL(sql);
        	Iterator it = list.iterator();
        	List<HrCAttendancedep> arrlist = new ArrayList<HrCAttendancedep>();
            while (it.hasNext()) {
            	HrCAttendancedep info = new HrCAttendancedep();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setAttendanceDeptId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setAttendanceDeptName(data[1].toString());
            	}
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
            LogUtil.log("EJB:考勤部门查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:劳考勤部门查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 考勤部门管理<考勤人员变更>删除考勤部门人员方法
	 * add by sychen 20100713
	 * @param ids
	 */
	public void deleteAttendanceDeptId(String ids) {

		String sql = "update hr_j_emp_info t\n" + "   set t.attendance_dept_id = ''\n"
				+ " where t.emp_id in (" + ids + ")";

		bll.exeNativeSQL(sql);

	}
}