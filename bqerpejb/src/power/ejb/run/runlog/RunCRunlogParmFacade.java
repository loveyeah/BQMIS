package power.ejb.run.runlog;

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
 * Facade for entity RunCRunlogParm.
 * 
 * @see power.ejb.run.runlog.RunCRunlogParm
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCRunlogParmFacade implements RunCRunlogParmFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public Long save(RunCRunlogParm entity) {
		
			if(entity.getRunlogParmId()==null)
			{
				entity.setRunlogParmId(bll.getMaxId("RUN_C_RUNLOG_PARM", "runlog_parm_id"));
				entity.setIsUse("Y");
				
			}
			entityManager.persist(entity);
			return entity.getRunlogParmId();
	}

	
	public void delete(Long id) {
		RunCRunlogParm entity=this.findById(id);
		entity.setIsUse("N");
		this.update(entity);
		
	}

	
	public RunCRunlogParm update(RunCRunlogParm entity) {
		LogUtil.log("updating RunCRunlogParm instance", Level.INFO, null);
		try {
			RunCRunlogParm result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCRunlogParm findById(Long id) {
		LogUtil.log("finding RunCRunlogParm instance with id: " + id,
				Level.INFO, null);
		try {
			RunCRunlogParm instance = entityManager.find(RunCRunlogParm.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCRunlogParm> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCRunlogParm instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCRunlogParm model";
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
	
	
	public PageObject findParmList(String itemCodeOrName,String specialCode, String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from RUN_C_RUNLOG_PARM t\n" +
				"where (t.item_code like '%"+itemCodeOrName+"%'\n" + 
				"or t.item_name like '%"+itemCodeOrName+"%')\n" + 
				"and t.speciality_code like '"+specialCode+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y' order by t.diaplay_no ";

			List<RunCRunlogParm> list=bll.queryByNativeSQL(sql, RunCRunlogParm.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from RUN_C_RUNLOG_PARM t\n" +
				"where (t.item_code like '%"+itemCodeOrName+"%'\n" + 
				"or t.item_name like '%"+itemCodeOrName+"%')\n" + 
				"and t.speciality_code like '"+specialCode+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";

			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public List<RunCRunlogParm> findListBySpecial(String specialcode,String enterprisecode){
		String sql="select t.*\n" +
			" from run_c_runlog_parm t\n" + 
			"where t.speciality_code = '"+specialcode+"'\n" + 
			"  and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"  and t.is_use = 'Y'";
		return bll.queryByNativeSQL(sql, RunCRunlogParm.class);
	}
}