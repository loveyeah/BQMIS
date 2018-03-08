package power.ejb.manage.project;

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

/**
 * Facade for entity PrjJCheckMeetSign.
 * 
 * @see power.ejb.manage.project.PrjJCheckMeetSign
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJCheckMeetSignFacade implements PrjJCheckMeetSignFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	
	
	public String findProjectNo(String dept, String date)
	{
		

		String sql1 = String
				.format("select fun_spellcode('%s') from dual", dept);
		String deptName = bll.getSingal(sql1).toString();
		System.out.println("部门名称首字母拼写为："+deptName);
		String keyword = "Q/CDT-"+deptName+"-"+date;
		int num = Integer.parseInt((bll.getSingal("select count(1) from" +
				" prj_j_check_meet_sign" +
				" where report_code   like '%"+keyword+"%'").toString()));
		if(num<9)
		{
			keyword+="-0"+(num+1);
		}
		else
		{
			keyword+="-"+(num+1);
		}
//		System.out.println("the keyword"+keyword);
		return keyword;
	
		
		
	}
	public PageObject  findCheckSignById(Long checkSignById,String enterpriseCode)
	{
		String sql=
			"select " +
			"s.equ_check_text," +
			"s.safety_check_text," +
			"s.manage_check_text," +
			"s.finance_check_text," +
			"s.audit_check_text,\n" +
			"s.equ_check_by," +
			"(SELECT wmsys.wm_concat(p.chs_name)" +
			" FROM hr_j_emp_info p" +
			" WHERE instr(','||s.equ_check_by||',',','||p.emp_code||',')<>0),\n" +
			"s.safety_check_by," +
			"(SELECT wmsys.wm_concat(p.chs_name)" +
			" FROM hr_j_emp_info p" +
			" WHERE instr(','||s.safety_check_by||',',','||p.emp_code||',')<>0),\n" +
			"s.manage_check_by ," +
			"(SELECT wmsys.wm_concat(p.chs_name)" +
			" FROM hr_j_emp_info p" +
			" WHERE instr(','||s.manage_check_by||',',','||p.emp_code||',')<>0),\n" +
			"s.finance_check_by," +
			"(SELECT wmsys.wm_concat(p.chs_name)" +
			" FROM hr_j_emp_info p" +
			" WHERE instr(','||s.finance_check_by||',',','||p.emp_code||',')<>0),\n" +
			"s.audit_check_by ," +
			"(SELECT wmsys.wm_concat(p.chs_name)" +
				" FROM hr_j_emp_info p" +
				" WHERE instr(','||s.audit_check_by||',',','||p.emp_code||',')<>0),\n" + 
			"to_char(s.equ_check_date,'yyyy-MM-dd') ,\n" + 
			"to_char(s.safety_check_date,'yyyy-MM-dd'),\n" + 
			"to_char(s.manage_check_date,'yyyy-MM-dd'),\n" + 
			"to_char(s.finance_check_date,'yyyy-MM-dd'),\n" + 
			"to_char(s.audit_check_date,'yyyy-MM-dd')\n" + 
			" from PRJ_J_CHECK_MEET_SIGN  s \n  " +
			" where s.check_sign_id='"+checkSignById+"'\n" +
			"and s.is_use='Y'   and    s.enterprise_code='"+enterpriseCode+"'";
      PageObject result=new PageObject();
//      System.out.println("the sql"+sql);
      List list=bll.queryByNativeSQL(sql);
      result.setList(list);
		return result;
		
	}

	public PageObject  getPrjCheckMeetSign(String conName,String contractorName,
			String enterpriseCode,String workCode,String flag,final int... rowStartIdxAndCount)
	{
		PageObject  result=new PageObject();
		String sql=
			"select\n" +
			"s.check_sign_id," +
			"s.con_id," +
			"s.con_name," +
			"s.report_code," +
			"s.budget_cost," +
			"s.contractor_id," +
			"s.contractor_name,\n" +
			"s.owner," +
			"to_char(s.start_date,'yyyy-MM-dd')," +
			"to_char(s.end_date,'yyyy-MM-dd')," +
			"s.change_info," +
			"s.devolve_on_info," +
			"s.memo, " +
			"getworkername( s.apply_by),\n" + 
			"to_char(s.apply_date ,'yyyy-MM-dd') ,\n" +
			" s.prj_id,\n" +
			" (select t.prj_name from PRJ_J_REGISTER t where t.prj_id = s.prj_id) as prj_name" +
			" from PRJ_J_CHECK_MEET_SIGN   s\n" + 
			"where  s.is_use='Y'\n" + 
			"and s.enterprise_code='"+enterpriseCode+"'"  ;
			if(conName!=null&&!"".equals(conName))
			{
				sql+="and s.con_name like  '%"+conName+"%'";
			}
			if(contractorName!=null&&!"".equals(contractorName))
			{
				sql+="and s.contractor_name like  '%"+contractorName+"%'";
			}
			if(flag.equals("fillQuery")){
				if(workCode != null&& !workCode.equals("999999"))
				{
					sql +=" and s.apply_by = '"+workCode+"'";
				}
			}
		String sqlCount="select count(1)  from ("+sql+")";
//		System.out.println("the sql"+sql);
		List  list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count=0l;
		Object obj=bll.getSingal(sqlCount);
		if(obj!=null)
		{
			count=Long.parseLong(obj.toString());
		}
		result.setList(list);
		result.setTotalCount(count);
		return result;
		
	}
	
	public void  delCheckMeetSign(String ids)
	{
		String sql="update PRJ_J_CHECK_MEET_SIGN  s\n " +
				" set  s.is_use='N' \n" +
				" where s.check_sign_id  in ("+ids+")";
		bll.exeNativeSQL(sql);
		
	}
	public Long save(PrjJCheckMeetSign entity) {
		LogUtil.log("saving PrjJCheckMeetSign instance", Level.INFO, null);
		try {
		Long 	checkSignId=bll.getMaxId("PRJ_J_CHECK_MEET_SIGN", "check_sign_id");
		    entity.setCheckSignId(checkSignId);
			entityManager.persist(entity);
			return checkSignId;
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PrjJCheckMeetSign entity) {
		LogUtil.log("deleting PrjJCheckMeetSign instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjJCheckMeetSign.class, entity
					.getCheckSignId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJCheckMeetSign update(PrjJCheckMeetSign entity) {
		LogUtil.log("updating PrjJCheckMeetSign instance", Level.INFO, null);
		try {
			PrjJCheckMeetSign result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJCheckMeetSign findById(Long id) {
		LogUtil.log("finding PrjJCheckMeetSign instance with id: " + id,
				Level.INFO, null);
		try {
			PrjJCheckMeetSign instance = entityManager.find(
					PrjJCheckMeetSign.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	 

}