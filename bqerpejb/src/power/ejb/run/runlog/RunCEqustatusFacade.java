package power.ejb.run.runlog;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCEqustatus.
 * 
 * @see power.ejb.run.runlog.RunCEqustatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCEqustatusFacade implements RunCEqustatusFacadeRemote {
	public static final String IS_USE = "isUse";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCEqustatus save(RunCEqustatus entity) throws CodeRepeatException{
		LogUtil.log("saving RunCEqustatus instance", Level.INFO, null);
		try {
			if(!this.checkName(entity.getEnterpriseCode(), entity.getStatusName()))
			{
				entity.setEqustatusId(bll.getMaxId("run_c_equstatus", "equstatus_id"));	
				entity.setIsUse("Y");
				entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("状态名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(RunCEqustatus entity) {
		LogUtil.log("deleting RunCEqustatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCEqustatus.class, entity
					.getEqustatusId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCEqustatus update(RunCEqustatus entity) throws CodeRepeatException {
		LogUtil.log("updating RunCEqustatus instance", Level.INFO, null);
		try {
			if(!this.checkName(entity.getEnterpriseCode(), entity.getStatusName(), entity.getEqustatusId()))
			{
			RunCEqustatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("状态名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCEqustatus findById(Long id) {
		LogUtil.log("finding RunCEqustatus instance with id: " + id,
				Level.INFO, null);
		try {
			RunCEqustatus instance = entityManager
					.find(RunCEqustatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCEqustatus> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCEqustatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCEqustatus model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
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
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCEqustatus> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCEqustatus instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCEqustatus model";
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

	
	public List<RunCEqustatus> findByIsUse(Object isUse,int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}
	public List<RunCEqustatus> findList(String enterpriseCode){
		String sql="select r.*\n" +
			"  from run_c_equstatus r\n" + 
			" where r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'";
		return bll.queryByNativeSQL(sql, RunCEqustatus.class);
	}
	
	private boolean checkName(String enterpriseCode, String statusName,Long... equstatusId)
	{
		boolean isSame = false;
		String sql ="select count(1) from run_c_equstatus s\n" +
			"      where s.is_use = 'Y'\n" + 
			"      and s.enterprise_code = '"+enterpriseCode+"'\n" + 
			"      and s.status_name = '"+statusName+"'";


	    if(equstatusId !=null&& equstatusId.length>0){
	    	sql += "  and s.equstatus_id <> " + equstatusId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
}