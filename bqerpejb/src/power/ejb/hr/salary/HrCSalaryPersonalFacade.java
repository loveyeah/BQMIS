package power.ejb.hr.salary;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

import power.ejb.hr.LogUtil;
import power.ejb.hr.archives.HrCPunish;
import power.ejb.hr.salary.form.SalaryPersonalForm;

/**
 * Facade for entity HrCSalaryPersonal.
 * 
 * @see power.ejb.hr.salary.HrCSalaryPersonal
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSalaryPersonalFacade implements HrCSalaryPersonalFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public  HrCSalaryPersonal  findPersonalRec(Long empId,Long deptId)
	{
		String sql=
			"select * from HR_C_SALARY_PERSONAL  a\n" +
			"where a.emp_id='"+empId+"'\n" + 
		//	"and a.dept_id='"+deptId+"'\n" + 
			"and a.is_use='Y'";
  List<HrCSalaryPersonal>  list=bll.queryByNativeSQL(sql, HrCSalaryPersonal.class);
  if(list!=null&&list.size()>0)
  {
	  return  list.get(0);
  }else
  {
		return null;
  }
		
	}
	
	public List<HrCSalaryPersonal> findPersonalById(Long empId){
		String sql=
			"select * from HR_C_SALARY_PERSONAL  a\n" +
			"where a.emp_id='"+empId+"'\n" + 
		//	"and a.dept_id='"+deptId+"'\n" + 
			"and a.is_use='Y'";
  List<HrCSalaryPersonal>  list=bll.queryByNativeSQL(sql, HrCSalaryPersonal.class);
  return list;
	}
	
	public  void insertSaralyPersonal(List<HrCSalaryPersonal> addOrUpdateList)
	{
		for(HrCSalaryPersonal  saralyModel :addOrUpdateList)
		{
			HrCSalaryPersonal  saralyPersonal=this.findPersonalRec(saralyModel.getEmpId(),saralyModel.getDeptId());
			if(saralyPersonal==null)
			{//增加记录
				this.save(saralyModel);
				 entityManager.flush();
			}else
			{//修改记录
				saralyModel.setIsUse("Y");
				saralyModel.setEnterpriseCode(saralyPersonal.getEnterpriseCode());
				saralyModel.setSalaryPersonalId(saralyPersonal.getSalaryPersonalId());
				this.update(saralyModel);
			}
		}
	}
	public Long  getIdBydeptName(String  deptName,String enterpriseCode)
	{
		Long deptId=0l;
		String sql=
			"select distinct a.dept_id \n" +
			"from HR_C_SALARY_PERSONAL  a,hr_c_dept  b\n" +
			"where  b.dept_name='"+deptName+"'\n" + 
			"and  b.dept_id=a.dept_id\n" + 
			"and  b.is_use='Y'\n" + //update by sychen 20100902
//			"and  b.is_use='U'\n" + 
			"and a.is_use='Y'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'\n" + 
			"and b.enterprise_code='"+enterpriseCode+"'";
//		System.out.println("the deptid"+sql);
    Object obj=bll.getSingal(sql);
    if(obj!=null)
    {
    	deptId=Long.parseLong(obj.toString());
    	return  deptId;
    	
    }
    else 
    {
		return null;
    }
		
	}
	public  Long  getIdByStationName(String stationName,String enterpriseCode)
	{
		Long stationId=0l;
		String sql=
			"select distinct b.quantify_id\n" +
			"  from HR_C_STATION_QUANTIFY b\n" + 
			" where b.station_name = '"+stationName+"'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'";

			
		Object obj=bll.getSingal(sql);
	    if(obj!=null)
	    {
	    	stationId=Long.parseLong(obj.toString());
	    	return  stationId;
	    	
	    }
	    else 
	    {
			return null;
	    }
			
		
	}
	public void save(HrCSalaryPersonal entity) {
		LogUtil.log("saving HrCSalaryPersonal instance", Level.INFO, null);
		try {
			entity.setSalaryPersonalId(bll.getMaxId("HR_C_SALARY_PERSONAL", "salary_personal_id"));
//			entity.setQuentityId(1l);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrCSalaryPersonal entity) {
		LogUtil.log("deleting HrCSalaryPersonal instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCSalaryPersonal.class, entity
					.getSalaryPersonalId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSalaryPersonal update(HrCSalaryPersonal entity) {
		LogUtil.log("updating HrCSalaryPersonal instance", Level.INFO, null);
		try {
//			entity.setQuentityId(1l);
			HrCSalaryPersonal result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSalaryPersonal findById(Long id) {
		LogUtil.log("finding HrCSalaryPersonal instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSalaryPersonal instance = entityManager.find(
					HrCSalaryPersonal.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void empChangeRecord(String enterpriseCode)
	{
		//人力资源新加
	
		String insertSql = "insert into HR_C_SALARY_PERSONAL\n" +
				"  (salary_personal_id, emp_id, dept_id,running_age, is_Use, enterprise_Code)\n" + 
				"  select (select nvl(max(salary_personal_id),0) from HR_C_SALARY_PERSONAL)+rownum ,\n" + 
				"         c.emp_id,\n" + 
				"         c.dept_id,\n" + 
				"         '0',\n" + 
				"         c.is_use,\n" + 
				"         c.enterprise_code\n" + 
				"    from hr_j_emp_info c\n" + 
				"   where not exists (select c.*\n" + 
				"            from HR_C_SALARY_PERSONAL a, hr_j_emp_info b\n" + 
				"           where a.emp_id = c.emp_id\n" + 
				"             and a.is_use = 'Y'\n" + 
				"             and c.is_use = 'Y'\n" + 
				"             and a.enterprise_code = '"+enterpriseCode+"')";
			
		 bll.exeNativeSQL(insertSql);
		
		 //人力资源主表发生部门变动时更新 Modified by ghzhou 20100827
		 String updateSql = 
			 "update HR_C_SALARY_PERSONAL p set p.dept_id =\n" +
			 "(select i.dept_id from hr_j_emp_info i where p.emp_id=i.emp_id)";

		 bll.exeNativeSQL(updateSql);
		 //修改薪酬个人维护表记录
		String sqlStr = "update HR_C_SALARY_PERSONAL a\n" +
			"   set a.is_use = 'N'\n" + 
			" where a.emp_id not in (select emp_id from hr_j_emp_info)";

		bll.exeNativeSQL(sqlStr);
	}
	
	public void runAgeChangeRecord(String ids)
	{
		String sql = "update HR_C_SALARY_PERSONAL p\n" +
			"   set p.running_age = p.running_age + 1\n" + 
			" where p.salary_personal_id in ("+ids+")\n" + 
			"   and p.running_age <> 0";

		bll.exeNativeSQL(sql);
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findSalaryPersonalList(String deptID,String fuzzy,String enterpriseCode,final int...rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		
		String sql = "select a.salary_personal_id,\n" +
			"       a.emp_id,\n" + 
			"       a.dept_id,\n" + 
			"       to_char(a.working_from, 'YYYY-MM-DD'),\n" + 
			"       to_char(a.join_from, 'YYYY-MM-DD'),\n" + 
			"       a.running_age,\n" + 
			"       a.remain_salary,\n" + 
			"       a.point_salary_adjust,\n" + 
			"       a.memo,\n" + 
			"       b.new_emp_code,\n" + 
			"       b.chs_name,\n" + 
			"       c.dept_code,\n" + 
			//"       c.dept_name,\n" +  //modify by fyyang 20100720
			" decode(GETFirstLevelBYID(c.dept_id),c.dept_code,'',GETDEPTNAME(GETFirstLevelBYID(c.dept_id))||'_')||c.dept_name,\n"+
			"       a.month_award  ,\n" +
			"       e.station_name,\n" +
			"       e.quantify_proportion ,\n" +//modify by  wpzhu 增加查询薪点和岗位信息
			"       a.salary_point,\n" +
			"       e.quantify_id,\n" +
			"      to_char( a.last_jion_runtime,'yyyy-MM-dd'),\n" +
			"       a.runage_flag,n.emp_type\n" + 
			"  from HR_C_SALARY_PERSONAL a, HR_J_EMP_INFO b,HR_C_STATION_QUANTIFY  e, hr_c_dept c,hr_j_newemployee n\n" + 
			" where  e.quantify_id(+)=a.quantify_id  \n" +
			"   and a.emp_id = b.emp_id\n" + 
			"   and a.dept_id = c.dept_id\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and c.is_use = 'Y'\n" + //update by sychen 20100902
//			"   and c.is_use = 'U'\n" + 
			"   and e.is_use(+) = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and e.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and c.enterprise_code = '"+enterpriseCode+"' and n.emp_id(+) = a.emp_id";
		

		String whereStr = "";
		if (fuzzy != null && fuzzy.length() > 0) {
			whereStr +=" and b.emp_code || b.chs_name like '%"+fuzzy+"%' \n";
		}
		//add by ypan 20100728
		if(deptID!=null&&!"".equals(deptID))
		{
			//whereStr+="and a.dept_id='"+deptID+"'";
            //modify by fyyang 20100802 部门级联查询
			whereStr+=" and "+deptID+" in ( select  t.dept_id from hr_c_dept t where t.is_use='Y' start with t.dept_id=c.dept_id  CONNECT BY PRIOR t.pdept_id = t.dept_id)";//update by sychen 20100902
//			whereStr+="and "+deptID+" in ( select  t.dept_id from hr_c_dept t where t.is_use='U' start with t.dept_id=c.dept_id  CONNECT BY PRIOR t.pdept_id = t.dept_id)";

		}
		sql += whereStr;
//		System.out.println("the sql"+sql);
		String sqlCount="select count(*) from ("+sql+")tt";
        sql+="   order by GETFirstLevelBYID(c.dept_id),c.dept_code ";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	@SuppressWarnings("unchecked")
	public SalaryPersonalForm getRunStationList(String empCode) {
		SalaryPersonalForm model = null;
		
		String sql = "select a.emp_id,\n" +
			"       a.emp_code,\n" + 
			"       b.station_id,\n" + 
			"       b.station_type_id,\n" + 
			"       c.station_type_name\n" + 
			"  from hr_j_emp_info a, hr_c_station b, hr_c_station_type c\n" + 
			" where a.station_id = b.station_id\n" + 
			"   and b.station_type_id = c.station_type_id\n" + 
			"   and a.new_emp_code = '"+empCode+"'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and b.is_use = 'Y'\n" + //update by sychen 20100902
//			"   and b.is_use = 'U'\n" + 
			"   and c.is_use = 'Y'";//update by sychen 20100902
//		"   and c.is_use = 'U'";
		
		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			model = new SalaryPersonalForm();
			Object[] f = list.get(0);
			if (f[0] != null)
				model.setEmpId(Long.parseLong(f[0].toString()));
			if (f[1] != null)
				model.setEmpCode(f[1].toString());
			if (f[2] != null)
				model.setStationId(Long.parseLong(f[2].toString()));
			if (f[3] != null)
				model.setStationTypeId(Long.parseLong(f[3].toString()));
			if(f[4] != null)
				model.setStationTypeName(f[4].toString());
		}  
		return model;
	}
	
}