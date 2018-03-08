package power.ejb.equ.base;

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
 * Facade for entity EquCBugsolution.
 * 
 * @see power.ejb.equ.base.EquCBugsolution
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCBugsolutionFacade implements EquCBugsolutionFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public Long save(EquCBugsolution entity) {
		if(entity.getEquSolutionId()==null)
		{
			entity.setEquSolutionId(bll.getMaxId("equ_c_bugsolution", "equ_solution_id"));
		
			entity.setIsUse("Y");
		
		}
		entityManager.persist(entity);
		return entity.getEquSolutionId();
	}


	public void delete(Long solutionId) {
		EquCBugsolution entity=this.findById(solutionId);
		entity.setIsUse("N");
		this.update(entity);
	}


	
	public EquCBugsolution update(EquCBugsolution entity) {
		LogUtil.log("updating EquCBugsolution instance", Level.INFO, null);
		try {
			EquCBugsolution result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCBugsolution findById(Long id) {
		LogUtil.log("finding EquCBugsolution instance with id: " + id,
				Level.INFO, null);
		try {
			EquCBugsolution instance = entityManager.find(
					EquCBugsolution.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	@SuppressWarnings("unchecked")
	public List<EquCBugsolution> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCBugsolution instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCBugsolution model";
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
	
	@SuppressWarnings("unchecked")
	public PageObject findBugSolutionList(String solutinDesc,String reasonId,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from equ_c_bugsolution t\n" +
				"where t.equ_solution_desc like '%"+solutinDesc+"%'\n" + 
				"and t.bug_reason_id="+reasonId+"\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";


			List<EquCBugsolution> list=bll.queryByNativeSQL(sql, EquCBugsolution.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from equ_c_bugsolution t\n" +
			"where t.equ_solution_desc like '%"+solutinDesc+"%'\n" + 
			"and t.bug_reason_id="+reasonId+"\n" + 
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
	
	public void deleteAllByReasonId(Long reasonId,String enterpriseCode)
	{
		String sql=
			"update equ_c_bugsolution t\n" +
			"set t.is_use='N'\n" + 
			"where t.bug_reason_id="+reasonId+"\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
		bll.exeNativeSQL(sql);
	}

}