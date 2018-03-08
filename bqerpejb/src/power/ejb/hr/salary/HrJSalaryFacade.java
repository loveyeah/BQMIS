package power.ejb.hr.salary;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.LogUtil;
import power.ejb.hr.salary.form.SalaryAmountForm;

/**
 * 薪酬主表
 * @author liuyi 090928
 */
@Stateless
public class HrJSalaryFacade implements HrJSalaryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="HrJSalaryDetailFacade")
	protected HrJSalaryDetailFacadeRemote detailRemote;

	/**
	 * 新增一条薪酬主表数据
	 */
	public HrJSalary save(HrJSalary entity) {
		LogUtil.log("saving HrJSalary instance", Level.INFO, null);
		try {
			entity.setSalaryId(bll.getMaxId("HR_J_SALARY", "SALARY_ID"));
			entityManager.persist(entity);			
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条薪酬主表数据
	 */
	public void delete(HrJSalary entity) {
		LogUtil.log("deleting HrJSalary instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJSalary.class, entity
					.getSalaryId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条薪酬主表数据
	 */
	public HrJSalary update(HrJSalary entity) {
		LogUtil.log("updating HrJSalary instance", Level.INFO, null);
		try {
			HrJSalary result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJSalary findById(Long id) {
		LogUtil.log("finding HrJSalary instance with id: " + id, Level.INFO,
				null);
		try {
			HrJSalary instance = entityManager.find(HrJSalary.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrJSalary> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrJSalary instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJSalary model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean judgetSalaryExist(Long deptId, String month,String enterpriseCode) {
		String sql = "select count(*) \n"
			+ "from HR_J_SALARY a,hr_j_emp_info b \n"
			+ "where a.is_use='Y' \n"
			+ "and b.is_use='Y' \n"
			+ "and a.enterprise_code='" + enterpriseCode + "' \n"
			+ "and b.enterprise_code='" + enterpriseCode + "' \n"
			+ "and a.emp_id=b.emp_id \n"
			+ "and b.dept_id=" + deptId;
		if(month != null && !month.equals(""))
			sql += " and to_char(a.salary_month,'yyyy-MM')='" + month + "' \n";
		Long num = Long.parseLong(bll.getSingal(sql).toString());
		if(num > 0)
			return true;
		else
			return false;
	}

	public List<HrJEmpInfo> findEmpByDeptId(Long deptId, String enterpriseCode) {
		String sql = "select *  \n"
			+ "from hr_j_emp_info a \n"
			+ "where a.is_use='Y' \n"
			+ "and a.enterprise_code='" + enterpriseCode + "' \n"
			+ "and a.emp_state='U' \n"
			+ "and a.dept_id = " + deptId 
			+ " order by a.emp_code \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		if(Integer.parseInt(bll.getSingal(sqlCount).toString())>0){
			return bll.queryByNativeSQL(sql, HrJEmpInfo.class);
		}
		else
			return null;
	}

	public void saveSalaryAmountRec(String method,
			List<SalaryAmountForm> arrlist) {
		if(method.equals("add"))
		{
			for(SalaryAmountForm form : arrlist)
			{
				HrJSalary salary = form.getSalary();
				List<HrJSalaryDetail> detailList = form.getDetailList();
				salary = this.save(salary);
				entityManager.flush();
				for(HrJSalaryDetail detail : detailList)
				{
					detail.setSalaryId(salary.getSalaryId());
					detailRemote.save(detail);
					entityManager.flush();
				}
			}
		}
		else
		{
			for(SalaryAmountForm form : arrlist)
			{
				HrJSalary salary = form.getSalary();
				List<HrJSalaryDetail> detailList = form.getDetailList();
				this.update(salary);
				for(HrJSalaryDetail detail : detailList)
				{
					String sql = "update HR_J_SALARY_DETAIL a set a.salary_money=" + detail.getSalaryMoney()
						 + " where a.salary_id=" + detail.getSalaryId() 
						 + " and a.salary_type_id=" + detail.getSalaryTypeId();
					bll.exeNativeSQL(sql);
				}
			}
		}
	}
	
	
	///add by fyyang 090928 工资查询
	@SuppressWarnings("unchecked")
	public PageObject findSalayByDept(String strMonth,Long deptId,String enterpriseCode)
	{
		String sqlSalaryCount=
			"select count(*)\n" +
			"  from HR_J_SALARY t, hr_j_emp_info a\n" + 
			" where t.emp_id = a.emp_id\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and to_char(t.salary_month, 'yyyy-MM') = '"+strMonth+"'\n" + 
			"   and a.dept_id ="+deptId;
        Long salaryCount=Long.parseLong(bll.getSingal(sqlSalaryCount).toString());
        if(salaryCount==0)
        {
        	//增加时
        	 String sql="select null as SALARY_ID,to_char(to_date('"+strMonth+"','yyyy-MM'),'yyyy-MM') as SALARY_MONTH,  a.emp_id,a.chs_name \n";
        	 String sqlTitle="select '薪酬ID','月份','员工id','人员姓名'\n";
        	 String dataIndex="select 'salaryId','month','empId','empName'\n";
        	 //------------------------------------------------------------------------------------------
        	String sqlSalaryType=
    			"select * from HR_C_SALARY_TYPE t\n" +
    			"where t.is_use='Y'\n" + 
    			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
    			"and t.is_need='1' order by t.salary_type_id asc ";
    		  List<HrCSalaryType> list=bll.queryByNativeSQL(sqlSalaryType, HrCSalaryType.class);
    		  if(list!=null&&list.size()>0)
    		  {
    			  for(HrCSalaryType model:list)
    			  {
    				  //标题
    				  sqlTitle+= ",(select t.salary_type_name\n" +
    	        	  "  from HR_C_SALARY_TYPE t\n" + 
    	        	  " where t.salary_type_id = "+model.getSalaryTypeId()+"\n" + 
    	        	  "   and t.is_use = 'Y'\n" + 
    	        	  "   and t.enterprise_code = '"+enterpriseCode+"')";
    				  dataIndex+=",'type"+model.getSalaryTypeId()+"'";
    				  
    				  if(model.getSalaryTypeId()==1)
    				  {
    					  //基本工资
    					  sql+=
    						  ",nvl((select  b.basis_salary from HR_C_BASIS_SALARY b\n" +
    						  "where to_date('"+strMonth+"','yyyy-MM')>=b.effect_start_time\n" + 
    						  "and to_date('"+strMonth+"','yyyy-MM')<=b.effect_end_time\n" + 
    						  "and b.is_use='Y' and b.enterprise_code='"+enterpriseCode+"' and rownum=1\n" + 
    						  "),0) salaryType1 \n";
    				  }
    				  else if(model.getSalaryTypeId()==2)
    				  {
    					//岗位薪点工资
    					  sql+=
    						  ", nvl((select\n" +
    						  " (select d.job_point from HR_C_POINT_SALARY d\n" + 
    						  "where d.check_station_grade=a.check_station_grade and d.salary_level=a.salary_level\n" + 
    						  "and d.is_use='Y' and d.enterprise_code='"+enterpriseCode+"' and rownum=1)*\n" + 
    						  "(select e.salary_point from HR_C_SALARY_POINT e\n" + 
    						  "where to_date('"+strMonth+"','yyyy-MM')>=e.effect_start_time\n" + 
    						  "and to_date('"+strMonth+"','yyyy-MM')<=e.effect_end_time\n" + 
    						  "and e.is_use='Y' and e.enterprise_code='"+enterpriseCode+"' and rownum=1)*\n" + 
    						  "(\n" + 
    						  "nvl((select 1+(f.increase_range/100) from HR_C_SALARY_POLICY f\n" + 
    						  "where f.station_id=a.station_id\n" + 
    						  "and f.is_use='Y' and f.enterprise_code='"+enterpriseCode+"' and rownum=1\n" + 
    						  "),1))\n" + 
    						  "\n" + 
    						  " from dual),0) salaryType2 \n";

    					  
    				  }
    				  else if(model.getSalaryTypeId()==3)
    				  {
    					 //工龄工资
    					  sql+=
    						  ",nvl((select\n" +
    						  "floor((sysdate- a.mission_date)/(12*30) )*\n" + 
    						  "nvl((select c.seniority_salary from HR_C_SENIORITY_SALARY c\n" + 
    						  "where to_date('"+strMonth+"','yyyy-MM')>=c.effect_start_time\n" + 
    						  "and to_date('"+strMonth+"','yyyy-MM')<=c.effect_end_time\n" + 
    						  "and c.is_use='Y' and c.enterprise_code='"+enterpriseCode+"'and rownum=1),0)\n" + 
    						  " from dual),0) salaryType3 \n";
    				  }
    				  else if(model.getSalaryTypeId()==4)
    				  {
    					  //特殊津贴
    					  sql+=
    						  ",nvl((select\n" +
    						  " (select floor((sysdate- a.mission_date)/(12*30) ) * g.operation_salary_point from HR_C_OPERATION_SALARY g\n" + 
    						  "where g.start_seniority<floor((sysdate- a.mission_date)/(12*30) ) and g.end_seniority>=floor((sysdate- a.mission_date)/(12*30) )\n" + 
    						  "and g.is_use='Y' and g.enterprise_code='"+enterpriseCode+"' and rownum=1)*\n" + 
    						  "(select e.salary_point from HR_C_SALARY_POINT e\n" + 
    						  "where to_date('"+strMonth+"','yyyy-MM')>=e.effect_start_time\n" + 
    						  "and to_date('"+strMonth+"','yyyy-MM')<=e.effect_end_time\n" + 
    						  "and e.is_use='Y' and e.enterprise_code='"+enterpriseCode+"' and rownum=1)*\n" + 
    						  "(\n" + 
    						  "nvl((select 1+(f.increase_range/100) from HR_C_SALARY_POLICY f\n" + 
    						  "where f.station_id=a.station_id\n" + 
    						  "and f.is_use='Y' and f.enterprise_code='"+enterpriseCode+"' and rownum=1\n" + 
    						  "),1))\n" + 
    						  "\n" + 
    						  "from dual),0) salaryType4 \n";

    				  }
    				  else if(model.getSalaryTypeId()==5)
    				  {
    					  //各类奖金
    					  sql+=",0 as salaryType5 \n";
    				  }
    				  else if(model.getSalaryTypeId()==6)
    				  {
    					  //加班工资
    					  sql+=", 0 as salaryType6 \n ";
    				  }
    				  else
    				  {
    					  sql+=", 0 as salaryType"+model.getSalaryTypeId()+" \n";
    				  }
    			  }
    		  }
    		 
    		  
    		//-----------------------------------------------------------------------------------
    		  sql+=",0 as TOTAL_SALARY \n"+
    			  "from hr_j_emp_info a\n" +
    			  // modified by liuyi 091224 
//    			  "where a.dept_id="+deptId+"  and  a.is_use='Y' and a.enterprise_code='"+enterpriseCode+"' and a.emp_state='U' ";
    			  "where a.dept_id="+deptId+"  and  a.is_use='Y' and a.enterprise_code='"+enterpriseCode+"' ";
    		  sqlTitle+=",'总工资'  from dual\n";
    		  dataIndex+=",'totalSalary'   from dual\n";
              List titleList=bll.queryByNativeSQL(sqlTitle+"\n union \n "+dataIndex);
        
              List dataList=new ArrayList();
              dataList=bll.queryByNativeSQL(sql);
              dataList.add(0, titleList.get(0));
              dataList.add(1, titleList.get(1));
              PageObject obj=new PageObject();
    		  obj.setList(dataList);
    		  obj.setTotalCount(Long.parseLong(list.size()+""));
    		  return obj;
        }
        else
        {
        	//修改时
        	String sql="select a.salary_id,to_char(a.salary_month,'yyyy-MM') ,a.emp_id,b.chs_name \n";
        	String sqlTitle="select '薪酬ID','月份','员工id','人员姓名'\n";
        	String dataIndex="select 'salaryId','month','empId','empName'\n";
        	//--------------------------------------------------------------------------------
        	String sqlSalaryDetailType=
        		"select *\n" +
        		"  from HR_J_SALARY_DETAIL tt\n" + 
        		" where tt.enterprise_code = 'hfdc'\n" + 
        		"   and tt.salary_id in\n" + 
        		"       (select t.salary_id\n" + 
        		"          from HR_J_SALARY t, hr_j_emp_info a\n" + 
        		"         where t.emp_id = a.emp_id\n" + 
        		"           and t.enterprise_code = '"+enterpriseCode+"'\n" + 
        		"           and a.enterprise_code = '"+enterpriseCode+"'\n" + 
        		"           and t.is_use = 'Y'\n" + 
        		"           and a.is_use = 'Y'\n" + 
        		"           and to_char(t.salary_month, 'yyyy-MM') = '"+strMonth+"'\n" + 
        		"           and a.dept_id ="+deptId+"\n" + 
        		"           and rownum = 1)    order by tt.salary_type_id asc ";
          List<HrJSalaryDetail> list=bll.queryByNativeSQL(sqlSalaryDetailType, HrJSalaryDetail.class);
          for(HrJSalaryDetail model:list)
          {
        	  sql+=
        		  ",(select c.salary_money  from HR_J_SALARY_DETAIL c\n" +
        		  "where c.salary_id=a.salary_id\n" + 
        		  "and c.salary_type_id="+model.getSalaryTypeId()+"\n" + 
        		  "and c.enterprise_code='"+enterpriseCode+"') as salaryType"+model.getSalaryTypeId();

        	  sqlTitle+= ",(select t.salary_type_name\n" +
        	  "  from HR_C_SALARY_TYPE t\n" + 
        	  " where t.salary_type_id = "+model.getSalaryTypeId()+"\n" + 
        	  "   and t.is_use = 'Y'\n" + 
        	  "   and t.enterprise_code = '"+enterpriseCode+"')";
        	  dataIndex+=",'type"+model.getSalaryTypeId()+"'";


          }
          //-------------------------------------------------------------------------------------------
          sql+=",a.TOTAL_SALARY \n"+
        	  " from HR_J_SALARY a, hr_j_emp_info b\n" +
        	  "where a.emp_id = b.emp_id\n" + 
        	  "  and b.is_use = 'Y'\n" + 
        	  "  and b.enterprise_code = '"+enterpriseCode+"'   and b.dept_id="+deptId+"";
          sqlTitle+=",'总工资' from dual\n";
          dataIndex+=",'totalSalary'  from dual\n";
          List titleList=bll.queryByNativeSQL(sqlTitle+"\n union \n "+dataIndex);
          List dataList=new ArrayList();
          dataList=bll.queryByNativeSQL(sql);
          dataList.add(0, titleList.get(0));
          dataList.add(1, titleList.get(1));
          PageObject obj=new PageObject();
		  obj.setList(dataList);
		  obj.setTotalCount(Long.parseLong(list.size()+""));
		  return obj;
        }
		
	}

}