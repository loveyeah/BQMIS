package power.ejb.hr.salary;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCSalaryLevel.
 * 
 * @see power.ejb.hr.salary.HrCSalaryLevel
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSalaryLevelFacade implements HrCSalaryLevelFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public HrCSalaryLevel save(HrCSalaryLevel entity) throws CodeRepeatException {
		LogUtil.log("saving HrCSalaryLevel instance", Level.INFO, null);
		try {
			if (!this.checkNameSame(entity.getSalaryLevelName())) {
				entity.setSalaryLevel(bll.getMaxId("HR_C_SALARY_LEVEL","salary_level"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql="update HR_C_SALARY_LEVEL t\n" +
			"   set t.is_use = 'N'\n" + 
			" where t.salary_level in ("+ids+")";

       bll.exeNativeSQL(sql);
	}

	public HrCSalaryLevel update(HrCSalaryLevel entity) throws CodeRepeatException {
		LogUtil.log("updating HrCSalaryLevel instance", Level.INFO, null);
		try {
			if(!this.checkNameSame(entity.getSalaryLevelName(), entity.getSalaryLevel()))
			{
			HrCSalaryLevel result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else{
				throw new CodeRepeatException("名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSalaryLevel findById(Long id) {
		LogUtil.log("finding HrCSalaryLevel instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSalaryLevel instance = entityManager.find(HrCSalaryLevel.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findSalaryLevelList(String enterpriseCode,String salaryLevelName,final int... rowStartIdxAndCount)
	{
		PageObject result = new PageObject();
		String sqlCount = "select count(*)\n" +
			"  from HR_C_SALARY_LEVEL a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.salary_level_name like '%"+salaryLevelName+"%'";
		Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(count > 0)
		{
			String sql = "select *\n" +
			"  from HR_C_SALARY_LEVEL a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.salary_level_name like '%"+salaryLevelName+"%'";
			List<HrCSalaryLevel> list = bll.queryByNativeSQL(sql, HrCSalaryLevel.class,
					rowStartIdxAndCount);
			result.setList(list);
			result.setTotalCount(count);
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	private boolean checkNameSame(String name,Long ... id)
	{
		String sql="select count(*)\n" +
			"  from HR_C_SALARY_LEVEL t\n" + 
			" where t.salary_level_name = '"+name+"'";

		if(id!=null&&id.length>0)
		{
			sql=sql+" and t.salary_level <>"+id[0];
		}
       int count=Integer.parseInt(bll.getSingal(sql).toString());
       if(count>0) return true;
       else return false;
		
	}

	/**
	 * add by liuyi 090929 13:20
	 */
	public List getSalaryLevelList() {
		String sql = "select a.salary_level, \n"
			+ "a.salary_level_name \n"
			+ "from HR_C_SALARY_LEVEL a \n"
			+ "where a.is_use='Y' \n";
		List list = bll.queryByNativeSQL(sql);
		return list;
	}
}