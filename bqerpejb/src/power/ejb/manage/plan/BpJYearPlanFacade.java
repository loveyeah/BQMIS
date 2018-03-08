package power.ejb.manage.plan;

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

/**
 * Facade for entity BpJYearPlan.
 * 
 * @see power.ejb.manage.plan.BpJYearPlan
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJYearPlanFacade implements BpJYearPlanFacadeRemote {
	// property constants
	public static final String STR_YEAR = "strYear";
	public static final String TITLE = "title";
	public static final String CONTENT_PATH = "contentPath";
	public static final String MEMO = "memo";
	public static final String ENTRY_BY = "entryBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public  PageObject getYearPlan(String year,String enterpriseCode,int... rowStartIdxAndCount)
	{
		PageObject result=new PageObject();
		String sql=
			"select y.year_plan_id," +
			"y.str_year," +
			"y.title," +
			"y.content_path," +
			"y.memo," +
			"y.entry_by,\n" +
			"getworkername(y.entry_by)," +
			"to_char(y.entry_date,'yyyy-MM-dd') " +
			" from  BP_J_YEAR_PLAN  y\n" + 
			"where  y.enterprise_code='"+enterpriseCode+"'\n" + 
			"and y.is_use='Y'";
		if(year!=null&&!year.equals(""))
		{
			sql+="and y.str_year='"+year+"'";
		}
		String sqlcount="select count(1)  from ("+sql+")";
//		System.out.println("the sql"+sql);
		Long count=Long.parseLong(bll.getSingal(sqlcount).toString());
	    List  list=	bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	    result.setList(list);
	    result.setTotalCount(count);
	    return result;
		
	}
	public void deleteYearPlan(String ids)
	{
		String sql="update BP_J_YEAR_PLAN  y \n " +
				"set y.is_use='N'\n " +
				"where y.year_plan_id in ("+ids+") ";
		bll.exeNativeSQL(sql);
	}
	public  BpJYearPlan save(BpJYearPlan entity) {
		LogUtil.log("saving BpJYearPlan instance", Level.INFO, null);
		try {

			entity.setYearPlanId(bll.getMaxId("BP_J_YEAR_PLAN", "year_plan_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);

			return entity;

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(BpJYearPlan entity) {
		LogUtil.log("deleting BpJYearPlan instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJYearPlan.class, entity
					.getYearPlanId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJYearPlan update(BpJYearPlan entity) {
		LogUtil.log("updating BpJYearPlan instance", Level.INFO, null);
		try {
			BpJYearPlan result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJYearPlan findById(Long id) {
		LogUtil.log("finding BpJYearPlan instance with id: " + id, Level.INFO,
				null);
		try {
			BpJYearPlan instance = entityManager.find(BpJYearPlan.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	


}