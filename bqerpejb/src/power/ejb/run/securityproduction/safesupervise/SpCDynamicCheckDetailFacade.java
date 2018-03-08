package power.ejb.run.securityproduction.safesupervise;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class SpCDynamicCheckDetailFacade implements
		SpCDynamicCheckDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public SpCDynamicCheckDetail save(SpCDynamicCheckDetail entity) {
		LogUtil.log("saving SpCDynamicCheckDetail instance", Level.INFO, null);
		try {
			entity.setDetailId(bll.getMaxId("SP_C_DYNAMIC_CHECK_DETAIL", "detail_id"));
			entity.setIsUse("Y");
			entity.setEntryDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(String ids) {
		String sql=
			"update SP_C_DYNAMIC_CHECK_DETAIL a\n" +
			"   set a.is_use = 'N'\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.detail_id in ("+ids+")";
		bll.exeNativeSQL(sql);

	}

	
	public SpCDynamicCheckDetail update(SpCDynamicCheckDetail entity) {
		LogUtil
				.log("updating SpCDynamicCheckDetail instance", Level.INFO,
						null);
		try {
			SpCDynamicCheckDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpCDynamicCheckDetail findById(Long id) {
		LogUtil.log("finding SpCDynamicCheckDetail instance with id: " + id,
				Level.INFO, null);
		try {
			SpCDynamicCheckDetail instance = entityManager.find(
					SpCDynamicCheckDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private PageObject findDynamicList(String year,String season,String enterpriseCode,String strWhere,int... rowStartIdxAndCount)
	{
		String sql=
			"select t.main_id,\n" +
			"       t.year,\n" + 
			"       t.season,\n" + 
			"       a.detail_id,\n" + 
			"       a.exist_question,\n" + 
			"       a.whole_step,\n" + 
			"       a.avoid_step,\n" + 
			"       to_char(a.plan_date, 'yyyy-MM-dd'),\n" + 
			"       to_char(a.actual_date, 'yyyy-MM-dd'),\n" + 
			"       a.duty_dept_code,\n" + 
			"       GETDEPTNAME(a.duty_dept_code),\n" + 
			"       a.duty_by,\n" + 
			"       GETWORKERNAME(a.duty_by),\n" + 
			"       a.super_dept_code,\n" + 
			"       GETDEPTNAME(a.super_dept_code),\n" + 
			"       a.super_by,\n" + 
			"       GETWORKERNAME(a.super_by),\n" + 
			"       a.no_reason,\n" + 
			"       a.issue_proerty,\n" + 
			"  c.noFinishNum, c.finishNum, c.totalNum,decode(nvl(c.totalNum, 0), 0, 0, c.finishNum / c.totalNum) \n"+
            //add by fyyang 20100510 完成情况 1---已完成 2---逾期未完 3---整改之中
			",(case when a.actual_date is not null then  1\n" +
			"else (case when sysdate>=a.plan_date then  2 else 3 end)\n" + 
			"end) \n"+
            //--------------------------------------------------------------
			"  from SP_C_DYNAMIC_CHECK_DETAIL a, SP_C_DYNAMIC_CHECK_MAIN t\n" +
			",\n" +
			"       (select bb.year,\n" + 
			"               bb.season,\n" + 
			"               sum(decode(aa.actual_date, null, 1, 0)) noFinishNum,\n" + 
			"               sum(decode(aa.actual_date, null, 0, 1)) finishNum,\n" + 
			"               sum(1) totalNum\n" + 
			"          from SP_C_DYNAMIC_CHECK_DETAIL aa, SP_C_DYNAMIC_CHECK_MAIN bb\n" + 
			"         where aa.main_id = bb.main_id\n" + 
			"         and aa.is_use='Y' and bb.is_use='Y' \n"+
			"         and aa.enterprise_code = '"+enterpriseCode+"'  and bb.enterprise_code = '"+enterpriseCode+"' \n"+
			"         group by bb.year, bb.season\n" + 
			"\n" + 
			"        ) c \n"+
			" where a.main_id = t.main_id\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n"+
			"  and t.year = c.year\n" +
			"  and t.season = c.season";

		
		if(year!=null&&!year.equals(""))
		{
			sql+="   and t.year = '"+year+"'\n" ;
		}
		if(season!=null&&!season.equals(""))
		{
			sql+="   and t.season = '"+season+"'\n" ;
		}
		sql+=strWhere;	

		String sqlCount="select count(*) from ("+sql+")tt";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
        PageObject obj=new PageObject();
        obj.setList(list);
        obj.setTotalCount(totalCount);
		return obj;
		
	}
	
	public PageObject findDynamicListForCheck(String year,String season,String enterpriseCode,String entryBy,int... rowStartIdxAndCount)
	{
		String strWhere="   and a.entry_by='"+entryBy+"'  and a.actual_date is  null \n";
		PageObject obj= this.findDynamicList(year, season, enterpriseCode, strWhere, rowStartIdxAndCount);
	    return obj;
		
	}
	
	public PageObject findDynamicListForPlan(String year,String season,String enterpriseCode,String dutyBy,int... rowStartIdxAndCount)
	{
		String strWhere="   and a.duty_by='"+dutyBy+"'  and a.actual_date is  null \n";
		PageObject obj=this.findDynamicList(year, season, enterpriseCode, strWhere, rowStartIdxAndCount);
		return obj;
	}
	
	public PageObject findDynamicListForQuery(String year,String season,String enterpriseCode,String existQuestion,String isFinish,int... rowStartIdxAndCount)
	{
		String strWhere="";
		if(existQuestion!=null)
		{
			strWhere+="   and a.exist_question like '%"+existQuestion+"%'\n";
		}
		if(isFinish.equals("Y"))
		{
			strWhere+=" and a.actual_date is not null \n";
		}
		else if(isFinish.equals("N"))
		{
			strWhere+=" and a.actual_date is  null \n";
		}
		
		PageObject obj=this.findDynamicList(year, season, enterpriseCode, strWhere, rowStartIdxAndCount);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject queryDynamicGatherList(String monthDate,String enterpriseCode,String problemKind,int... rowStartIdxAndCount)
	{
		PageObject obj=new PageObject();
		
		   GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(monthDate.substring(0,4)),Integer.parseInt(monthDate.substring(5, 7)) - 1, 1);
           gc.add(Calendar.MONTH, 1);
           int nextMInt = gc.get(Calendar.MONTH) + 1;
           String nextM = nextMInt > 9 ? String.valueOf(nextMInt) : "0" + String.valueOf(nextMInt);
           String nextMonth = String.valueOf(gc.get(Calendar.YEAR)) + "-" + nextM;
           
           String sql="";
           if(problemKind!=null&&problemKind.equals("6"))
           {
        	   //25项反措
        	   sql=
        		   "select a.charge_dept,\n" +
        		   "       GETDEPTNAME(a.charge_dept),\n" + 
        		   "       sum(1) total,\n" + 
        		   "       sum(decode(a.amend_finish_date, null, 0, '', 0, 1)) finish,\n" + 
        		   "       sum(case\n" + 
        		   "             when a.amend_finish_date is not null then\n" + 
        		   "              0\n" + 
        		   "             else\n" + 
        		   "              (case\n" + 
        		   "             when sysdate >= a.plan_finish_date then\n" + 
        		   "              1\n" + 
        		   "             else\n" + 
        		   "              0\n" + 
        		   "           end) end) notfinish,\n" + 
        		   "\n" + 
        		   "       (decode(sum(1),\n" + 
        		   "               0,\n" + 
        		   "               0,\n" + 
        		   "               sum(decode(a.amend_finish_date, null, 0, '', 0, 1)) / sum(1)) * 100) || '%'\n" + 
        		   "  from SP_J_ANTI_ACCIDENT_AMEND a\n" + 
        		   " where a.plan_finish_date >= to_date('"+monthDate+"' || '-01', 'yyyy-MM-dd')\n" + 
        		   "   and a.plan_finish_date < to_date('"+nextMonth+"' || '-01', 'yyyy-MM-dd')\n" + 
//        		   "   and a.is_use = 'Y'\n" + 
//        		   "   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
        		   " group by a.charge_dept \n"+
        		   " order by  GETDEPTNAME(a.charge_dept)";

           }
           else
           {
        	   sql="select a.duty_dept_code,\n" +
        	   "       GETDEPTNAME(a.duty_dept_code),\n" + 
        	   "       sum(1) total,\n" + 
        	   "       sum(decode(a.actual_date, null, 0, '', 0, 1)) finish,\n" + 
        	   "       sum(case\n" + 
        	   "             when a.actual_date is not null then\n" + 
        	   "              0\n" + 
        	   "             else\n" + 
        	   "              (case\n" + 
        	   "             when sysdate >= a.plan_date then\n" + 
        	   "              1\n" + 
        	   "             else\n" + 
        	   "              0\n" + 
        	   "           end) end) notfinish,\n" + 
        	   "\n" + 
        	   "       (decode(sum(1),\n" + 
        	   "               0,\n" + 
        	   "               0,\n" + 
        	   "               sum(decode(a.actual_date, null, 0, '', 0, 1)) / sum(1)) * 100) || '%'\n" + 
        	   "\n" + 
        	   "  from SP_C_DYNAMIC_CHECK_DETAIL a, SP_C_DYNAMIC_CHECK_MAIN b\n" + 
        	   " where a.main_id = b.main_id\n" + 
        	   "   and b.season = '"+problemKind+"'\n" + 
        	   "   and a.plan_date >= to_date('"+monthDate+"' || '-01', 'yyyy-MM-dd')\n" + 
        	   "   and a.plan_date < to_date('"+nextMonth+"' || '-01', 'yyyy-MM-dd')\n" + 
        	   "and a.enterprise_code='"+enterpriseCode+"'\n" +
        	   "  and b.enterprise_code='"+enterpriseCode+"'\n" + 
        	   "  and a.is_use='Y'\n" + 
        	   "  and b.is_use='Y' \n"+
        	   " group by a.duty_dept_code \n"+
        	   " order by GETDEPTNAME(a.duty_dept_code)";
           }
           List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
           String sqlCount=" select count(1) from ("+sql+") tt";
           Long totalCount= Long.parseLong(bll.getSingal(sqlCount).toString());
           obj.setList(list);
           obj.setTotalCount(totalCount);
		   return obj;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public PageObject findDynamicDetailForGather(String monthDate,String enterpriseCode,String problemKind,String deptCode,int... rowStartIdxAndCount)
	{
		 GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(monthDate.substring(0,4)),Integer.parseInt(monthDate.substring(5, 7)) - 1, 1);
         gc.add(Calendar.MONTH, 1);
         int nextMInt = gc.get(Calendar.MONTH) + 1;
         String nextM = nextMInt > 9 ? String.valueOf(nextMInt) : "0" + String.valueOf(nextMInt);
         String nextMonth = String.valueOf(gc.get(Calendar.YEAR)) + "-" + nextM;
		if(problemKind!=null&&problemKind.equals("6"))
		{
			PageObject obj=new PageObject();
			String sql=
				"select a.amend_id,\n" +
				"       '',\n" + 
				"       '6',\n" + 
				"       a.checkup_id,\n" + 
				"       a.exist_problem,\n" + 
				"       a.amend_measure,\n" + 
				"       a.before_amend_measure,\n" + 
				"       to_char(a.plan_finish_date, 'yyyy-MM-dd'),\n" + 
				"       to_char(a.amend_finish_date, 'yyyy-MM-dd'),\n" + 
				"       a.charge_dept,\n" + 
				"       GETDEPTNAME(a.charge_dept),\n" + 
				"       a.charge_by,\n" + 
				"       GETWORKERNAME(a.charge_by),\n" + 
				"       a.supervise_dept,\n" + 
				"       GETDEPTNAME(a.supervise_dept),\n" + 
				"       a.supervise_by,\n" + 
				"       GETWORKERNAME(a.supervise_by),\n" + 
				"       a.no_amend_reason,\n" + 
				"       a.problem_kind,\n" + 
				"       0 as data1,\n" + 
				"       0 as data2,\n" + 
				"       0 as data3,\n" + 
				"       0 as data4,\n" + 
				"       (case\n" + 
				"         when a.amend_finish_date is not null then\n" + 
				"          1\n" + 
				"         else\n" + 
				"          (case\n" + 
				"         when sysdate >= a.plan_finish_date then\n" + 
				"          2\n" + 
				"         else\n" + 
				"          3\n" + 
				"       end) end)\n" + 
				"  from SP_J_ANTI_ACCIDENT_AMEND a\n" + 
				" where a.plan_finish_date >= to_date('"+monthDate+"' || '-01', 'yyyy-MM-dd')\n" + 
				"   and a.plan_finish_date < to_date('"+nextMonth+"' || '-01', 'yyyy-MM-dd')\n" + 
//				"   and a.is_use = 'Y'\n" + 
//				"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   and a.charge_dept = '"+deptCode+"'";

			  List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	           String sqlCount=" select count(1) from ("+sql+") tt";
	           Long totalCount= Long.parseLong(bll.getSingal(sqlCount).toString());
	           obj.setList(list);
	           obj.setTotalCount(totalCount);
	           return obj;
		}
		else
		{
		
		  String strWhere=
			" and a.plan_date >= to_date('"+monthDate+"' || '-01', 'yyyy-MM-dd')\n" +
			"   and a.plan_date <= to_date('"+nextMonth+"' || '-01', 'yyyy-MM-dd')\n" + 
		 	"   and a.duty_dept_code = '"+deptCode+"'";
          return  this.findDynamicList(null, problemKind, enterpriseCode, strWhere, rowStartIdxAndCount);
		}
	}
	
	
	private SpCDynamicCheckMain findModelByYearAndSeason(SpCDynamicCheckMain mainEntity)
	{
		String sql=
			"select * from SP_C_DYNAMIC_CHECK_MAIN t\n" +
			"where t.is_use='Y'\n" + 
			"and t.enterprise_code='"+mainEntity.getEnterpriseCode()+"'\n" + 
			"and t.year='"+mainEntity.getYear()+"'\n" + 
			"and t.season='"+mainEntity.getSeason()+"'\n" + 
			"and t.entry_by='"+mainEntity.getEntryBy()+"'";
		List<SpCDynamicCheckMain> list=bll.queryByNativeSQL(sql, SpCDynamicCheckMain.class);
		if(list!=null&&list.size()>0)
		{
			return (SpCDynamicCheckMain)list.get(0);
		}
		else
		{
			return null;
		}

	}
	
	
	public void importInfo(SpCDynamicCheckMain mainEntity,List<SpCDynamicCheckDetail> detailList)
	{
		StringBuffer sqlsb = new StringBuffer();
		sqlsb.append("begin \n");
		String sql="";
		SpCDynamicCheckMain mainModel=this.findModelByYearAndSeason(mainEntity);
	    Long maxDetailId=bll.getMaxId("SP_C_DYNAMIC_CHECK_DETAIL", "detail_id");
		if(mainModel==null)
		{
			mainEntity.setMainId(bll.getMaxId("Sp_c_Dynamic_Check_Main", "main_id"));
			sql=
				"insert into Sp_c_Dynamic_Check_Main aa(aa.main_id,aa.year,aa.season," +
				"aa.is_use,aa.enterprise_code,aa.entry_by,aa.entry_date,aa.status)\n" +
				"values("+mainEntity.getMainId()+","+mainEntity.getYear()+","+mainEntity.getSeason()+",'Y','"+mainEntity.getEnterpriseCode()+"',"+mainEntity.getEntryBy()+",sysdate,'1'); \n";
			sqlsb.append(sql);
		}
		else
		{
			
			mainEntity=mainModel;
		}
		int i=0;
		String insertSql=
			"insert into SP_C_DYNAMIC_CHECK_DETAIL t\n" +
			"(t.detail_id,t.main_id,t.exist_question,t.whole_step,t.avoid_step,\n" + 
			"t.plan_date,t.duty_dept_code,t.duty_by,t.super_dept_code,t.super_by,\n" + 
			"t.issue_proerty,t.is_use,t.enterprise_code,t.entry_by,t.entry_date) \n";

		String valueSql="";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		for(SpCDynamicCheckDetail detailModel:detailList)
		{
			
			detailModel.setMainId(mainEntity.getMainId());
			detailModel.setDetailId(maxDetailId+i);
			 valueSql=insertSql+
				"select "+detailModel.getDetailId()+","+detailModel.getMainId()+",'"+detailModel.getExistQuestion()+"','"+detailModel.getWholeStep()+"','"+detailModel.getAvoidStep()+"',\n" +
				"to_date('"+sf.format(detailModel.getPlanDate())+"','yyyy-MM-dd'),"+" GETFirstLevelBYID(a.dept_id),a.emp_code,GETFirstLevelBYID(b.dept_id),b.emp_code,\n" + 
				"'"+detailModel.getIssueProerty()+"','Y','"+detailModel.getEnterpriseCode()+"','"+detailModel.getEntryBy()+"',sysdate\n" + 
				"from hr_j_emp_info a,hr_j_emp_info b\n" + 
				"where a.chs_name='"+detailModel.getDutyBy()+"'\n" + 
				"and b.chs_name='"+detailModel.getSuperBy()+"'; \n";
			 sqlsb.append(valueSql);
			 i++;
             
		}
		sqlsb.append("commit;\n");
		sqlsb.append("end;\n");
		bll.exeNativeSQL(sqlsb.toString());
	}
	
	
	public String  checkInputManName(String dutyNames,String superNames,String dutyCr,String superCr)
	{
		String msg="";
		String [] arrDutyName=dutyNames.split(",");
		String [] arrSuperName=superNames.split(",");
		
		String sql=
			"select count(*) from hr_j_emp_info t\n" +
			"where t.chs_name in ("+dutyNames+")";
        if(arrDutyName.length==Integer.parseInt(bll.getSingal(sql).toString()))
        {
        	sql="select count(*) from hr_j_emp_info t\n" +
			"where t.chs_name in ("+superNames+")";
        	if(arrSuperName.length==Integer.parseInt(bll.getSingal(sql).toString()))
        	{
        		msg= "";
        	}
        	else
        	{
        		int i=this.findErrorData(arrSuperName);
        		msg=i+"";
        		 if(i!=-1)
        		 { 
        			 String [] arrSuperCr=superCr.split(";");
        			 String [] crData=arrSuperCr[i].split(",");
        			 crData[0]=(Integer.parseInt(crData[0])+1)+"";
        			 crData[1]=(Integer.parseInt(crData[1])+1)+"";
        			 msg="第"+crData[0]+"行第"+crData[1]+"列";
        		 }
        		 
        	}
        	
        }
        else
        {
        	int i=this.findErrorData(arrDutyName);
        	msg=i+"";
        	 if(i!=-1)
    		 {
        		 String [] arrDutyCr=dutyCr.split(";");
    			 String [] crData=arrDutyCr[i].split(",");
    			 crData[0]=(Integer.parseInt(crData[0])+1)+"";
    			 crData[1]=(Integer.parseInt(crData[1])+1)+"";
    			 msg="第"+crData[0]+"行第"+crData[1]+"列";
    		 }
        }
		
		
	     
		
		return msg;
	}
	
	
	
	private int findErrorData(String [] workName)
	{
		int start=0;
		int end=workName.length-1;
		
		for(int i=0;i<workName.length;i++)
		{
			if(start==end) return start;
			
			int k=(end-start)/2;
			String strArrOne="";
			for(int j=start;j<=k;j++)
			{
				if(strArrOne.equals(""))
				{
				strArrOne=workName[j];
				}
				else
				{
					strArrOne +=","+workName[j];
				}
			}
			
			if(strArrOne.equals("")) return -1;
			else
			{
				String sql="select count(*) from hr_j_emp_info t\n" +
				"where t.chs_name in ("+strArrOne+")";
				 if(strArrOne.split(",").length==Integer.parseInt(bll.getSingal(sql).toString()))
				 {
					 strArrOne="";
						for(int j=k+1;j<=end;j++)
						{
							if(strArrOne.equals(""))
							{
							 strArrOne=workName[j];
							}
							else
							{
								strArrOne +=","+workName[j];
							}
						}
						
						 sql="select count(*) from hr_j_emp_info t\n" +
						"where t.chs_name in ("+strArrOne+")";
						 if(strArrOne.split(",").length==Integer.parseInt(bll.getSingal(sql).toString()))
						 {
							 return -1;
						 }
						 else
						 {
							 start=k+1;
						 }
					 
				 }
				 else
				 {
					end=k; 
				 }
			}
			
		}
		return -1;
	}
	


}