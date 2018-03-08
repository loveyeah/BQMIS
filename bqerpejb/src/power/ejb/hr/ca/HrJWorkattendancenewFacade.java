package power.ejb.hr.ca;

import java.sql.SQLException;
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

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrJWorkattendancenew.
 * 
 * @see power.ejb.hr.ca.HrJWorkattendancenew
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJWorkattendancenewFacade implements
		HrJWorkattendancenewFacadeRemote {

	/** 是否使用 */
	public static final String IS_USE_Y = "Y";
	/** 考勤类别 */
	public static final String ATTEN_DEPT_TYPE_3 = "3";
	/** 字符串: 0 */
	public static final String STRING_0 = "0";
	/** 字符串: 1 */
	public static final String STRING_1 = "1";
	/** 字符串: 2 */
	public static final String STRING_2 = "2";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	


	/**
	 * Perform an initial save of a previously unsaved HrJWorkattendancenew
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrJWorkattendancenew entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkattendancenew entity) {
		LogUtil.log("saving HrJWorkattendancenew instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJWorkattendancenew entity.
	 * 
	 * @param entity
	 *            HrJWorkattendancenew entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkattendancenew entity) {
		LogUtil.log("deleting HrJWorkattendancenew instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJWorkattendancenew.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJWorkattendancenew entity and return it or a
	 * copy of it to the sender. A copy of the HrJWorkattendancenew entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJWorkattendancenew entity to update
	 * @return HrJWorkattendancenew the persisted HrJWorkattendancenew entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJWorkattendancenew update(HrJWorkattendancenew entity) {
		LogUtil.log("updating HrJWorkattendancenew instance", Level.INFO, null);
		try {
			HrJWorkattendancenew result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJWorkattendancenew findById(HrJWorkattendancenewId id) {
		LogUtil.log("finding HrJWorkattendancenew instance with id: " + id,
				Level.INFO, null);
		try {
			HrJWorkattendancenew instance = entityManager.find(
					HrJWorkattendancenew.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJWorkattendancenew entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkattendancenew property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkattendancenew> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkattendancenew> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJWorkattendancenew instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJWorkattendancenew model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJWorkattendancenew entities.
	 * 
	 * @return List<HrJWorkattendancenew> all HrJWorkattendancenew entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkattendancenew> findAll() {
		LogUtil.log("finding all HrJWorkattendancenew instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from HrJWorkattendancenew model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	private Long getCountAttendanceDate(String attendanceDate,Long deptId) {
		String sql = "select count(1) from hr_j_workattendancenew t where to_char(t.attendance_date,'yyyy-mm-dd') = '"
				+ attendanceDate + "'  and t.attendance_dept_id ='"+deptId+"'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		return count;

	}

	/**
	 * 未审核明细部查询
	 * 
	 * @param attendanceDate
	 *            考勤日期
	 * @param attendanceYear
	 *            考勤年份
	 * @param attendanceMonth
	 *            考勤月份
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param workResetFlag
	 *            上班休息标识
	 * @param enterpriseCode
	 *            企业编码
	 * @return 未审核明细部信息
	 */

	@SuppressWarnings("unchecked")
	public PageObject getDetailInfoForRegister(String attendanceDate,
			String attendanceYear, String attendanceMonth,
			String attendanceDeptId, String workResetFlag, String enterpriseCode) {
		try {
			PageObject object = new PageObject();
			String sql = "";
			String countSql = "";
			if (this.getCountAttendanceDate(attendanceDate,Long.parseLong(attendanceDeptId)) > 0) {
				sql = "select distinct t.emp_id,\n"
						+ "       a.chs_name,\n"
						+ "       t.dept_id,\n"
						+ "       b.dept_name,\n"
						+ "       t.attendance_dept_id,\n"
						+ "       c.attendance_dept_name,\n"
						+ "       t.work,\n"
						+ "       t.work_shift_id,\n"
						+ "       t.overtime_type_id,\n"
						+ "       t.overtime_time_id,\n"
						+ "       t.absent_time_id,\n"
						+ "       t.vacation_type_id,\n"
						+ "       t.other_time_id,\n"
						+ "       t.change_rest,\n"
						+ "       t.year_rest,\n"
						+ "       t.evection_type,\n"
						+ "       t.out_work,\n"
						+ "       t.memo,\n"
						+ "       '1'\n"
						+ "  from hr_j_workattendancenew t\n"
						+ "  left join hr_j_emp_info a on a.emp_id = t.emp_id\n"
						+ "  left join hr_c_dept b on b.dept_id = t.dept_id\n"
						+ "  left join HR_C_ATTENDANCEDEP c on c.attendance_dept_id =\n"
						+ "                                    t.attendance_dept_id\n"
						+ "where to_char(t.attendance_date,'yyyy-mm-dd') = '" + attendanceDate
						+ "'\n" + "and t.attendance_dept_id ='"
						+ attendanceDeptId + "'" +
//						" order by a.emp_id";
		                " order by a.chs_name";//update by sychen 20100907
				
				countSql = "select count(1)\n"
						+ "  from hr_j_workattendancenew t\n"
						+ "  left join hr_j_emp_info a on a.emp_id = t.emp_id\n"
						+ "  left join hr_c_dept b on b.dept_id = t.dept_id\n"
						+ "  left join HR_C_ATTENDANCEDEP c on c.attendance_dept_id =\n"
						+ "                                    t.attendance_dept_id\n"
						+ "where to_char(t.attendance_date,'yyyy-mm-dd') = '" + attendanceDate
						+ "'\n" + "and t.attendance_dept_id ='"
						+ attendanceDeptId + "'";
			} else {
				sql = "select distinct a.emp_id,\n"
						+ "       a.chs_name,\n"
						+ "       b.dept_id,\n"
						+ "       b.dept_name,\n"
						+ "       t.attendance_dept_id,\n"
						+ "       t.attendance_dept_name,\n"
						+ "       '1',\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       null,\n"
						+ "       '0'\n"
						+ "  from HR_C_ATTENDANCEDEP t\n"
						+ "  left join hr_j_emp_info a on a.attendance_dept_id = t.attendance_dept_id\n"
						+ "  left join hr_c_dept b on b.dept_id = a.dept_id\n"
						+ " where t.attendance_dept_id = '"+attendanceDeptId+"' \n" 
						+" and a.emp_id is not null \n"+ //add by fyyang 20100717
//								" order by a.emp_id";
				          " order by a.chs_name";//update by sychen 20100907

				countSql = "select count(1)\n"
					+ "  from HR_C_ATTENDANCEDEP t\n"
					+ "  left join hr_j_emp_info a on a.attendance_dept_id = t.attendance_dept_id\n"
					+ "  left join hr_c_dept b on b.dept_id = a.dept_id\n"
					+ " where t.attendance_dept_id = '"+attendanceDeptId+"'  \n"
					+" and a.emp_id is not null \n"; //add by fyyang 20100717
			}
			List list = bll.queryByNativeSQL(sql);
			Long count = Long.parseLong(bll.getSingal(countSql).toString());
			object.setList(list);
			object.setTotalCount(count);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:未审核明细部查询失败。", Level.SEVERE, e);
			throw e;
		}

	}
	/**
	 * 查询是否已经审核信息
	 * 
	 * @param attendanceYear
	 *            考勤年份
	 * @param attendanceMonth
	 *            考勤月份
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param enterpriseCode
	 *            企业编码
	 * @return 是否已经审核信息
	 */
	@SuppressWarnings("unchecked")
	public boolean getApprovedForRegister(String attendanceMonth, String attendanceDeptId) {
		LogUtil.log("EJB:判断是否已经审核开始。",
                Level.INFO, null);
        try {
        	//  modify  by wpzhu 20100714
        	String sql = "select count(1) from HR_J_ATTENDANCE_APPROVE t where t.APPROVE_MONTH='"+attendanceMonth+"' and t.ATTENDANCE_DEPT_ID ='"+attendanceDeptId+"'";
        	System.out.println("the sql"+sql);
        	Long count = Long.parseLong( bll.getSingal(sql).toString());
        	if(count > 0){
        		return true;
        	}else{
        		return false;
        	}
        	
        } catch (RuntimeException e) {
            LogUtil.log("EJB:判断是否已经审核失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	public HrJWorkattendancenew findById(HrJWorkattendancenewId id,
			String enterpriseCode){
		
		LogUtil.log("EJB:根据ID查找考勤记录开始。", Level.INFO, null);
		try {
			HrJWorkattendancenew bean = new HrJWorkattendancenew();
			// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT * ");
        	sbSql.append("FROM ");
        	sbSql.append("	hr_j_workattendancenew A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	A.EMP_ID = ? AND ");
        	sbSql.append("	A.ATTENDANCE_DATE = ? AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.EMP_ID, ");
        	sbSql.append("	A.ATTENDANCE_DATE ");
        	// 查询
        	List<HrJWorkattendancenew> list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{id.getEmpId(), id.getAttendanceDate(),
        		"Y", enterpriseCode},
        		HrJWorkattendancenew.class);
            if(list == null) {
            	bean = null;
            } else {
            	bean = list.get(0);
            }
            LogUtil.log("EJB:根据ID查找考勤记录结束。", Level.INFO, null);
			return bean;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:根据ID查找考勤记录失败。", Level.SEVERE, re);
			throw re;
		}
	
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAattendanceListByYear(Long deptId,String strYear,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		PageObject obj=new PageObject();
		String sql="select tt.attendance_dept_id,\n" +
			" tt.emp_id,\n" + 
			" tt.detpName,\n" + 
			" tt.teamName,\n" + 
			" tt.chs_name,\n" + 
			" tt.month1,\n" + 
			" tt.month2,\n" + 
			" tt.month3,\n" + 
			" tt.month4,\n" + 
			" tt.month5,\n" + 
			" tt.month6,\n" + 
			" tt.month7,\n" + 
			" tt.month8,\n" + 
			" tt.month9,\n" + 
			" tt.month10,\n" + 
			" tt.month11,\n" + 
			" tt.month12\n"+
			" from ("+
			"select distinct t.attendance_dept_id,\n" +
			"       t.emp_id,\n" + 
			"        nvl(a.attendance_dept_name,'灞桥电厂')   as detpName,\n" + 
			"       c.attendance_dept_name  as teamName,\n" + 
			"       d.chs_name\n";
		   for(int i=1;i<=12;i++)
		   {
			   String month=strYear;
			   if(i<10) month=month+"-0"+i;
			   else month=month+"-"+i;
			   
			   String cName="month"+i;
		    	sql +=" ,GETHRVACATIONBYMONTH(t.emp_id, '"+month+"') as "+cName+" \n" ;
		   }
			
			sql +="  from HR_J_WORKATTENDANCENEW t,\n" + 
			"       HR_C_ATTENDANCEDEP     a,\n" + 
			"       HR_C_ATTENDANCEDEP     c,\n" + 
			"       hr_j_emp_info          d\n" + 
			" where to_char(t.attendance_date, 'yyyy') = '"+strYear+"'\n" + 
			//"   and t.attendance_dept_id = a.top_check_dep_id(+)\n" + 
			" and c.top_check_dep_id=a.attendance_dept_id(+) \n"+
			"   and t.attendance_dept_id = c.attendance_dept_id\n" + 
			"   and t.emp_id = d.emp_id\n" + 
			"   and t.is_use = 'Y'   and t.enterprise_code='"+enterpriseCode+"' \n" ;
		if(deptId!=null&&deptId!=-1)
		{
		  sql+=	"   and t.attendance_dept_id in\n" + 
			"       (select b.attendance_dept_id\n" + 
			"          from HR_C_ATTENDANCEDEP b\n" + 
			"         where (b.attendance_dept_id = "+deptId+" or b.top_check_dep_id = "+deptId+"))";
		}
		
		sql+="  order by t.attendance_dept_id,c.attendance_dept_name  )tt \n";
		for(int i=1;i<=12;i++)
		   {
			   
			   String cName="month"+i;
		    	if(i==1) {sql+=" where  trim(tt."+cName+") <>' ' \n";}
		    	else {sql+="  or  trim(tt."+cName+") <>' ' \n";}
		   }
		
		String sqlCount=" select count(*) from ("+sql+")";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		obj.setList(list);
		obj.setTotalCount(totalCount);

		
		return obj;
	}
	
	


	
	@SuppressWarnings("unchecked")
	public PageObject findYearRestList(Long deptId,String strYear,String enterpriseCode,String chsName,final int... rowStartIdxAndCount)
	{
		PageObject obj=new PageObject();
		String sql=
			"select distinct t.attendance_dept_id,\n" +
			"                t.emp_id,\n" + 
			"                nvl(a.attendance_dept_name,'灞桥电厂'),\n" + 
			"                c.attendance_dept_name,\n" + 
			"                d.chs_name,\n" + 
			"                (case\n" + 
			"                  when (to_date('"+strYear+"-06-30', 'yyyy-MM-dd') - d.work_date) / 365 > 20 then\n" + 
			"                   15\n" + 
			"                  when (to_date('"+strYear+"-06-30', 'yyyy-MM-dd') - d.work_date) / 365 > 10 then\n" + 
			"                   10\n" + 
			"                  when (to_date('"+strYear+"-06-30', 'yyyy-MM-dd') - d.work_date) / 365 > 1 then\n" + 
			"                   5\n" + 
			"                  else\n" + 
			"                   0\n" + 
			"                end),\n" + 
			"                f.startDate,\n" + 
			"                f.endDate,\n" + 
			"                nvl(f.totalDays, 0)\n" + 
			"  from HR_J_WORKATTENDANCENEW t,\n" + 
			"       HR_C_ATTENDANCEDEP a,\n" + 
			"       HR_C_ATTENDANCEDEP c,\n" + 
			"       hr_j_emp_info d,\n" + 
			"       (select e.emp_id,\n" + 
			"               max(e.attendance_date) endDate,\n" + 
			"               min(e.attendance_date) startDate,\n" + 
			"               sum(1) totalDays\n" + 
			"          from HR_J_WORKATTENDANCENEW e\n" + 
			"         where to_char(e.attendance_date, 'yyyy') = '"+strYear+"'\n" + 
			"           and e.year_rest = '1'\n" + 
			"           and e.is_use = 'Y'\n" + 
			"           and e.enterprise_code = '"+enterpriseCode+"'\n" + 
			"         group by e.emp_id) f\n" + 
			" where to_char(t.attendance_date, 'yyyy') = '"+strYear+"'\n" + 
			//"   and t.attendance_dept_id = a.top_check_dep_id(+)\n" + 
			" and c.top_check_dep_id=a.attendance_dept_id(+) \n"+
			"   and t.attendance_dept_id = c.attendance_dept_id\n" + 
			"   and t.emp_id = d.emp_id\n" + 
			"   and t.emp_id = f.emp_id(+)\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" ;
		if(chsName!=null&&!chsName.equals(""))
		{
		  sql+=	"   and d.chs_name like '%"+chsName.trim()+"%'\n";
		}
		if(deptId!=null&&deptId!=-1)
		{
		sql+=	"   and t.attendance_dept_id in\n" + 
			"       (select b.attendance_dept_id\n" + 
			"          from HR_C_ATTENDANCEDEP b\n" + 
			"         where (b.attendance_dept_id = "+deptId+" or b.top_check_dep_id = "+deptId+"))\n";
		}
		sql+=
			" order by t.attendance_dept_id, c.attendance_dept_name,d.chs_name";
//		System.out.println("the sql"+sql);
		String sqlCount=" select count(*) from ("+sql+")";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		obj.setList(list);
		obj.setTotalCount(totalCount);

		return obj;
	}
	
	
	
	
//	public PageObject findCompanyMonthList(Long deptId,String strMonth,String enterpriseCode,final int... rowStartIdxAndCount) throws SQLException
//	{
//		PageObject obj=new PageObject();
//		
//		HrCVacationtypeFacadeRemote remote=(HrCVacationtypeFacadeRemote)Ejb3Factory.getInstance()
//		.getFacadeRemote("HrCVacationtypeFacade");
//		PageObject typeObj=remote.findAllVacation(enterpriseCode);
//		List<HrCVacationtype> typeList=typeObj.getList();
//		int days=31;
//		
//		if("02".equals(strMonth.substring(6, 8))) days=28;
//		else if("04,06,09,11".indexOf(strMonth.substring(6, 8))!=-1) days=30;
//		else days=31;
//		
//		String sqlName="select '人员id','姓名','部门','班组','一','二'";
//		
//		String sql=
//
//			"\n" +
//			"select t1.*\n";
//		
//	      for(int i=1;i<=days;i++)
//	      {
//	    	  String strDate="";
//	    	  if(i<10) strDate=strMonth+"-0"+i;
//	    	  else strDate=strMonth+"-"+i;
//		   sql+=	"       ,GETHRVACATIONBYDAY(t1.emp_id, '"+strDate+"')\n" ;
//	      }
//		   
//	      for(int j=0;j<typeList.size();j++)
//			{
//				
//				String cName="t3.day"+j;
//				sql+=	" ,"+cName+"\n";
//			
//			}
//		   sql+=	
//			"  from (select distinct t.emp_id,\n" + 
//			"                        d.chs_name,\n" + 
//			"                        c.attendance_dept_name,\n" + 
//			"                        a.attendance_dept_name as banzu,\n" + 
//			"                        sum(GETHRDAYSBYID(t.overtime_time_id)) overtime,\n" + 
//			"                        sum(decode(t.year_rest,'1',1,0)) yearrest,\n" + 
//			"                        sum(decode(t.work,'1',1,0)) workday,\n" + 
//			"                       sum( decode(t.change_rest,'1',1,0) ) changeday\n" + 
//			"\n" + 
//			"\n" + 
//			"          from HR_J_WORKATTENDANCENEW t,\n" + 
//			"               HR_C_ATTENDANCEDEP     a,\n" + 
//			"               HR_C_ATTENDANCEDEP     c,\n" + 
//			"               hr_j_emp_info          d,\n" + 
//			"               HR_C_VACATIONTYPE      e\n" + 
//			"         where to_char(t.attendance_date, 'yyyy-MM') = '2010-07'\n" + 
//			"           and t.attendance_dept_id = a.top_check_dep_id(+)\n" + 
//			"           and t.attendance_dept_id = c.attendance_dept_id\n" + 
//			"           and t.emp_id = d.emp_id\n" + 
//			"           and t.vacation_type_id = e.vacation_type_id(+)\n" + 
//			"           and t.is_use = 'Y'\n" + 
//			"           and t.enterprise_code = 'hfdc'";
//
//		if(deptId!=null&&deptId!=-1)
//		{
//		sql+=	"   and t.attendance_dept_id in\n" + 
//			"       (select b.attendance_dept_id\n" + 
//			"          from HR_C_ATTENDANCEDEP b\n" + 
//			"         where (b.attendance_dept_id = "+deptId+" or b.top_check_dep_id = "+deptId+"))\n";
//		}
//		
//
//		sql+="group by t.emp_id,\n" +
//		"                        d.chs_name,\n" + 
//		"                        c.attendance_dept_name,\n" + 
//		"                        a.attendance_dept_name"+
//		") t1, " +
//		"(\n" +
//		"           select t2.emp_id\n";
//		
//		for(int j=0;j<typeList.size();j++)
//		{
//			HrCVacationtype model=typeList.get(j);
//			String cName="day"+j;
//		     sql+="                  ,sum(decode(t2.vacation_type_id,"+model.getVacationTypeId()+",GETHRDAYSBYID(t2.other_time_id), 0)) as  "+cName+"\n" ;
//		
//		}
//		
//		sql+="             from HR_J_WORKATTENDANCENEW t2\n" + 
//		"            where to_char(t2.attendance_date, 'yyyy-MM') = '2010-07' and t2.is_use='Y' and t2.enterprise_code='hfdc'\n" + 
//		"            group by t2.emp_id\n" + 
//		"           ) t3\n" + 
//		"           where t1.emp_id=t3.emp_id \n"+
//		" order by t1.attendance_dept_name,t1.banzu,t1.chs_name ";
//
//		
//		return obj;
//	}
}