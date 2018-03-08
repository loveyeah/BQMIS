package power.ejb.hr.salary;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.db.DBHelper;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.salary.form.SalaryWageForm;

/**
 * @author liuyi 20100208
 */
@Stateless
public class HrJSalaryWageFacade implements HrJSalaryWageFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName="NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	

	/**
	 * 计算工资 月奖 大奖
	 * @param flag 1：工资, 2：月奖, 3：大奖 
	 * @param deptId 部门
	 * @param yearMonth  月份
	 * @param enterpriseCode
	 * @throws Exception 
	 */
	public void calculateSalary(String flag,String deptId, String yearMonth,
			String enterpriseCode) throws Exception {
		if(deptId != null && !deptId.equals("") && yearMonth != null && !yearMonth.equals(""))
		{
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn = DBHelper.getConn();
				CallableStatement cs = null;
				if(flag != null && flag.equals("1"))
				{
					cs = conn
					.prepareCall("call HR.getSalary(?,?,?)");
				}
				else if(flag != null && flag.equals("2"))
				{
					cs = conn
					.prepareCall("call HR.getPremium(?,?,?)");
				}else{
					cs = conn
					.prepareCall("call HR.getBigAward(?,?,?)");
				}
				cs.setInt(1, Integer.parseInt(deptId));
				cs.setString(2, yearMonth);
				cs.setString(3, enterpriseCode);
				// 传入入口参数
				// cs.setString(1,"aaaaaaaa");
//				Date xx = new Date();
				// cs.setString(1,"2009-6-18 15");
//				cs.setDate(1, new java.sql.Date(xx.getTime()));
				// cs.setString(3, "11.0000");
				// 注册返回参数
				// cs.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR);
				cs.execute();
				// 获取返回游标，返回类型为ResultSet
				// rs = (ResultSet)cs.getObject(3);
				// while (rs.next()) {
				// System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t"
				// + rs.getString(3));
				// }
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
		
		
		
		
		
	}
    
	//add by ypan 20100730
	public PageObject getSalary(String startDate,String endDate,int...rowStartIdxAndCount){
		PageObject pg=new PageObject();
		  String sql="select \n" +
		  		" w.emp_id,\n" +
		  " f.chs_name,\n" +
	       "sum(nvl(w.total_wage,0)),\n"+
	       "sum(nvl(w.basis_wage,0)),\n"+
	       "sum(nvl(w.age_wage,0)),\n"+
	       "sum(nvl(w.month_awards,0)),\n"+
	       "sum(nvl(w.big_awards,0)),\n"+
           "sum(nvl(w.total_wage,0)+nvl(w.basis_wage,0)+nvl(w.age_wage,0)+nvl(w.month_awards,0)+nvl(w.big_awards,0))\n"+
	       "from HR_J_SALARY_WAGE       w,\n"+
	       "hr_j_emp_info f\n"+
	      "where w.emp_id=f.emp_id\n";
		if(startDate!=null&&!"".equals(startDate)&&endDate!=null&&!"".equals(endDate))
		{
			sql+="and w.salary_month>=to_date('"+startDate+"','yyyy-MM')\n"+
                  "and  w.salary_month<=to_date('"+endDate+"','yyyy-MM')";
		}
		sql+="group by w.emp_id,f.chs_name,w.salary_month";
//   System.out.println("the sql"+sql);
		String sqlCount="select count(*) from ("+sql+")tt";
     
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	/**
	 * 
	 * @param workCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  String getMsgName(String workCode)
{
	String msgName="";
	String sql="select  getworkername('"+workCode+"')\n " +
			"   from   hr_j_emp_info a\n  " +
			"   where   a.emp_code ='"+workCode+"'";
	Object obj=bll.getSingal(sql);
	if(obj!=null)
	{
		msgName=obj.toString();
		return msgName;
		
	}
	return "";
	
}
	
	
	@SuppressWarnings("unchecked")
	public PageObject getBasicPrimiumAndAward(String deptId, String yearMonth,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.wage_id,\n" +
			"       a.emp_id,\n" + 
			"       b.chs_name,\n" + 
			"       to_char(a.salary_month, 'yyyy-mm'),\n" + 
			"       a.basis_wage,\n" + 
			"       a.age_wage,\n" + 
			"       a.operation_wage,\n" + 
			"       a.remain_wage,\n" + 
			"       a.point_wage,\n" + 
			"       a.overtime_wage,\n" + 
			"       a.deduction_wage,\n" + 
			"       a.total_wage,\n" + 
			"       a.wage_memo,\n" + 
//			"\n" + 
//			"       (select t.month_award\n" + 
//			"          from HR_C_MONTH_AWARD t\n" + 
//			"         where t.is_use = 'Y'\n" + 
//			"           and to_date('"+yearMonth+"', 'yyyy-mm') between t.effect_start_time and\n" + 
//			"               t.effect_end_time) monthBasic,\n" + 
//			"       a.coefficient,\n" + 
//			"       a.month_award_cut,\n" + 
//			"       a.month_award,\n" + 
//			"       a.month_award_memo,\n" + 
//			"\n" + 
//			"       nvl((select tt.big_award_base\n" + 
//			"          from HR_C_BIG_AWARD tt\n" + 
//			"         where tt.big_award_id in\n" + 
//			"               (select min(f.big_award_id)\n" + 
//			"                  from HR_C_BIG_AWARD f\n" + 
//			"                 where f.is_use = 'Y'\n" + 
//			"                   and to_date('"+yearMonth+"', 'yyyy-mm') between\n" + 
//			"                       f.assessment_from and f.assessment_to)),0) as award1Basic,\n" + 
//			"       a.coefficient as award1coeff,\n" + 
//			"       a.big_award_one_cut,\n" + 
//			"       a.big_award_one,\n" + 
//			"\n" + 
//			"\n" + 
//			"       decode(nvl((select max(f.big_award_id)\n" + 
//			"                  from HR_C_BIG_AWARD f\n" + 
//			"                 where f.is_use = 'Y'\n" + 
//			"                   and to_date('"+yearMonth+"', 'yyyy-mm') between\n" + 
//			"                       f.assessment_from and f.assessment_to),0) -\n" + 
//			"\n" + 
//			"                       nvl((select min(f.big_award_id)\n" + 
//			"                  from HR_C_BIG_AWARD f\n" + 
//			"                 where f.is_use = 'Y'\n" + 
//			"                   and to_date('"+yearMonth+"', 'yyyy-mm') between\n" + 
//			"                       f.assessment_from and f.assessment_to),0)\n" + 
//			"                       ,0,0,nvl((select max(f.big_award_base)\n" + 
//			"                  from HR_C_BIG_AWARD f\n" + 
//			"                 where f.is_use = 'Y'\n" + 
//			"                   and to_date('"+yearMonth+"', 'yyyy-mm') between\n" + 
//			"                       f.assessment_from and f.assessment_to),0)) as award2Basic,\n" + 
//			"       a.coefficient as award2coff,\n" + 
//			"       a.big_award_two_cut,\n" + 
//			"       a.big_award_two,\n" + 
//			"       a.big_award_memo,\n" + 
			"       GETDEPTNAME(GETFirstLevelBYID(b.dept_id)) dept_name1,\n" + //add by sychen 20100803
			"       a.others,\n" + //add by sychen 20100804
			"       a.individual_awards_one,\n" + //add by sychen 20100804
			"       a.individual_awards_two,\n" + //add by sychen 20100804
			"       a.month_awards,\n" + //add by sychen 20100804
			"       a.big_awards,\n" + //add by sychen 20100804
			"       a.total_income,\n" + //add by sychen 20100804
			"       b.dept_id,\n" + //add by sychen 20100804
			"       b.new_emp_code,\n"+//add by sychen 20100806
            "       c.dept_name" +//add by mgxia 20100909
			"  from hr_j_salary_wage a, hr_j_emp_info b,hr_c_dept c\n" + 
			" where a.emp_id = b.emp_id\n" + 
			" and c.dept_id = b.dept_id\n" +
			" and a.enterprise_code='hfdc'\n" + 
			" and to_char(a.salary_month,'yyyy-mm') = '"+yearMonth+"'\n";
		if(deptId != null && !deptId.equals(""))
			//update by sychen 20100803
			sql+=" and b.dept_id in (select t.dept_id\n" +
                 "                     from hr_c_dept t\n" + 
                 "                     where t.is_use = 'Y'\n" +  //update by sychen 20100902
//                 "                     where t.is_use = 'U'\n" +  
                 "                     start with t.dept_id = "+deptId+"\n" + 
                 "                     connect by prior t.dept_id = t.pdept_id)\n";
//			sql += " and b.dept_id="+deptId+" \n" ;
		//update by sychen 20100803 end
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += "  order by dept_name1,c.dept_name  \n";
//		System.out.println(sql);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		Iterator it = list.iterator();
		List<SalaryWageForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			SalaryWageForm form = new SalaryWageForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				form.setWageId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				form.setEmpId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				form.setChsName(data[2].toString());
			if (data[3] != null)
				form.setSalaryMonth(data[3].toString());
			if (data[4] != null)
				form.setBasisWage(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				form.setAgeWage(Double.parseDouble(data[5].toString()));
			if (data[6] != null)
				form.setOperationWage(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				form.setRemainWage(Double.parseDouble(data[7].toString()));
			if (data[8] != null)
				form.setPointWage(Double.parseDouble(data[8].toString()));
			if (data[9] != null)
				form.setOvertimeWage(Double.parseDouble(data[9].toString()));
			if (data[10] != null)
				form.setDeductionWage(Double.parseDouble(data[10].toString()));
			if (data[11] != null)
				form.setTotalWage(Double.parseDouble(data[11].toString()));
			if (data[12] != null)
				form.setWageMemo(data[12].toString());
			
//			if (data[13] != null)
//				form.setMonthBasic(Double.parseDouble(data[13].toString()));
//			if (data[14] != null)
//				form.setCoefficient(Double.parseDouble(data[14].toString()));
//			if (data[15] != null)
//				form.setMonthAwardCut(Double.parseDouble(data[15].toString()));
//			if (data[16] != null)
//				form.setMonthAward(Double.parseDouble(data[16].toString()));
//			if (data[17] != null)
//				form.setMonthAwardMemo(data[17].toString());
//			if (data[18] != null)
//				form.setAward1Basic(Double.parseDouble(data[18].toString()));
//			if (data[19] != null)
//				form.setAward1coeff(Double.parseDouble(data[19].toString()));
//			if (data[20] != null)
//				form.setBigAwardOneCut(Double.parseDouble(data[20].toString()));
//			if (data[21] != null)
//				form.setBigAwardOne(Double.parseDouble(data[21].toString()));
//			if (data[22] != null)
//				form.setAward2Basic(Double.parseDouble(data[22].toString()));
//			if (data[23] != null)
//				form.setAward2coeff(Double.parseDouble(data[23].toString()));
//			if (data[24] != null)
//				form.setBigAwardTwoCut(Double.parseDouble(data[24].toString()));
//			if (data[25] != null)
//				form.setBigAwardTwo(Double.parseDouble(data[25].toString()));
//			if (data[26] != null)
//				form.setBigAwardMemo(data[26].toString());
			//add by sychen 20100803
//			if (data[1] != null){
//				String empId=data[1].toString();
//				
//				String st= "select t.dept_name\n" +
//					"  from hr_c_dept t\n" + 
//					" where t.is_use = 'Y'\n" +  //update by sychen 20100902
//					"and t.dept_id <> 0\n" +   //去除灞桥热电厂
////					" where t.is_use = 'U'\n" + 
//					" start with t.dept_id =\n" + 
//					"            (select i.dept_id from hr_j_emp_info i where i.emp_id = "+empId+")\n" + 
//					"connect by prior t.pdept_id = t.dept_id\n"+
//					"order by  t.dept_id asc\n";
//
//				List deptList = bll.queryByNativeSQL(st);
//				String deptNames = "";
//				for (int i = 0; i < deptList.size(); i++) {
//					Object deptData = deptList.get(i);
//					if (data != null) {
//						deptNames += deptData.toString() + "_" ;
//					}
//				}
//				deptNames=deptNames.substring(0,deptNames.lastIndexOf("_"));
//				//form.setDeptName(deptNames);
//				deptNames="";
//				
//			}
		  if(data[13]!=null){
	        	  form.setDeptName(data[13].toString());
	          }	
          if(data[14]!=null){
        	  form.setOthers(Double.parseDouble(data[14].toString()));
          }
          if(data[15]!=null){
        	  form.setIndividualAwardsOne(Double.parseDouble(data[15].toString()));
          }
          if(data[16]!=null){
        	  form.setIndividualAwardsTwo(Double.parseDouble(data[16].toString()));
          }
          if(data[17]!=null){
        	  form.setMonthAwards(Double.parseDouble(data[17].toString()));
          }
          if(data[18]!=null){
        	  form.setBigAwards(Double.parseDouble(data[18].toString()));
          }
          if(data[19]!=null){
        	  form.setTotalIncome(Double.parseDouble(data[19].toString()));
          }
          if(data[20]!=null){
        	  form.setDeptId(Long.parseLong(data[20].toString()));
          }
          if(data[21]!=null){
        	  form.setNewEmpCode(data[21].toString());
          }
			//add by sychen 20100803 end 
          if(data[22]!=null){
        	  form.setBanZhu(data[22].toString());
          }
          
			arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;

	}

	public void saveBasicPrimiumAndAward(List<HrJSalaryWage> updateList) {
		if(updateList != null && updateList.size() > 0)
		{
			for(HrJSalaryWage entity : updateList)
			{
				entityManager.merge(entity);
			}
		}
		
	}
	/**
	 * 判断历史工资不可修改 add by sychen 20100805
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	public String checkOldSalaryModify(String deptId,String enterpriseCode) {
		String sql ="select max(to_char(w.salary_month, 'yyyy-mm'))\n" +
			"  from hr_j_salary_wage w, hr_j_emp_info t\n" + 
			" where w.emp_id = t.emp_id\n" + 
			"   and w.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.dept_id = '"+deptId+"'";

	Object maxNoObj = bll.getSingal(sql);
	String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
	return maxNo;
}
	public PageObject getSalaryByMonth(String flag,Long empId,String month ,String enterpriseCode,final int...rowStartIdxAndCount)
	{
		PageObject  result =new PageObject();
		String sql=
			"select distinct \n" +
			"f.chs_name ," +
			"w.basis_wage," +
			"w.remain_wage," +
			"w.point_wage," +
			"w.age_wage," +
			"w.operation_wage," +
			"w.overtime_wage,\n" +
			"w.deduction_wage," +
			"w.others," +
			"w.total_wage," +
			"w.wage_memo," +
			"w.individual_awards_one," +
			"w.individual_awards_two," +
			"w.month_awards," +
			"w.big_awards, " +
			"nvl(w.total_wage,0)+nvl(w.individual_awards_one,0)+nvl(w.individual_awards_two,0)+nvl(w.month_awards,0)+nvl(w.big_awards,0),\n" +
			"to_char(w.salary_month ,'yyyy-MM')\n" + 
			"from   HR_J_SALARY_WAGE   w,hr_j_emp_info f\n" + 
			"where f.emp_id=w.emp_id " +
			" and w.emp_id='"+empId+"' \n " +
			"and f.is_use='Y'\n" + 
			"and f.enterprise_code='"+enterpriseCode+"'\n" + 
			"and w.enterprise_code='"+enterpriseCode+"'";
		if(flag!=null&&!"".equals(flag)&&(flag=="1"||"1".equals(flag)))
		{
		if(month!=null&&!"".equals(month))
		{
			sql+="and to_char(w.salary_month,'yyyy-MM')='"+month+"'\n";
		}
		}else
		{
			sql+=" and   w.salary_month=  (select max(w.salary_month) from HR_J_SALARY_WAGE  w where w.emp_id='"+empId+"')\n";
		}
//		System.out.println("the sql"+sql);
     String sqlCount="select count(1) from ("+sql+")";
     Long count=0l;
     List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
     Object obj=bll.getSingal(sqlCount);
     if(obj!=null)
     {
    	 count=Long.parseLong(obj.toString());
     }
     result.setList(list);
     result.setTotalCount(count);
		
		return result;
		
	}

	/**
	 * 功能：查询员工工资明细
	 * add by qxjiao 20100806
	 * @param id
	 * @param rowStartIdxAndCount
	 * @return
	 */
	
	public List getWageDetail(String id,String month,int... rowStartIdxAndCount){
		String sql = "SELECT" +
				" tv.emp_id, "
				+"te.chs_name,"
				+"ts.salary_point, "
				
				+"(SELECT tp.salary_point "
				+"FROM HR_C_SALARY_POINT tp "
				+"WHERE tv.salary_month > tp.effect_start_time "
				+"AND tv.salary_month < tp.effect_end_time) AS point_value,"
				
				+"tv.point_wage, "
				+"nvl((floor((tv.salary_month - ts.working_from) / (12 * 30)) + 1), 0) AS work_age, "
				+"tv.age_wage, "
				+"(ts.running_age +"
				+"nvl((floor((SYSDATE - ts.last_jion_runtime) / (12 * 30)) + 1), 0)) as running_age, "
				+"tv.operation_wage,"
				+"(SELECT wmsys.wm_concat(tt.overtime_type || SUM(GETHRDAYSBYID(tw.overtime_time_id)))"
				         +" FROM HR_J_WORKATTENDANCENEW tw,"
				              +" HR_C_OVERTIME          tt"
				         +" WHERE to_char(tw.attendance_date, 'yyyy-mm') =to_char(tv.salary_month, 'yyyy-mm')"
				           +" AND tt.overtime_type_id = tw.overtime_type_id"
				         + " and tw.emp_id=tv.emp_id"
				       +"  GROUP BY tw.emp_id,"
				              +"    tt.overtime_type) as over_time,"
                +"tv.overtime_wage,"
                +"GETHRVACATIONBYMONTH(tv.emp_id, tv.salary_month) "
                +" AS queqing,"
                +"tv.deduction_wage "
                +"FROM HR_C_SALARY_PERSONAL ts,"
                +"HR_J_SALARY_WAGE     tv ,"
                +"hr_j_emp_info te "
                +"WHERE ts.emp_id = tv.emp_id and te.emp_id = tv.emp_id and to_char(tv.salary_month,'yyyy-MM')='"+month+"' and ts.emp_id in("+id+") ";
		System.out.println(sql);
		List result = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		return result;
	}


}