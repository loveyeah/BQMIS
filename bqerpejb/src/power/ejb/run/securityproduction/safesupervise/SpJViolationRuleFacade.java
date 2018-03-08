package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
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
public class SpJViolationRuleFacade implements SpJViolationRuleFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	
	public SpJViolationRule save(SpJViolationRule entity) {
		LogUtil.log("saving SpJViolationRule instance", Level.INFO, null);
		try {
			entity.setRuleId(bll.getMaxId("SP_J_VIOLATION_RULE", "rule_id"));
			entity.setIsUse("Y");
			entity.setLastModifiedDate(new Date());
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
			"update SP_J_VIOLATION_RULE t\n" +
			"set t.is_use='N'\n" + 
			"where t.rule_id in ("+ids+")";
		bll.exeNativeSQL(sql);

	}

	
	public SpJViolationRule update(SpJViolationRule entity) {
		LogUtil.log("updating SpJViolationRule instance", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new Date());
			SpJViolationRule result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJViolationRule findById(Long id) {
		LogUtil.log("finding SpJViolationRule instance with id: " + id,
				Level.INFO, null);
		try {
			SpJViolationRule instance = entityManager.find(
					SpJViolationRule.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findViolationRuleList(String strDate,String enterpriseCode,int... rowStartIdxAndCount)
	{
		PageObject obj=new PageObject();
		String sql=
			"select t.rule_id,\n" +
			"       t.emp_code,\n" + 
			"       GETWORKERNAME(t.emp_code),\n" + 
			"       t.dept_code,\n" + 
			"       GETDEPTNAME(t.dept_code),\n" + 
			"       to_char(t.examine_date, 'yyyy-MM-dd'),\n" + 
			"       t.examine_money,\n" + 
			"       t.phenomenon,\n" + 
			"       t.check_by,\n" + 
			"       GETWORKERNAME(t.check_by),\n" + 
			"       to_char(t.entry_date, 'yyyy-MM-dd')\n" + 
			"  from SP_J_VIOLATION_RULE t\n" + 
			" where \n" + 
			"    t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y'";
		if(strDate!=null&&!strDate.equals(""))
		{
			sql+="  and to_char(t.examine_date, 'yyyy-MM-dd') = '"+strDate+"' ";
		}

		String sqlCount="select count(1) from ("+sql+")";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		obj.setList(list);
		obj.setTotalCount(totalCount);
		return obj;
	}
	
	public PageObject queryViolationRuleList(String strMonth,String enterpriseCode,int... rowStartIdxAndCount)
	{
		PageObject obj=new PageObject();
		String sql=
			"select t.rule_id,\n" +
			"       t.emp_code,\n" + 
			"       GETWORKERNAME(t.emp_code),\n" + 
			"       t.dept_code,\n" + 
			"       GETDEPTNAME(t.dept_code),\n" + 
			"       to_char(t.examine_date, 'yyyy-MM-dd'),\n" + 
			"       t.examine_money,\n" + 
			"       t.phenomenon,\n" + 
			"       t.check_by,\n" + 
			"       GETWORKERNAME(t.check_by),\n" + 
			"       to_char(t.entry_date, 'yyyy-MM-dd'),\n" + 
			"       b.num,\n" + 
			"       b.money\n" + 
			"  from SP_J_VIOLATION_RULE t,\n" + 
			"       (select sum(1) num, sum(nvl(a.examine_money, 0)) money\n" + 
			"          from SP_J_VIOLATION_RULE a\n" + 
			"         where a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and a.is_use = 'Y'\n" + 
			"           and to_char(a.examine_date, 'yyyy-MM') = '"+strMonth+"') b\n" + 
			" where t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and to_char(t.examine_date, 'yyyy-MM') = '"+strMonth+"'";
		String sqlCount="select count(1) from ("+sql+")";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		obj.setList(list);
		obj.setTotalCount(totalCount);
		return obj;
	}

}